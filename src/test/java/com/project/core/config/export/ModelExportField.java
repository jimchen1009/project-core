package com.project.core.config.export;

public class ModelExportField {

	private final ModelFieldConfig fieldConfig;
	private final ModelTypeConfig typeConfig;


	public ModelExportField(ModelFieldConfig fieldConfig, ModelTypeConfig typeConfig) {
		this.fieldConfig = fieldConfig;
		this.typeConfig = typeConfig;
	}

	public ModelFieldConfig getFieldConfig() {
		return fieldConfig;
	}

	public ModelTypeConfig getTypeConfig() {
		return typeConfig;
	}
}
