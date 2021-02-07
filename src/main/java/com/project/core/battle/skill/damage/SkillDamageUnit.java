package com.project.core.battle.skill.damage;

import com.game.common.util.DecideBool;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.attribute.BaseAttributes;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.result.ActorActionDamage;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class SkillDamageUnit {

	protected final BattleUnit attackUnit;
	protected final BattleUnit damageUnit;
	protected final DamageType type;
	protected final boolean isDeadBefore;
	protected final BaseAttributes beforeAttributes;
	protected int damage;
	private boolean isCrit;
	private boolean isMiss;
	protected int beforeDamage;
	private DecideBool damageBool;

	public SkillDamageUnit(BattleUnit attackUnit, BattleUnit damageUnit, DamageType type) {
		this.attackUnit = attackUnit;
		this.damageUnit = damageUnit;
		this.type = type;
		this.isDeadBefore = damageUnit.isDead();
		this.beforeAttributes = damageUnit.getAttributes().copy();
		this.damage = 0;
		this.isCrit = false;
		this.isMiss = false;
		this.damageBool = new DecideBool(false);
	}

	public BattleUnit getAttackUnit() {
		return attackUnit;
	}

	public BattleUnit getDamageUnit() {
		return damageUnit;
	}

	public boolean isDamageType(DamageType damageType) {
		return type.equals(damageType);
	}

	public int getDamage() {
		return damage;
	}

	public SkillDamageUnit setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	public SkillDamageUnit incDamage(int inc) {
		this.damage += inc;
		return this;
	}

	public boolean isCrit() {
		return isCrit;
	}

	public SkillDamageUnit setCrit(boolean crit) {
		isCrit = crit;
		return this;
	}

	public boolean hasDoDamage() {
		return damageBool.getCurrentValue();
	}

	public SkillDamageUnit setMiss(boolean miss) {
		isMiss = miss;
		return this;
	}

	public final void doDamage(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		if (damageBool.hasDecidedValue()) {
			return;
		}
		beforeDamage = damage;
		if (isMiss || damage <= 0){
			damageBool.decideValue(false);
		}
		else {
			beforeDoDamage(battleContext, conditionContext, effectContext);
			boolean success = startDoDamage(battleContext, conditionContext, effectContext);
			damageBool.decideValue(success);
		}

		int afterHp = damageUnit.getAttribute(Attribute.hp);
		if (afterHp <= 0) {
			damageUnit.setDead();
		}
		battleContext.addBattleAction(new ActorActionDamage(attackUnit, type, damageUnit, damage));
	}

	protected void beforeDoDamage(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){
		DamageModifier modifier = new DamageModifier(type, damage);
		BuffUtil.foreachBuffFeature(damageUnit, feature  -> feature.adjustFinalDamage(battleContext, effectContext, modifier), conditionContext);
		damage = modifier.getDamage();
	}

	protected boolean startDoDamage(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){
		if (damage <= 0){
			return false;
		}
		int beforeHp = beforeAttributes.get(Attribute.hp);
		if (damage >= beforeHp){

		}
		damageUnit.setAttribute(Attribute.hp, Math.max(beforeHp - damage, 0));
		return true;
	}

	public void onDamageSuccess(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){
		if (!damageBool.getCurrentValue()) {
			return;
		}
		doDamageSuccess(battleContext, conditionContext, effectContext);
	}

	protected void doDamageSuccess(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){
		BuffUtil.foreachBuffFeature(damageUnit, feature -> feature.onBeDamage(battleContext, this));
	}
}
