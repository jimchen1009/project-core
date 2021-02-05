package com.project.core.battle.control;

import com.game.common.util.IEnumBase;

/**
 *
 */
public enum BattleInterrupt implements IEnumBase {
	ABORT(1),
	LOGOUT(2),
	;

	private final int id;

	BattleInterrupt(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}
}
