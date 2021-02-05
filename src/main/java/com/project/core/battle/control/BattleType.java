package com.project.core.battle.control;

import com.game.common.util.IEnumBase;

public enum BattleType implements IEnumBase {
	PVE(1),
	;

	private final int id;

	BattleType(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return 0;
	}
}
