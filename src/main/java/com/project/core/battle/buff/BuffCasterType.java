package com.project.core.battle.buff;

import com.game.common.util.IEnumBase;

public enum BuffCasterType implements IEnumBase {
	UnKnown(0),
	Own(1),
	TeamMate(2),
	Enemy(3),
	Neutral(4),
	;

	private final int id;

	BuffCasterType(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}
}
