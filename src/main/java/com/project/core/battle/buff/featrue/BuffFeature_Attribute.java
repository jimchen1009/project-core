package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.game.common.expression.ExprManager;
import com.game.common.expression.ExprParams;
import com.game.common.expression.IExpression;
import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.attribute.AttributeType;
import com.project.core.battle.result.ActorActionAttribute;

public class BuffFeature_Attribute extends BuffFeature {

	private Attribute attribute;
	private IExpression valueExpression;
	private IExpression rateExpression;

	private Integer addInt;

	@Override
	protected void initParams(JSONObject params) {
		this.attribute = IEnumBase.findOne(Attribute.values(), params.getString("attr"));
		this.valueExpression = ExprManager.getInstance().createExpression(params.getString("value"));
		this.rateExpression = ExprManager.getInstance().createExpression(params.getString("rate"));
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.Attribute;
	}

	@Override
	public void onFeatureAdd(BattleContext context) {
		if (attribute == null || addInt != null || attribute.getType().equals(AttributeType.Defence)) {
			return;
		}
		BattleUnit battleUnit = buff.getHostUnit();
		int baseValue = battleUnit.getAttribute(attribute);
		ExprParams params = new ExprParams();
		params.put("round", buff.getRemainRound());
		battleUnit.attributeParams(params);
		addInt = valueExpression.calculateInt(params);
		int rate = rateExpression.calculateInt(params);
		addInt += (int) ((long) baseValue * rate / 1000);
		int attributeValue = battleUnit.getAttribute(attribute);
		battleUnit.setAttribute(attribute, attributeValue + addInt);
		context.addBattleAction(new ActorActionAttribute(null, battleUnit, attribute));
	}

	@Override
	public void onFeatureRemove(BattleContext context) {
		if (attribute == null || addInt == null) {
			return;
		}
		BattleUnit battleUnit = buff.getHostUnit();
		int attributeValue = battleUnit.getAttribute(attribute);
		battleUnit.setAttribute(attribute, attributeValue - addInt);
		context.addBattleAction(new ActorActionAttribute(null, battleUnit, attribute));
		addInt = null;
	}
}
