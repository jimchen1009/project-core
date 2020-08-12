package com.project.game.config.export.excel.convert;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

public abstract class ExcelCellConverter implements IExcelCellConverter {

	private final String type;
	private final Object blankClassValue;


	public ExcelCellConverter(String type, Object blankClassValue) {
		this.type = type;
		this.blankClassValue = blankClassValue;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public final Object convert2ClassValue(XSSFCell xssfCell) throws Exception {
		if (xssfCell == null) {
			return blankClassValue;
		}
		CellType cellType = xssfCell.getCellType();
		if (cellType.equals(CellType.FORMULA)){
			cellType = xssfCell.getCachedFormulaResultType();
		}
		if (cellType.equals(CellType.NUMERIC)){
			if (DateUtil.isCellDateFormatted(xssfCell)) {
				return xssfCell.getDateCellValue();
			}
			else {
				double numericCellValue = xssfCell.getNumericCellValue();
				return double2ClassValue(numericCellValue);
			}
		}
		else if (cellType.equals(CellType.STRING)){
			String stringCellValue = xssfCell.getStringCellValue();
			return string2ClassValue(stringCellValue);
		}
		else if (cellType.equals(CellType.BLANK)){
			return blankClassValue;
		}
		else if (cellType.equals(CellType.BOOLEAN)){
			return  xssfCell.getBooleanCellValue();
		}
		else {
			throw new UnsupportedOperationException("不支持类型:" + cellType.name());
		}
	}

	protected Object double2ClassValue(double numericCellValue){
		throw new UnsupportedOperationException(this.getClass().getSimpleName() + ", 不支持方法.");
	}

	protected Object string2ClassValue(String string) throws Exception{
		throw new UnsupportedOperationException(this.getClass().getSimpleName() + ", 不支持方法.");
	}
}
