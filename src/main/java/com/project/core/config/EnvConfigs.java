package com.project.core.config;

import com.game.common.config.EvnConfigUtil;
import com.game.common.config.IEvnConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EnvConfigs {

	private static IEvnConfig myConfig = EvnConfigUtil.loadConfig(System.getProperty("game.project.config.path", "project_config.conf"));

	public static boolean hasPath(String path) {
		return myConfig.hasPath(path);
	}

	public static IEvnConfig getConfig(String path){
		return myConfig.getConfig(path);
	}

	public static int getInt(String path){
		return myConfig.getInt(path);
	}

	public static long getLong(String path){
		return myConfig.getLong(path);
	}

	public static String getString(String path) {
		return myConfig.getString(path);
	}

	public static boolean getBoolean(String path) {
		return myConfig.getBoolean(path);
	}

	public static List<IEvnConfig> getConfigList(String path){
		return myConfig.getConfigList(path);
	}

	public static <T> List<T> getList(String path){
		return myConfig.getList(path);
	}

	public static long getDuration(String path, TimeUnit timeUnit) {
		return myConfig.getDuration(path, timeUnit);
	}
}
