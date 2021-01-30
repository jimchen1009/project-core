package com.project.core.config.export.excel.convert;

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
		long longCellValue = (long) numericCellValue;
		double decimal = numericCellValue - longCellValue;
		return decimal > 0 ? String.valueOf(numericCellValue) : String.valueOf(longCellValue);
	}
}
