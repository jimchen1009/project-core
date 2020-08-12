package com.project.game.config.export.excel.convert;

public class ExcelStringConverter extends ExcelCellConverter {

	public ExcelStringConverter() {
		super("string", null);
	}

	@Override
	protected Object string2ClassValue(String string) {
		return string;
	}

	@Override
	protected Object double2ClassValue(double numericCellValue) {
		return String.valueOf(numericCellValue);
	}
}
