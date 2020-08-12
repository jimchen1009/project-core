package com.project.game.config.export.excel.convert;

public class ExcelDoubleConverter extends ExcelCellConverter {

	public ExcelDoubleConverter() {
		super("double", (double) 0);
	}

	@Override
	protected Object string2ClassValue(String string) {
		return Double.parseDouble(string);
	}

	@Override
	protected Object double2ClassValue(double numericCellValue) {
		return numericCellValue;
	}
}
