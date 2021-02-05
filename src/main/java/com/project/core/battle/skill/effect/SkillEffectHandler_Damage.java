package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public class SkillEffectHandler_Damage extends SkillEffectHandler {

	private int damageRate;

	@Override
	protected void initParam(String param) {
		this.damageRate = Integer.parseInt(param);
	}

	public int getDamageRate() {
		return damageRate;
	}


	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		return false;
	}
}
