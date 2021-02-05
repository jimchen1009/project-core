package com.project.core.battle;

import com.game.common.util.IEnumBase;

import java.io.Serializable;

public enum BattleUnitType implements IEnumBase, Serializable {
	Hero(1, true),
	Monster(2, true),
	;

	public final int id;
	private final boolean selectSupport;

	BattleUnitType(int id, boolean selectSupport) {
		this.id = id;
		this.selectSupport = selectSupport;
	}

	@Override
	public int getId() {
		return id;
	}

	public boolean isSelectSupport() {
		return selectSupport;
	}

	public long genObjectId(Battle battle){
		return battle.genObjectId();
	}
}
