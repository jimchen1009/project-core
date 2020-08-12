package com.project.game.config.export.excel.convert;

import org.apache.poi.xssf.usermodel.XSSFCell;

public interface IExcelCellConverter {

	String getType();

	Object convert2ClassValue(XSSFCell xssfCell) throws Exception;
}
