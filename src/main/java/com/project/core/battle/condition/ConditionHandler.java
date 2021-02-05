package com.project.core.battle.condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConditionHandler implements IConditionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ConditionHandler.class);

	private ConditionType conditionType;

	protected void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	@Override
	public ConditionType getConditionType() {
		return conditionType;
	}

	protected void initParam(String param){

	}
}
