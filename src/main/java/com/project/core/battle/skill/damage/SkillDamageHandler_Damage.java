package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.effect.EffectParamKey;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class SkillDamageHandler_Damage extends SkillDamageHandler {

	@Override
	protected void execute0(BattleContext battleContext, SkillContext skillContext, SkillDamage damageOutput) {
		ISkillEffectContext effectContext = skillContext.getSkillEffectContext();
		if (effectContext.getDecideNeverHurt().finalDecideAndGet()) {
			damageOutput.setDamage(0);
			damageOutput.setNormalDamage(0);
		}
		Integer addNormalDamageRate = effectContext.getEffectValue(EffectParamKey.AddNormalDamageRate, 0);
		int normalDamage = damageOutput.getNormalDamage();
		if (normalDamage > 0 && addNormalDamageRate > 0) {
			int addNormalDamage = normalDamage * addNormalDamageRate / 1000;
			damageOutput.setDamage(normalDamage + addNormalDamage);

			damageOutput.setNormalDamage(normalDamage + addNormalDamage);
		}
	}
}
