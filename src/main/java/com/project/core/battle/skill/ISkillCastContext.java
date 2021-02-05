package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;

public interface ISkillCastContext {

	BattleSkill getBattleSkill();

	void changeTargetBattleUnit(BattleUnit battleUnit);
}
