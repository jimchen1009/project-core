package com.project.core.battle.skill.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public interface ISkillCondition {

	/**
	 *
	 * @param context
	 * @param conditionContext
	 * @param battleSkill
	 * @return
	 */
	boolean isSkillLimit(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit);
}
