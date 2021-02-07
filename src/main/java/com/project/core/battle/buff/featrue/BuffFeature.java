package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.game.common.util.SupplyHolder;
import com.game.common.util.IncInteger;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.operate.OperateContext;
import com.project.core.battle.skill.SkillCastContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.damage.DamageModifier;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.heal.SkillHealUnit;

import java.util.List;

public abstract class BuffFeature implements IBuffFeature{

	protected Buff buff;

	protected void initialize(Buff buff, JSONObject params){
		this.buff = buff;
		this.initParams(params);
	}

	protected abstract void initParams(JSONObject params);

	@Override
	public Buff getBuff() {
		return buff;
	}

	@Override
	public boolean isFeature(BuffFeatureType featureType) {
		return featureType.equals(getFeatureType());
	}

	@Override
	public boolean isLimitAddBuff(BattleContext battleContext, BuffContext buffContext) {
		return false;
	}

	@Override
	public boolean isSkillLimit(BattleContext battleContext, IConditionContext conditionContext) {
		return false;
	}

	@Override
	public boolean canCastSkill(BattleContext battleContext, IConditionContext conditionContext) {
		return true;
	}

	@Override
	public void onBattleStartRound(BattleContext context) {
	}

	@Override
	public void onBattleEndRound(BattleContext context) {

	}

	@Override
	public void onIncBuffRound(BattleContext context) {

	}

	@Override
	public void onDecBuffRound(BattleContext context) {

	}

	@Override
	public boolean checkRemoveFeature(BattleContext context){
		return false;
	}

	@Override
	public void onFeatureAdd(BattleContext context) {

	}

	@Override
	public void onFeatureRemove(BattleContext context) {

	}

	@Override
	public void resetBattleOperate(BattleContext context, OperateContext operateContext) {

	}

	@Override
	public void castResetTargetUnit(BattleContext context, SkillCastContext castContext) {
	}

	@Override
	public boolean canIgnoreDefRate() {
		return false;
	}

	@Override
	public void beforeCastSkillRequest(BattleContext context, SkillContext skillContext) {

	}

	@Override
	public void beforeCastSkillTarget(BattleContext context, SkillContext skillContext) {

	}

	@Override
	public void getExtraAttribute(Attribute attribute, IncInteger incInteger) {

	}

	@Override
	public void getAttackAddDamage(BattleContext context, ISkillEffectContext effectContext, IncInteger addAtkDamage) {

	}

	@Override
	public void adjustFinalDamage(BattleContext context, ISkillEffectContext effectContext, DamageModifier modifier) {

	}

	@Override
	public void getProtectBattleUnit(BattleContext context, ISkillEffectContext effectContext, List<BattleUnit> battleUnitList) {
	}

	@Override
	public boolean canHeal() {
		return true;
	}


	@Override
	public void onBeDamage(BattleContext context, SkillDamageUnit damageUnit) {

	}

	@Override
	public void onEndRoundHeal(BattleContext context, SupplyHolder<SkillHealUnit> holder) {

	}

	@Override
	public void onEndRoundPoison(BattleContext context, SupplyHolder<SkillDamageUnit> holder) {

	}
}
