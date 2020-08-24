package com.project.core.config.export.excel;

import com.project.core.config.export.ModelFieldConfig;
import com.project.core.config.export.excel.convert.ExcelBoolConverter;
import com.project.core.config.export.excel.convert.ExcelCellConverter;
import com.project.core.config.export.excel.convert.ExcelDateConverter;
import com.project.core.config.export.excel.convert.ExcelDoubleConverter;
import com.project.core.config.export.excel.convert.ExcelFloatConverter;
import com.project.core.config.export.excel.convert.ExcelLanguageConverter;
import com.project.core.config.export.excel.convert.ExcelListConverter;
import com.project.core.config.export.excel.convert.ExcelNumberConverter;
import com.project.core.config.export.excel.convert.ExcelStringConverter;
import com.project.core.config.export.excel.convert.IExcelCellConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelCellConvertUtil {

	private static final Map<String, IExcelCellConverter> converters = new ConcurrentHashMap<>();

	static {
		addConverter(new ExcelStringConverter());
		addConverter(new ExcelDateConverter());
		addConverter(new ExcelDoubleConverter());
		addConverter(new ExcelFloatConverter());
		addConverter(new ExcelNumberConverter());
		addConverter(new ExcelLanguageConverter());
		addConverter(new ExcelBoolConverter());
	}

	private static void addConverter(ExcelCellConverter cellConverter){
		converters.put(cellConverter.getType(), cellConverter);
		ExcelListConverter listConverter = new ExcelListConverter(String.format("[%s]", cellConverter.getType()), cellConverter);
		converters.put(listConverter.getType(), listConverter);
	}

	public static Object readConfigValue(XSSFCell xssfCell, ModelFieldConfig fieldConfig) throws Exception {
		IExcelCellConverter converter = getConverter(fieldConfig.getType());
		if (converter == null){
			throw new UnsupportedOperationException("不支持类型:" + fieldConfig.getType());
		}
		return converter.convert2ClassValue(xssfCell);
	}

	public static String readString(XSSFCell xssfCell) throws Exception {
		IExcelCellConverter converter = getConverter("string");
		return (String) converter.convert2ClassValue(xssfCell);
	}

	private static IExcelCellConverter getConverter(String type){
		return converters.get(type);
	}
}
