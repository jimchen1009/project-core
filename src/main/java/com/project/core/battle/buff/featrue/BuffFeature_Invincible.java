package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.project.core.battle.BattleContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.damage.DamageModifier;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class BuffFeature_Invincible extends BuffFeature {


	@Override
	protected void initParams(JSONObject params) {
	}


	@Override
	public void beforeCastSkillRequest(BattleContext context, SkillContext skillContext) {
		skillContext.getSkillEffectContext().getDecideNeverHurt().decideValue(true);
	}

	@Override
	public void adjustFinalDamage(BattleContext context, ISkillEffectContext effectContext, DamageModifier modifier) {
		modifier.setDamage(0);
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.Invincible;
	}
}
