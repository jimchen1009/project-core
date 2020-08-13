package com.project.game.config.export;

import com.project.game.config.export.excel.ExcelExportUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

public class TemplateExportUtil {

	private static final Logger logger = LoggerFactory.getLogger(TemplateExportUtil.class);

	public static void create(String templateName, String exportName, Map<String, Object> paramMap, boolean forceUpdate) throws IOException, TemplateException {
		File file = new File(exportName);
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new RuntimeException("创建文件夹失败：" + file.getParentFile().getAbsolutePath());
			}
		}
		if (forceUpdate || !file.exists()){
			Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			URL resource = ExcelExportUtil.class.getResource(templateName);
			File resourceFile = new File(resource.getPath());
			configuration.setDirectoryForTemplateLoading(resourceFile.getParentFile());
			configuration.setClassicCompatible(false);
			Template template = configuration.getTemplate(resourceFile.getName());
			Writer writer = new FileWriter(file);
			template.process(paramMap, writer);
			writer.flush();
			writer.close();
			System.out.println("导出模板文件成功: " + exportName);
		}
		else {
			System.out.println("模板文件已经存在, 忽略不导出: " + exportName);
		}
	}
}
