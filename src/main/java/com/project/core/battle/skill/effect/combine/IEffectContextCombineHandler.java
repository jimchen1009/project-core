package com.project.core.battle.skill.effect.combine;

import com.project.core.battle.skill.effect.SkillEffectContext;

public interface IEffectContextCombineHandler {

	SkillEffectContext execute(SkillEffectContext sourceContext, SkillEffectContext... fromContextList);
}
