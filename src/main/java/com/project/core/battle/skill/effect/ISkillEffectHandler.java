package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public interface ISkillEffectHandler {

	SkillEffectType getEffectType();

	int getSkillId();

	/**
	 * 执行效果
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param conditionContext
	 * @param effectContext
	 * @return
	 */
	boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext);

	/**
	 * 所有效果执行完毕之后再执行
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param conditionContext
	 * @param effectContext
	 */
	void onSkillSuccess(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext);
}
