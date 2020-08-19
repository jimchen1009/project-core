package com.project.core.config;

import com.project.core.config.export.excel.ExcelExportUtil;
import org.junit.Test;

public class ConfigExportRunner {

	@Test
	public void export() throws Exception {
		String fileName = "D:\\demo\\project-config\\src\\main\\resources\\export\\excel\\demo.xlsx";
		String configName = "D:\\demo\\project-config\\src\\main\\resources\\export\\excel2json.conf";
		ExcelExportUtil.exportExcel2JsonAndClass(fileName, configName);
	}
}
