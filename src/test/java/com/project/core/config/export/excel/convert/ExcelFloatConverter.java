package com.project.core.config.export.excel.convert;

public class ExcelFloatConverter extends ExcelCellConverter {

	public ExcelFloatConverter() {
		super("float", (float)0);
	}

	@Override
	protected Object string2ClassValue(String string) {
		return Float.parseFloat(string);
	}

	@Override
	protected Object double2ClassValue(double numericCellValue) {
		return (float)numericCellValue;
	}
}
