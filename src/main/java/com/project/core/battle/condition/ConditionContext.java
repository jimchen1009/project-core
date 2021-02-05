package com.project.core.battle.condition;

import com.project.core.battle.skill.effect.ext.SkillBattleUnit;

public class ConditionContext implements IConditionContext {

	public static final IConditionContext DEFAULT = new ConditionContext(null);

	private final IConditionContext conditionContext;

	public ConditionContext() {
		this(null);
	}

	public ConditionContext(IConditionContext conditionContext) {
		this.conditionContext = conditionContext;
	}

	@Override
	public SkillBattleUnit getSkillBattleUnit(long id) {
		return conditionContext == null ? null : conditionContext.getSkillBattleUnit(id);
	}

	@Override
	public boolean castSkillMiss() {
		return conditionContext != null && conditionContext.castSkillMiss();
	}

	@Override
	public boolean castHitTargetUnit() {
		return conditionContext != null && conditionContext.castHitTargetUnit();
	}
}
