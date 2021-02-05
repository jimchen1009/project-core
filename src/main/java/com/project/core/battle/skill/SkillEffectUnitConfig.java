package com.project.core.battle.skill;

import com.project.core.battle.skill.effect.ISkillEffectHandler;
import com.project.core.battle.skill.effect.SkillEffectType;

public class SkillEffectUnitConfig {

	private final SkillEffectTypeConfig typeConfig;
	private final ISkillEffectHandler effectHandler;

	public SkillEffectUnitConfig(SkillEffectTypeConfig typeConfig, ISkillEffectHandler effectHandler) {
		this.typeConfig = typeConfig;
		this.effectHandler = effectHandler;
	}

	public SkillEffectTypeConfig getTypeConfig() {
		return typeConfig;
	}

	public SkillEffectType getEffectType() {
		return typeConfig.getEffectType();
	}

	@SuppressWarnings("unchecked")
	public <T extends ISkillEffectHandler> T getEffectHandler() {
		return (T)effectHandler;
	}

	public SkillOccasion getOccasionType() {
		return typeConfig.getSkillOccasion();
	}
}
