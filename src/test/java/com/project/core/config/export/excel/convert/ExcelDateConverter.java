package com.project.core.config.export.excel.convert;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;

public class ExcelDateConverter extends ExcelCellConverter {

	public ExcelDateConverter() {
		super("date", null);
	}

	@Override
	protected Object string2ClassValue(String string) throws ParseException {
		return DateUtils.parseDate(string, "yyyy-MM-dd HH:mm:ss");
	}
}
