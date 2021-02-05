package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionType;

import java.util.Collection;
import java.util.Collections;

public class SkillOccasionRequest extends SkillBaseRequest<SkillOccasionRequest> {

	private final SkillOccasion skillOccasion;
	private Collection<ConditionType> conditionTypes;

	public SkillOccasionRequest(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, SkillOccasion skillOccasion) {
		super(requestUnit, battleSkill, targetUnit);
		this.skillOccasion = skillOccasion;
	}

	public SkillOccasion getSkillOccasion() {
		return skillOccasion;
	}


	@Override
	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillEffectConfig effectConfig) {
		ConditionType conditionType = effectConfig.getConditionConfig().getConditionType();
		if (!supportCondition(conditionType)) {
			return Collections.emptyList();
		}
		return effectConfig.getEffectUnitConfigs(skillOccasion);
	}

	@Override
	protected SkillOccasionRequest getMyself() {
		return this;
	}

	public SkillOccasionRequest setConditionType(ConditionType conditionType) {
		return setConditionTypes(Collections.singleton(conditionType));
	}

	private boolean supportCondition(ConditionType conditionType){
		return conditionTypes == null || conditionTypes.isEmpty() || conditionTypes.contains(conditionType);
	}

	public SkillOccasionRequest setConditionTypes(Collection<ConditionType> conditionTypes) {
		this.conditionTypes = conditionTypes;
		return this;
	}
}
