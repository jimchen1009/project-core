package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.project.core.battle.BattleContext;
import com.project.core.battle.condition.IConditionContext;

public class BuffFeature_Dizzy extends BuffFeature {

	@Override
	protected void initParams(JSONObject params) {
	}

	@Override
	public boolean isSkillLimit(BattleContext battleContext, IConditionContext conditionContext) {
		return true;
	}

	@Override
	public boolean canCastSkill(BattleContext battleContext, IConditionContext conditionContext) {
		return false;
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.Dizzy;
	}
}
