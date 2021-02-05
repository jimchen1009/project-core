package com.project.core.battle.skill.effect.combine;

import com.project.core.battle.skill.effect.SkillEffectContext;

public class EffectContextCombineHandler_ActiveSkill extends EffectContextCombineHandler {

	@Override
	protected void executeOne(SkillEffectContext sourceContext, SkillEffectContext fromContext) {
		combineDecideBool(sourceContext, fromContext);
		combineSkillBattleUnit(sourceContext, fromContext);
		combineSkillHealUnit(sourceContext, fromContext);
	}
}
