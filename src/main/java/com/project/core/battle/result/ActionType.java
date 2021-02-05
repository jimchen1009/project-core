package com.project.core.battle.result;

import com.game.common.util.IEnumBase;

public enum ActionType implements IEnumBase {
	Skill,
	SkillCD,
	Effect,
	BuffAdd,
	BuffLimit,
	BuffRemove,
	BuffChange,
	Attribute,
	Heal,
	Damage,
	;

	ActionType() {
	}

	@Override
	public int getId() {
		return ordinal();
	}
}
