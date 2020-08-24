package com.project.core.config.export.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.config.EvnConfigUtil;
import com.game.common.config.IEvnConfig;
import com.game.common.util.DateFormatUtil;
import com.project.config.IDataSource;
import com.project.core.config.data.DataMapper;
import com.project.core.config.export.ModelExportField;
import com.project.core.config.export.ModelFieldConfig;
import com.project.core.config.export.ModelTypeConfig;
import com.project.core.config.export.TemplateExportUtil;
import freemarker.template.TemplateException;
import jodd.io.FileUtil;
import jodd.util.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelExportUtil {

	/**
	 * excel 配置表导出成Json文件、Java类文件
	 * @param fileName
	 * @param configName
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static void exportExcel2JsonAndClass(String fileName, String configName) throws Exception {
		File file = new File(fileName);
		if (!file.exists()){
			throw new RuntimeException("不存在文件:" + fileName);
		}
		IEvnConfig config = EvnConfigUtil.loadConfig(configName);
		exportExcel2JsonAndClass(file, config);
	}

	/**
	 * excel 配置表导出成Json文件、Java类文件
	 * @param file
	 * @param config
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private static void exportExcel2JsonAndClass(File file, IEvnConfig config) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		int numberOfSheets = workbook.getNumberOfSheets();
		int firstIndex = config.getInt("field.firstIndex");
		int lastIndex = config.getInt("field.lastIndex");
		if (lastIndex - firstIndex + 1 != ModelFieldConfig.Count) {
			throw new RuntimeException(String.format("字段信息行数错误:%s-%s", firstIndex, lastIndex));
		}
		String jsonDirectory = checkAndGetDirectory(config, "jsonPath");
		for (int index = 0; index < numberOfSheets; index++) {
			XSSFSheet sheet = workbook.getSheetAt(index);
			String sheetName = sheet.getSheetName();
			if (sheetName.indexOf("export_") != 0){
				continue;
			}
			if (sheet.getLastRowNum() < lastIndex){
				throw new RuntimeException(String.format("页签: %s.%s 行数小于%s,格式错误", file.getName(), sheetName, lastIndex));
			}
			String jsonName = ExcelCellConvertUtil.readString(sheet.getRow(0).getCell(0)) + ".json";
			String javaClassName = ExcelCellConvertUtil.readString(sheet.getRow(0).getCell(1));
			String javaConfigClass = ExcelCellConvertUtil.readString(sheet.getRow(0).getCell(2));
			int primaryIndex = config.getInt("field.primaryIndex");
			List<ModelFieldConfig> fieldConfigs = readFieldConfigList(sheet, primaryIndex, firstIndex, lastIndex);
			exportExcel2Json(sheet, lastIndex, fieldConfigs, jsonName, jsonDirectory);
			exportExcel2JavaClass(file.getName(), sheetName, jsonName, javaClassName, javaConfigClass, fieldConfigs, config);
		}
	}

	private static final Map<String, DataMapper.ConfigClass> name2ConfigClass = DataMapper.getName2ConfigClass();

	private static void exportExcel2JavaClass(String filename, String sheetName, String jsonName, String javaClassName, String javaConfigClass,
											  List<ModelFieldConfig> fieldConfigs, IEvnConfig config) throws IOException, TemplateException, ClassNotFoundException {
		Map<String, ModelTypeConfig> typeConfigMap = ModelTypeConfig.readTypeConfigMap(config.getConfigList("field.types"));
		List<ModelExportField> exportFieldList = new ArrayList<>(fieldConfigs.size());
		for (ModelFieldConfig fieldConfig : fieldConfigs) {
			ModelTypeConfig typeConfig = typeConfigMap.get(fieldConfig.getType()).deepCopy();
			ModelExportField exportField = new ModelExportField(fieldConfig, typeConfig);
			exportFieldList.add(exportField);
		}

		String javaDirectory = checkAndGetDirectory(config, "javaPath");
		String javaPackage = config.getString("javaPackage");
		String annotation = "文件创建时间:" + DateFormatUtil.formatYMDHMS(new Date()) + " 创建者:" + System.getProperties().getProperty("user.name") + "\n";


		String javaDataClassName = javaClassName + "Data";
		Map<String, Object> map = new HashMap<>();
		map.put("javaPackage", javaPackage);
		map.put("filename", String.format("%s[%s]", filename, sheetName));
		map.put("name", jsonName);
		map.put("javaClassName", javaDataClassName);
		map.put("fieldList", beforeProcessJavaDataClass(exportFieldList));
		map.put("annotation", annotation);


		String exportDataName = String.format("%s/%s/%s.java", javaDirectory, javaPackage.replace(".", "/"), javaDataClassName);
		TemplateExportUtil.create("/export/data_model.ftl", exportDataName, map, true);

		long primaryCount = fieldConfigs.stream().filter(ModelFieldConfig::isPrimaryKey).count();
		map.put("javaClassName", javaClassName);
		map.put("javaDataClassName", javaDataClassName);
		map.put("primaryCount", primaryCount);
		String exportName = String.format("%s/%s/%s.java", javaDirectory, javaPackage.replace(".", "/"), javaClassName);
		TemplateExportUtil.create("/export/data_source.ftl", exportName, map, true);


		if (!StringUtil.isEmpty(javaConfigClass)){
			String mapperDirectory = checkAndGetDirectory(config, "mapperPath");

			int indexOf = javaConfigClass.lastIndexOf(".");
			String javaConfigName = javaConfigClass.substring(indexOf + 1);
			String javaConfigPackage = javaConfigClass.substring(0, indexOf);

			map.put("javaSourceClassName", javaClassName);
			map.put("javaClassName", javaConfigName);
			map.put("javaDataPackage", javaPackage);
			map.put("javaPackage", javaConfigPackage);

			String javaConfigDirectory = checkAndGetDirectory(String.format("%s/%s", mapperDirectory, javaConfigPackage.replace(".", "/")));
			String exportConfigName = String.format("%s/%s.java", javaConfigDirectory, javaConfigName);
			if (primaryCount == 1){
				TemplateExportUtil.create("/export/model_config1.ftl", exportConfigName, map, false);
			}
			else if (primaryCount == 2){
				TemplateExportUtil.create("/export/model_config2.ftl", exportConfigName, map, false);
			}
			else {
				throw new UnsupportedOperationException("primaryCount = " + primaryCount);
			}
			if (name2ConfigClass.containsKey(jsonName)) {
				return;
			}
			//先编译好生成的Java文件~
			compileJava(Arrays.asList(exportDataName, exportName, exportConfigName));
			@SuppressWarnings("unchecked")
			Class<? extends IDataSource> dataClass = (Class<? extends IDataSource>)Class.forName(String.format("%s.%s", javaPackage, javaClassName));
			Class<?> aClass = Class.forName(javaConfigClass);
			DataMapper.ConfigClass configClass = new DataMapper.ConfigClass(jsonName, dataClass, aClass);
			name2ConfigClass.put(configClass.getName(), configClass);

			List<DataMapper.ConfigClass> configClassList = new ArrayList<>(name2ConfigClass.values());

			configClassList.sort(Comparator.comparing(DataMapper.ConfigClass::getName));
			map.put("classConfigList", configClassList);
			String mapperPackage = DataMapper.class.getPackage().getName();
			String exportMapperName = String.format("%s/%s/%s.java", mapperDirectory, mapperPackage.replace(".", "/"), DataMapper.class.getSimpleName());
			TemplateExportUtil.create("/export/data_mapper.ftl", exportMapperName, map, true);
		}
	}

	private static void compileJava(List<String> exportJavaNameList) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector diagnostics = new DiagnosticCollector();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		// 建立源文件对象，每个文件被保存在一个从JavaFileObject继承的类中
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(exportJavaNameList);
		// options命令行选项
		URL resource = IDataSource.class.getResource("");
		String name = IDataSource.class.getPackage().getName().replace(".", "/");
		FileUtil.mkdir(resource.getPath() + "/model/ext");
		int indexOf = resource.getPath().lastIndexOf(name);
		String substring = resource.getPath().substring(0, indexOf - 1);
		Iterable<String> options = Arrays.asList("-d", substring);// 指定的路径一定要存在，javac不会自己创建文件夹
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);

		// 编译源程序
		boolean success = task.call();
		fileManager.close();
		System.out.println((success) ? "编译JAVA文件成功" : "编译JAVA文件失败");
	}


	private static List<ModelExportField> beforeProcessJavaDataClass(List<ModelExportField> fieldList){
		List<String> typImportList = new ArrayList<>();
		List<ModelExportField> changeFieldList = new ArrayList<>();
		for (ModelExportField field : fieldList) {
			ModelTypeConfig typeConfig = field.getTypeConfig().deepCopy();
			if (typImportList.contains(typeConfig.getTypeImport())){
				typeConfig.setTypeImport(null);
			}
			else {
				typImportList.add(typeConfig.getTypeImport());
			}
			//主要是为了避免重复import, 特殊处理一下。
			changeFieldList.add(new ModelExportField(field.getFieldConfig(), typeConfig));
		}
		return changeFieldList;
	}

	private static String checkAndGetDirectory(IEvnConfig config, String path){
		return checkAndGetDirectory(config.getString(path));
	}

	private static String checkAndGetDirectory(String pathDirectory){
		File directory = new File(pathDirectory);
		if (directory.exists()){
			if (!directory.isDirectory()){
				throw new RuntimeException("不是目录类型:" + pathDirectory);
			}
		}
		else {
			if (!directory.mkdirs()){
				throw new RuntimeException("创建目录失败:" + pathDirectory);
			}
		}
		return pathDirectory;
	}

	/**
	 * 导出Json文件
	 * @param sheet
	 * @param firstIndex
	 * @param fieldConfigs
	 * @param jsonName
	 * @param jsonDirectory
	 * @throws IOException
	 */
	private static void exportExcel2Json(XSSFSheet sheet, int firstIndex, List<ModelFieldConfig> fieldConfigs, String jsonName, String jsonDirectory) throws Exception {
		List<Map<ModelFieldConfig, Object>> excelReadConfigValueList = new ArrayList<>();
		for (int rowIndex = firstIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			XSSFRow row = sheet.getRow(rowIndex);
			if (row == null){
				continue;	//存在空行
			}
			Map<ModelFieldConfig, Object> readConfigValueMap = new HashMap<>();
			for (ModelFieldConfig fieldConfig : fieldConfigs) {
				XSSFCell xssfCell = row.getCell(fieldConfig.getIndex());
				if (fieldConfig.getIndex() == 0 && StringUtil.isEmpty(ExcelCellConvertUtil.readString(xssfCell))){
					readConfigValueMap.clear();
					break;
				}
				else {
					Object readConfigValue = ExcelCellConvertUtil.readConfigValue(xssfCell, fieldConfig);
					readConfigValueMap.put(fieldConfig, readConfigValue);
				}
			}
			if (!readConfigValueMap.isEmpty()){
				excelReadConfigValueList.add(readConfigValueMap);
			}
		}

		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < excelReadConfigValueList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			Map<ModelFieldConfig, Object> readConfigValueMap = excelReadConfigValueList.get(i);
			for (ModelFieldConfig fieldConfig : fieldConfigs) {
				jsonObject.put(fieldConfig.getName(), readConfigValueMap.get(fieldConfig));
			}
			if (i > 0){
				builder.append(",");
			}
			builder.append("\n\t").append(JSONArray.toJSONString(jsonObject));
		}
		builder.append("\n]");
		File file = new File(String.format("%s/%s", jsonDirectory, jsonName));
		FileWriter writer = new FileWriter(file);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
		String absolutePath = file.getAbsolutePath();
		System.out.println("导出JSON文件成功: " + absolutePath);
	}


	private static List<ModelFieldConfig> readFieldConfigList(XSSFSheet sheet, int primaryKeyIndex, int firstIndex, int lastIndex) throws Exception {
		XSSFRow firstXssRow = sheet.getRow(firstIndex - 1);
		int numberOfCells = firstXssRow.getLastCellNum();
		List<ModelFieldConfig> fieldConfigs = new ArrayList<>();
		for (int number = 0; number < numberOfCells; number++) {
			String name = ExcelCellConvertUtil.readString(firstXssRow.getCell(number));
			if (StringUtil.isEmpty(name)){
				continue;	//空列
			}
			List<String> nameList = new ArrayList<>();
			for (int index = firstIndex - 1; index < lastIndex; index++) {
				XSSFRow xssfRow = sheet.getRow(index);
				nameList.add(ExcelCellConvertUtil.readString(xssfRow.getCell(number)));
			}
			boolean isPrimaryKey = false;
			XSSFCell cell = sheet.getRow(primaryKeyIndex - 1).getCell(number);
			if (cell != null){
				String readString = ExcelCellConvertUtil.readString(cell);
				if (!StringUtil.isEmpty(readString)){
					if (readString.trim().equals("PrimaryKey")){
						isPrimaryKey = true;
					}
					else {
						throw new RuntimeException("主键名字错误:" + readString);
					}
				}
			}
			ModelFieldConfig fieldConfig = new ModelFieldConfig(number, isPrimaryKey, nameList.get(0), nameList.get(1), nameList.get(2), nameList.get(3));
			fieldConfigs.add(fieldConfig);
		}
		return fieldConfigs;
	}
}
