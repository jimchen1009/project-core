package com.project.core.config.export.excel.convert;

import jodd.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelListConverter extends ExcelCellConverter {

	private final ExcelCellConverter cellConverter;

	public ExcelListConverter(String type, ExcelCellConverter cellConverter) {
		super(type, null);
		this.cellConverter = cellConverter;
	}

	@Override
	protected Object string2ClassValue(String string) throws Exception {
		if (StringUtil.isEmpty(string)) {
			return Collections.emptyList();
		}
		String[] strings = string.split(",");
		List<Object> objectList = new ArrayList<>(strings.length);
		for (String s : strings) {
			objectList.add(cellConverter.string2ClassValue(s));
		}
		return objectList;
	}

	@Override
	protected Object double2ClassValue(double numericCellValue) throws Exception {
		return Collections.singletonList(cellConverter.double2ClassValue(numericCellValue));
	}
}
