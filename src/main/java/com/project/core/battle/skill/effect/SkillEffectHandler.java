package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public abstract class SkillEffectHandler implements ISkillEffectHandler {

	private int skillId;
	private SkillEffectType effectType;

	public SkillEffectHandler() {
	}

	protected void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	protected void setEffectType(SkillEffectType effectType) {
		this.effectType = effectType;
	}

	@Override
	public int getSkillId() {
		return skillId;
	}

	@Override
	public SkillEffectType getEffectType() {
		return effectType;
	}

	protected abstract void initParam(String param);

	@Override
	public void onSkillSuccess(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {

	}
}
