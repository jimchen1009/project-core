package com.project.core.battle.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;

public class ConditionHandler_TargetHpRateBelow extends ConditionHandler {

	private int hpRate;

	@Override
	public void initParam(String param) {
		this.hpRate = Integer.parseInt(param);
	}

	@Override
	public boolean isSatisfy(BattleContext context, BattleUnit requestUnit, BattleUnit targetUnit, IConditionContext conditionContext) {
		if (!conditionContext.castHitTargetUnit() || conditionContext.castSkillMiss()){
			return false;
		}
		return targetUnit.getAttributes().getHpRate() <= hpRate;
	}
}
