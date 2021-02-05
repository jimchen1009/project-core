package com.project.core.battle.result;

import com.game.common.util.IEnumBase;

public enum ActorType implements IEnumBase {
	Skill(1),
	Limit(2),
	Buff(3),
	EnemyUnit(4),
	;

	private final int id;

	ActorType(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}
}
