package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleContext;
import com.project.core.battle.skill.SkillContext;

public abstract class SkillDamageHandler implements ISkillDamageHandler {

	@Override
	public void execute(BattleContext battleContext, SkillContext skillContext, SkillDamage damageOutput) {
		execute0(battleContext, skillContext, damageOutput);
	}

	protected abstract void execute0(BattleContext battleContext, SkillContext skillContext, SkillDamage damageOutput);
}
