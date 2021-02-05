package com.project.core.battle.condition;

public interface IConditionConfig {

	ConditionType getConditionType();

	IConditionHandler getConditionHandler();

}
