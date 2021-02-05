package com.project.core.battle.condition;

import com.project.core.battle.IBattleContext;
import com.project.core.battle.skill.IConditionContextLink;
import com.project.core.battle.skill.effect.ext.SkillBattleUnit;

public interface IConditionContext extends IBattleContext, IConditionContextLink {

	SkillBattleUnit getSkillBattleUnit(long id);

	boolean castSkillMiss();
}
