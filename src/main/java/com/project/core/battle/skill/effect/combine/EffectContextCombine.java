package com.project.core.battle.skill.effect.combine;

import com.game.common.util.IEnumBase;
import com.project.core.battle.skill.effect.SkillEffectContext;

public enum EffectContextCombine implements IEnumBase, IEffectContextCombineHandler {
	ActiveSkill(new EffectContextCombineHandler_ActiveSkill()),
	;

	private final IEffectContextCombineHandler handler;

	EffectContextCombine(IEffectContextCombineHandler handler) {
		this.handler = handler;
	}

	@Override
	public int getId() {
		return ordinal();
	}


	@Override
	public SkillEffectContext execute(SkillEffectContext sourceContext, SkillEffectContext... fromContextList) {
		return handler.execute(sourceContext, fromContextList);
	}
}
