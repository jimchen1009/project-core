package com.project.core.config.data;

import com.project.config.IDataSource;
import com.project.core.config.EnvConfigs;
import jodd.io.FileUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataConfigs {

	private static final String PackageName = "com.project.config.model";

	private static final Logger logger = LoggerFactory.getLogger(DataConfigs.class);

	private static final Map<String, IDataSource> class2Sources = initDataSourceClasses();

	public static void init(){
		Collection<IDataSource> dataSources = class2Sources.values();
		for (IDataSource dataSource : dataSources) {
			try {
				reloadDataSourceJson(dataSource);
				logger.debug("load datasource[{}] success.", dataSource.getName());
			}
			catch (Throwable t){
				logger.error("load datasource[{}] failure.", dataSource.getName(), t);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T extends IDataSource> T getDataSource(Class<T> aClass){
		return (T) class2Sources.get(aClass.getName());
	}

	public static void reloadDataSource(String name){
		for (Map.Entry<String, IDataSource> entry : class2Sources.entrySet()) {
			IDataSource dataSource = entry.getValue();
			if (dataSource.getName().equals(name)){
				try {
					IDataSource instanceSource = dataSource.getClass().newInstance();
					reloadDataSourceJson(instanceSource);
					class2Sources.put(entry.getKey(), instanceSource);
					logger.error("reload datasource[{}] success, class:[{}].", name, dataSource.getClass().getSimpleName());
				}
				catch (Throwable e) {
					logger.error("reload datasource[{}] failure, class:[{}].", name, dataSource.getClass().getSimpleName(), e);
				}
				return;
			}
		}
		logger.error("couldn't find data source name[{}].", name);
	}

	/**
	 * 加载配置类~
	 * @return
	 */
	private static Map<String, IDataSource> initDataSourceClasses(){
		Map<String, IDataSource> dataSourceMap = new HashMap<>();
		try {
			Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(PackageName.replace('.', '/'));
			URL url = resources.nextElement();
			//获取包的物理路径
			String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
			File directory = new File(filePath);
			if (directory.isDirectory()) {
				File[] files = Objects.requireNonNull(directory.listFiles());
				for (File file : files) {
					if (file.isDirectory()) {
						continue;
					}
					int length = file.getName().length() - 6;
					if (file.getName().lastIndexOf(".class") != length) {
						continue;
					}
					String className = file.getName().substring(0, length);
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(PackageName + '.' + className);
					if (!IDataSource.class.isAssignableFrom(loadClass)) {
						continue;
					}
					IDataSource dataSource = (IDataSource)loadClass.newInstance();
					dataSourceMap.put(dataSource.getClass().getName(), dataSource);
				}
			}
			else {
				logger.error("it's not directory, name={}", directory.getAbsolutePath());
			}
		}
		catch (Throwable t){
			logger.error("load package classes error, name={}", PackageName, t);
		}
		return dataSourceMap;
	}

	private static void reloadDataSourceJson(IDataSource dataSource) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String resourceJson = EnvConfigs.getString("configPath.resourceJson");
		String[] readLines = FileUtil.readLines(resourceJson + File.separator + dataSource.getName(), "UTF-8");
		String jsonString = StringUtil.join(readLines, "\n");
		dataSource.loadJson(jsonString);

		DataMapper.onDataSourceReload(dataSource);
	}
}
