package com.project.core.battle.skill.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public abstract class SkillCondition implements ISkillCondition {

	@Override
	public boolean isSkillLimit(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return isSkillLimit0(context, conditionContext, requestUnit, battleSkill, targetUnit);
	}

	protected abstract boolean isSkillLimit0(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit);
}
