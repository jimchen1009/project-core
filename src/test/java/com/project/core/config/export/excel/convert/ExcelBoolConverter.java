package com.project.core.config.export.excel.convert;

public class ExcelBoolConverter extends ExcelCellConverter {

	public ExcelBoolConverter() {
		super("bool", false);
	}

	@Override
	protected Object string2ClassValue(String string) {
		return Boolean.parseBoolean(string);
	}

	@Override
	protected Object double2ClassValue(double numericCellValue) {
		return numericCellValue > 0;
	}
}
