package com.project.core.battle.skill.damage;

import com.game.common.util.IncInteger;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.SkillEffectRequest;
import com.project.core.battle.skill.SkillEffectUtil;
import com.project.core.battle.skill.effect.EffectParamKey;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.effect.SkillEffectType;

public class SkillDamageHandler_Adjust extends SkillDamageHandler {

	@Override
	protected void execute0(BattleContext battleContext, SkillContext skillContext, SkillDamage damageOutput) {
		int damage = damageOutput.getDamage();
		if (damage <= 0){
			return;
		}
		BattleUnit requestUnit = skillContext.getRequestUnit();
		BattleUnit targetUnit = skillContext.getTargetUnit();
		ISkillEffectContext effectContext = skillContext.getSkillEffectContext();
		IConditionContext conditionContext = skillContext.getConditionContext();

		IncInteger addAtkDamage = new IncInteger();
		BuffUtil.foreachBuffFeature(requestUnit, feature -> feature.getAttackAddDamage(battleContext, effectContext, addAtkDamage));
		addAtkDamage.increaseValue(requestUnit.getAttribute(Attribute.atk_damage_add, conditionContext));

		SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillEffectType.AddAttackDamageRate, conditionContext, effectContext);
		addAtkDamage.increaseValue(effectContext.getEffectValue(EffectParamKey.AddAttackDamageRate, 0));

		BattleTeamUnit teamUnit = targetUnit.getTeamUnit();
		addAtkDamage.increaseValue(teamUnit.getData().getAddDamageRate());

		int atkDamageAdd = addAtkDamage.getValue();

		SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, targetUnit, requestUnit, SkillEffectType.DecBeAttackDamageRate, conditionContext, effectContext);

		int decDamageRate = effectContext.getEffectValue(EffectParamKey.DecBeAttackDamageRate, 0);

		int defDamageDec = targetUnit.getAttribute(Attribute.def_damage_dec, conditionContext);
		int damageAdd = atkDamageAdd - defDamageDec - decDamageRate;
		if (damageAdd != 0) {
			damage += damage * damageAdd / 1000;
			if (damage <= 0) {
				damage = 1;
			}
		}

		// 最大伤害限制
		Integer maxDamage = effectContext.getEffectValue(EffectParamKey.MaxBeAttackDamagePercent, 0);
		if (maxDamage > 0) {
			damage = Math.min(damage, maxDamage);
		}

		damageOutput.setDamage(damage);
	}
}
