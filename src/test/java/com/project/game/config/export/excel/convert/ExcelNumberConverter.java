package com.project.game.config.export.excel.convert;

public class ExcelNumberConverter extends ExcelCellConverter {

	public ExcelNumberConverter() {
		super("number",0);
	}

	@Override
	protected Object string2ClassValue(String string) {
		return Integer.parseInt(string);
	}

	@Override
	protected Object double2ClassValue(double numericCellValue) {
		return (int)numericCellValue;
	}
}
