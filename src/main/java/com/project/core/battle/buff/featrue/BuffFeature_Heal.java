package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.game.common.expression.ExprManager;
import com.game.common.expression.IExpression;
import com.game.common.util.SupplyHolder;
import com.project.core.battle.BattleContext;
import com.project.core.battle.skill.heal.SkillHealUnit;

public class BuffFeature_Heal extends BuffFeature {

	private IExpression expression;

	@Override
	protected void initParams(JSONObject params) {
		this.expression = ExprManager.getInstance().createExpression(params.getString("rate"));
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.Heal;
	}

	@Override
	public void onEndRoundHeal(BattleContext context, SupplyHolder<SkillHealUnit> holder) {
		int incRate = expression.calculateInt();
		if (incRate <= 0){
			return;
		}
		SkillHealUnit skillHealUnit = holder.computeIfAbsent();
		skillHealUnit.incHealHpRate(incRate);
	}
}
