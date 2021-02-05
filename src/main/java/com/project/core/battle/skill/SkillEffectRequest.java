package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.skill.effect.SkillEffectType;

import java.util.Collection;
import java.util.Collections;

public class SkillEffectRequest extends SkillBaseRequest<SkillEffectRequest> {

	private final SkillEffectType effectType;
	private Collection<ConditionType> conditionTypes;

	public SkillEffectRequest(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, SkillEffectType effectType) {
		super(requestUnit, battleSkill, targetUnit);
		this.effectType = effectType;
	}

	public SkillEffectType getEffectType() {
		return effectType;
	}

	@Override
	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillEffectConfig effectConfig) {
		if (conditionTypes == null || conditionTypes.contains(effectConfig.getSkillConditionType())) {
			return effectConfig.getEffectUnitConfigs(effectType);
		}
		else {
			return Collections.emptyList();
		}

	}

	@Override
	protected SkillEffectRequest getMyself() {
		return this;
	}


	public SkillEffectRequest setConditionTypes(Collection<ConditionType> conditionTypes) {
		this.conditionTypes = conditionTypes;
		return this;
	}
}

