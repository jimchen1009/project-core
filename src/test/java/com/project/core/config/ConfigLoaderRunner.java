package com.project.core.config;

import com.project.core.config.data.DataConfigs;
import org.junit.Test;

public class ConfigLoaderRunner {

	@Test
	public void reloadConfig() {
		System.setProperty("game.project.config.path", "D:/demo/project-core/src/main/resources/project_config.conf");
		DataConfigs.init();
	}
}
