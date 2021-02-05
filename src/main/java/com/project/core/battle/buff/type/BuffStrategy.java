package com.project.core.battle.buff.type;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.dec.BuffDecPoint;

public enum BuffStrategy implements IEnumBase, IBuffStrategy {

	Old(0, "保留旧", new BuffStrategy_Old()),
	New(1, "保留新", new BuffStrategy_New()),
	AddUnion(2, "同时存在,一起生效", new BuffStrategy_AddUnion()),
	LongRound(3, "保留剩余回合长", new BuffStrategy_Long()),
	AddBehind(4, "追加后面, 只有第一个生效", new BuffStrategy_AddBehind()),
	AddPriority(5, "追加后面, 只有第一个生效", new BuffStrategy_AddBehind()),
	;

	private final int id;
	private final IBuffStrategy strategy;

	BuffStrategy(int id, String comment, IBuffStrategy strategy) {
		this.id = id;
		this.strategy = strategy;
	}

	@Override
	public int getId() {
		return id;
	}


	@Override
	public boolean directAddBuff(BattleContext battleContext, BuffContext buffContext) {
		return strategy.directAddBuff(battleContext, buffContext);
	}

	@Override
	public boolean directRemoveBuff(BattleContext battleContext, BuffContext buffContext) {
		return strategy.directRemoveBuff(battleContext, buffContext);
	}

	@Override
	public void decBattleUnitBuffRound(BattleContext battleContext, BattleUnit battleUnit, BuffContainer.Container typeContainer, BuffDecPoint decPoint) {
		strategy.decBattleUnitBuffRound(battleContext, battleUnit, typeContainer, decPoint);
	}
}
