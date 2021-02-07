package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.game.common.expression.ExprManager;
import com.game.common.expression.IExpression;
import com.game.common.util.SupplyHolder;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleMath;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.skill.damage.SkillDamageUnit;

public class BuffFeature_Poison extends BuffFeature {

	private IExpression expression;

	@Override
	protected void initParams(JSONObject params) {
		this.expression = ExprManager.getInstance().createExpression(params.getString("rate"));
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.Poison;
	}

	@Override
	public void onEndRoundPoison(BattleContext context, SupplyHolder<SkillDamageUnit> holder) {
		int incRate = expression.calculateInt();
		if (incRate <= 0){
			return;
		}
		SkillDamageUnit skillDamageUnit = holder.computeIfAbsent();
		int decDamage = BattleMath.rate1000(skillDamageUnit.getDamageUnit().getAttribute(Attribute.thp), incRate);
		skillDamageUnit.incDamage(decDamage);
	}
}
