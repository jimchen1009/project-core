package com.project.game.config.export;

public class ModelFieldConfig {

	public static final int Count = 4;

	private final int index;
	private final boolean primaryKey;
	private final String type;				//字段1
	private final String export;			//字段2
	private final String name;				//字段3
	private final String annotation;		//字段4

	private final String methodGet;
	private final String methodSet;

	public ModelFieldConfig(int index, boolean isPrimaryKey, String type, String export, String name, String annotation) {
		this.index = index;
		this.primaryKey = isPrimaryKey;
		this.type = type;
		this.export = export;
		this.name = name;
		this.annotation = annotation;
		this.methodGet = String.format("get%s", capitalizeName());
		this.methodSet = String.format("set%s", capitalizeName());
	}

	public int getIndex() {
		return index;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public String getType() {
		return type;
	}

	public String getExport() {
		return export;
	}

	public String getName() {
		return name;
	}

	public String getAnnotation() {
		return annotation;
	}

	public String getMethodGet() {
		return methodGet;
	}

	public String getMethodSet() {
		return methodSet;
	}

	private String capitalizeName(){
		return name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1) : "");
	}
}
