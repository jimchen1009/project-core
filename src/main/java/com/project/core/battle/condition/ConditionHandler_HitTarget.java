package com.project.core.battle.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;

public class ConditionHandler_HitTarget extends ConditionHandler {

	@Override
	public boolean isSatisfy(BattleContext context, BattleUnit requestUnit, BattleUnit targetUnit, IConditionContext conditionContext) {
		return conditionContext.castHitTargetUnit();
	}
}
