package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.heal.HealType;
import com.project.core.battle.skill.heal.SkillHealUnit;

public class SkillEffectHandler_Heal extends SkillEffectHandler {

	private int percent;
	private int atk_factor;
	private int hp_factor;

	@Override
	protected void initParam(String param) {
		String[] strings = param.split("#");
		this.percent = Integer.parseInt(strings[0]);
		this.atk_factor = strings.length > 1 ? Integer.parseInt(strings[1]) : 0;
		this.hp_factor = strings.length > 2 ? Integer.parseInt(strings[2]) : 0;
	}

	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		int extraHealValue = 0;
		if (atk_factor > 0) {
			extraHealValue += requestUnit.getAttribute(Attribute.atk) * atk_factor / 1000;
		}
		if (hp_factor > 0) {
			extraHealValue += (int) ((long) requestUnit.getAttribute(Attribute.thp) * hp_factor / 1000);
		}

		SkillHealUnit skillHealUnit = HealType.Skill.createUnitHeal(requestUnit, targetUnit);
		skillHealUnit.incHealHp(extraHealValue).incHealHpRate(percent).doHeal(battleContext, conditionContext, effectContext);
		return true;
	}
}
