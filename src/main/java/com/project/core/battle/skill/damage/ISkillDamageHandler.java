package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleContext;
import com.project.core.battle.skill.SkillContext;

public interface ISkillDamageHandler {

	void execute(BattleContext battleContext, SkillContext castContext, SkillDamage skillDamage);
}
