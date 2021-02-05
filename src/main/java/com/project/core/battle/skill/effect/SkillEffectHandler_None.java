package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillEffectHandler_None extends SkillEffectHandler {

	private static final Logger logger = LoggerFactory.getLogger(SkillEffectHandler_None.class);

	private String param;

	@Override
	protected void initParam(String param) {
		this.param = param;
	}

	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		return false;
	}
}
