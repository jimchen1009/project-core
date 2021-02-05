package com.project.core.battle.buff.dec;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.Buff;

public interface IBuffDecPointHandler {

	boolean canDecBuff(BattleContext battleContext, Buff buff);
}
