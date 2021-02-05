package com.project.core.battle.buff.dec;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContext;

public class BuffDecPointHandler implements IBuffDecPointHandler {

	public static final BuffDecPointHandler TRUE = new BuffDecPointHandler(true);
	public static final BuffDecPointHandler FALSE = new BuffDecPointHandler(true);

	private final boolean canDec;

	public BuffDecPointHandler(boolean canDec) {
		this.canDec = canDec;
	}

	@Override
	public boolean canDecBuff(BattleContext battleContext, Buff buff) {
		return canDec;
	}
}
