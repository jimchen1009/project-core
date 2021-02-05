package com.project.core.battle.skill.heal;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.SkillHandler;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class SkillHealUnit_Normal extends SkillHealUnit {

	public SkillHealUnit_Normal(BattleUnit requestUnit, BattleUnit healUnit, HealType type) {
		super(requestUnit, healUnit, type);
	}

	@Override
	protected void doHealSuccess(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		super.doHealSuccess(battleContext, conditionContext, effectContext);
		SkillHandler.doConditionAllSkill(battleContext, getHealUnit(), getRequestUnit(), ConditionType.Heal);
		SkillHandler.doConditionAllSkill(battleContext, getHealUnit(), getRequestUnit(), ConditionType.MyHpRateAbove);
	}
}
