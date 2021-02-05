package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.game.common.expression.ExprManager;
import com.game.common.expression.ExprParams;
import com.game.common.expression.IExpression;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.BuffHandler;
import com.project.core.battle.skill.damage.DamageModifier;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class BuffFeature_Shield extends BuffFeature {

	private IExpression expression;
	private int remainHp;

	@Override
	protected void initParams(JSONObject params) {
		this.expression = ExprManager.getInstance().createExpression(params.getString("value"));
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.Shield;
	}

	@Override
	public boolean checkRemoveFeature(BattleContext context) {
		return remainHp <= 0;
	}

	@Override
	public void onFeatureAdd(BattleContext context) {
		BattleUnit hostUnit = buff.getHostUnit();
		BattleUnit casterUnit = buff.getCasterUnit();

		ExprParams params = new ExprParams();
		if (hostUnit != null) {
			params.put("host", hostUnit.attributeParams(params));
		}
		if (casterUnit != null) {
			params.put("caster", casterUnit.attributeParams(params));
		}
		this.remainHp = Math.max(1, (int) Math.ceil(expression.calculateDouble(params)));
	}

	@Override
	public void adjustFinalDamage(BattleContext context, ISkillEffectContext effectContext, DamageModifier modifier) {
		if (remainHp <= 0){
			return;
		}
		int damage = modifier.getDamage();
		int decDamage = Math.min(damage, this.remainHp);
		modifier.addDamage(-decDamage);
		this.remainHp = this.remainHp - decDamage;
	}

	@Override
	public void onBeDamage(BattleContext context, SkillDamageUnit damageUnit) {
		BuffHandler.checkAndRemoveBuff(context, new BuffContext(buff, false));
	}
}
