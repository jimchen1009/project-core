package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class SkillUnitDamage_SkillUnit extends SkillDamageUnit {

	public SkillUnitDamage_SkillUnit(BattleUnit attackUnit, BattleUnit damageUnit, DamageType damageType) {
		super(attackUnit, damageUnit, damageType);
	}

	@Override
	protected void beforeDoDamage(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		super.beforeDoDamage(battleContext, conditionContext, effectContext);
		DamageModifier modifier = new DamageModifier(type, damage);
		BuffUtil.foreachBuffFeature(damageUnit, feature  -> feature.adjustFinalDamage(battleContext, effectContext, modifier), conditionContext);
		damage = modifier.getDamage();
	}
}
