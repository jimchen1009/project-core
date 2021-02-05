package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.effect.EffectParamKey;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class SkillDamageHandler_CritMore extends SkillDamageHandler {

	@Override
	protected void execute0(BattleContext battleContext, SkillContext skillContext, SkillDamage damageOutput) {
		ISkillEffectContext effectContext = skillContext.getSkillEffectContext();
		IConditionContext conditionContext = skillContext.getConditionContext();

		boolean isCrit = effectContext.getDecideCrit().finalDecideAndGet();
		BattleUnit requestUnit = skillContext.getRequestUnit();
		BattleUnit selectUnit = skillContext.getTargetUnit();
		if (!isCrit) {
			int critRand = battleContext.getBattle().getRandom().nextInt(1000);
			if (critRand < ((requestUnit.getAttribute(Attribute.critic, conditionContext)  - selectUnit.getAttribute(Attribute.anti_critic, conditionContext)))) {
				isCrit = true;
			}
		}
		int normalDamage = damageOutput.getNormalDamage();
		if (isCrit) {
			if (normalDamage > 0) {
				int damage = damageOutput.getDamage();
				int critDamage = requestUnit.getAttribute(Attribute.critic_dmg);
				critDamage += effectContext.getEffectValue(EffectParamKey.AddCritDamage, 0);
				damage += Math.max(0, normalDamage * critDamage / 1000);
				damageOutput.setDamage(damage);
			}
			else {
				isCrit = false;
			}
		}
		damageOutput.setCrit(isCrit);

		boolean isHit = effectContext.getDecideHit().finalDecideAndGet();
		boolean isMiss = effectContext.getDecideMiss().finalDecideAndGet();
		if (damageOutput.isCrit() || isHit) {
			// 暴击一定命中
			isMiss = false;
		}
		else if (!isMiss) {
			int missRandom = battleContext.getBattle().getRandom().nextInt(1000);
			int addHitRate = effectContext.getEffectValue(EffectParamKey.AddHitRate, 0);
			int hit = requestUnit.getAttribute(Attribute.hit, conditionContext);
			if (missRandom < selectUnit.getAttribute(Attribute.dodge, conditionContext) - addHitRate - hit) {
				isMiss = true;
			}
		}
		if (isMiss) {
			damageOutput.setDamage(0);
			damageOutput.setNormalDamage(0);
		}
		damageOutput.setHit(isHit);
		damageOutput.setMiss(isMiss);
	}
}
