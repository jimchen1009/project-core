package com.project.core.battle.buff.type;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.dec.BuffDecPoint;

import java.util.Collection;

public interface IBuffStrategy {

	boolean directAddBuff(BattleContext battleContext, BuffContext buffContext);

	boolean directRemoveBuff(BattleContext battleContext, BuffContext buffContext);

	void decBattleUnitBuffRound(BattleContext battleContext, BattleUnit battleUnit, BuffContainer.Container typeContainer, BuffDecPoint decPoint);

	void changeBattleUnitBuffRound(BattleContext battleContext, Collection<BuffContext> buffContexts, int changeRound);
}
