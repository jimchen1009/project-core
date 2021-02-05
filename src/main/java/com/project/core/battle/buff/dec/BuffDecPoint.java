package com.project.core.battle.buff.dec;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContext;

public enum BuffDecPoint implements IBuffDecPointHandler, IEnumBase {

	EndRound(0, BuffDecPointHandler.TRUE),
	MyEndRound(1, BuffDecPointHandler.TRUE),
	EnemyEndRound(2, BuffDecPointHandler.TRUE),
	MyEndNextOperate(3, BuffDecPointHandler.FALSE),
	NoDec(4, BuffDecPointHandler.FALSE),
	BeAttack(5, BuffDecPointHandler.TRUE),
	AfterMyOperation(6, BuffDecPointHandler.TRUE),
	;

	private final int id;
	private final IBuffDecPointHandler handler;

	BuffDecPoint(int id, IBuffDecPointHandler handler) {
		this.id = id;
		this.handler = handler;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean canDecBuff(BattleContext battleContext, Buff buff) {
		return handler != null && handler.canDecBuff(battleContext, buff);
	}
}
