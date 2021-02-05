package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleContext;
import com.project.core.battle.skill.SkillContext;

public enum SkillDamageRound implements ISkillDamageHandler {
	Damage(new SkillDamageHandler_Damage()),
	CritMore(new SkillDamageHandler_CritMore()),
	Adjust(new SkillDamageHandler_Adjust()),
	Final(new SkillDamageHandler_Final()),
	;

	private final ISkillDamageHandler damageHandler;

	SkillDamageRound(ISkillDamageHandler damageHandler) {
		this.damageHandler = damageHandler;
	}

	@Override
	public void execute(BattleContext battleContext, SkillContext castContext, SkillDamage skillDamage) {
		damageHandler.execute(battleContext, castContext, skillDamage);
	}
}
