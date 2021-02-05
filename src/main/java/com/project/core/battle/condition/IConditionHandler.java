package com.project.core.battle.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;

public interface IConditionHandler {

	ConditionType getConditionType();

	boolean isSatisfy(BattleContext context, BattleUnit requestUnit, BattleUnit targetUnit, IConditionContext conditionContext);
}
