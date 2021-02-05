package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.heal.HealType;
import com.project.core.battle.skill.heal.SkillHealUnit;

public class SkillEffectHandler_HealToHigherHpRate extends SkillEffectHandler {


	@Override
	protected void initParam(String param) {

	}

	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		if (!requestUnit.isAlive() || !targetUnit.isAlive()) {
			return false;
		}
		int requestHpRate = requestUnit.getHpRate();
		int targetHpRate = targetUnit.getHpRate();


		if (requestHpRate >= targetHpRate) {
			SkillHealUnit skillHealUnit = HealType.Skill.createUnitHeal(requestUnit, targetUnit);
			return skillHealUnit.incHealHpRate(requestHpRate - targetHpRate).doHeal(battleContext, conditionContext, effectContext);
		}
		else {
			SkillHealUnit skillHealUnit = HealType.Skill.createUnitHeal(targetUnit, requestUnit);
			return skillHealUnit.incHealHpRate(targetHpRate - requestHpRate).doHeal(battleContext, conditionContext, effectContext);
		}
	}
}
