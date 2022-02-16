package com.project.core.config.data;

import com.project.config.IDataSource;
import com.project.core.config.EnvConfigs;
import jodd.io.findfile.ClassScanner;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
		class2Sources.values().forEach(DataMapper::touchDataSource);
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
					DataMapper.reloadDataSource(instanceSource);
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
		ClassScanner scanner = new ClassScanner(){
			@Override
			protected void onEntry(EntryData entryData) {
				super.onEntry(entryData);
				String className = PackageName + '.' + entryData.name();
				try {
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
					if (!IDataSource.class.isAssignableFrom(loadClass)) {
						return;
					}
					IDataSource dataSource = (IDataSource)loadClass.newInstance();
					dataSourceMap.put(dataSource.getClass().getName(), dataSource);
				}
				catch (Throwable throwable){
					logger.error("newInstance class error, name={}", className, throwable);
				}
			}
		};
		scanner.excludeAllJars(true);
		scanner.includeResources(false);
		URL resource = Thread.currentThread().getContextClassLoader().getResource(PackageName.replace('.', '/'));
		scanner.scan(resource).start();
		return dataSourceMap;
	}

	private static void reloadDataSourceJson(IDataSource dataSource) throws IOException{
		String resourceJson = EnvConfigs.getString("resourceJson");
		String filePath = resourceJson + File.separator + dataSource.getName();
		dataSource.loadFile(filePath);
	}
}
