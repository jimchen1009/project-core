package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionType;

import java.util.Collection;
import java.util.Collections;

public class SkillConditionRequest extends SkillBaseRequest<SkillConditionRequest> {

	private final ConditionType conditionType;
	private SkillOccasion skillOccasion;

	public SkillConditionRequest(BattleUnit battleUnit, BattleSkill battleSkill, BattleUnit targetUnit, ConditionType conditionType) {
		super(battleUnit, battleSkill, targetUnit);
		this.conditionType = conditionType;
	}

	@Override
	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillEffectConfig effectConfig) {
		ConditionType conditionType = effectConfig.getConditionConfig().getConditionType();
		if (!conditionType.equals(this.conditionType)) {
			return Collections.emptyList();
		}
		return skillOccasion == null ? effectConfig.getEffectUnitConfigs() : effectConfig.getEffectUnitConfigs(skillOccasion);
	}

	@Override
	protected SkillConditionRequest getMyself() {
		return this;
	}

	public SkillConditionRequest setSkillOccasion(SkillOccasion skillOccasion) {
		this.skillOccasion = skillOccasion;
		return getMyself();
	}
}
