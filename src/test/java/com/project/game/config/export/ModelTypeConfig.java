package com.project.game.config.export;

import com.game.common.config.IEvnConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelTypeConfig {

	private final String type;
	private final String typeClass;
	private String typeImport;

	public ModelTypeConfig(String type, String typeClass, String typeImport) {
		this.type = type;
		this.typeClass = typeClass;
		this.typeImport = typeImport;
	}

	public String getType() {
		return type;
	}


	public String getTypeClass() {
		return typeClass;
	}

	public String getTypeImport() {
		return typeImport;
	}

	public void setTypeImport(String typeImport) {
		this.typeImport = typeImport;
	}

	public ModelTypeConfig deepCopy(){
		return new ModelTypeConfig(type, typeClass, typeImport);
	}

	public static Map<String, ModelTypeConfig> readTypeConfigMap(List<IEvnConfig> configs){
		Map<String, ModelTypeConfig> configMap = new HashMap<>();
		for (IEvnConfig config : configs) {
			String type = config.getString("type");
			String aClass = config.getString("typeClass");
			String aImport = config.hasPath("typeImport") ? config.getString("typeImport") : null;
			configMap.put(type, new ModelTypeConfig(type, aClass, aImport));
		}
		return configMap;
	}
}
