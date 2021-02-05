package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.IBattleContext;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.effect.ISkillEffectContext;

interface ISkillContext extends IBattleContext {

	BattleSkill getBattleSkill();

	BattleUnit getRequestUnit();

	BattleUnit getTargetUnit();
	
	boolean isCastSkill();

	ISkillEffectContext getSkillEffectContext();

	IConditionContext getConditionContext() ;
}
