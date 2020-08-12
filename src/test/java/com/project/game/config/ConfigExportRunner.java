package com.project.game.config;

import com.project.game.config.export.excel.ExcelExportUtil;
import org.junit.Test;

public class ConfigExportRunner {

	@Test
	public void export() throws Exception {
		String fileName = "C:\\ProjectG\\pjg-product\\AExcel\\develop\\excel\\activity193.xlsx";
		String configName = "D:\\demo\\project-config\\src\\main\\resources\\export\\excel2json.conf";
		ExcelExportUtil.exportExcel2JsonAndClass(fileName, configName);
	}
}
