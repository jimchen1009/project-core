package com.project.core.battle.buff;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.type.BuffStrategy;
import com.project.core.battle.result.ActionType;
import com.project.core.battle.result.ActorActionBuff;

public class BuffContext {

	private final BattleUnit requestUnit;
	private final BattleUnit battleUnit;
	private final Buff buff;
	private final boolean isDoAction;

	public BuffContext(Buff buff, boolean isDoAction) {
		this(buff.getHostUnit(), buff.getHostUnit(), buff, isDoAction);
	}

	public BuffContext(BattleUnit requestUnit, BattleUnit battleUnit, Buff buff, boolean isDoAction) {
		this.requestUnit = requestUnit;
		this.battleUnit = battleUnit;
		this.buff = buff;
		this.isDoAction = isDoAction;
	}

	public BattleUnit getBattleUnit() {
		return battleUnit;
	}

	public Buff getBuff() {
		return buff;
	}

	public BuffContainer.Container getTypeContainer(){
		BuffContainer buffContainer = battleUnit.getBuffContainer();
		return buffContainer.getTypeContainer(buff.getType());
	}

	public BuffContainer getBuffContainer(){
		return battleUnit.getBuffContainer();
	}

	public BuffStrategy getBuffStrategy(){
		return buff.getTypeConfig().getStrategy();
	}

	public BuffContext changeBuff(Buff buff){
		return new BuffContext(requestUnit, battleUnit, buff, isDoAction);
	}

	public void addBattleAction(BattleContext battleContext, ActionType actionType){
		battleContext.addBattleAction(new ActorActionBuff(requestUnit, battleUnit, actionType, buff, isDoAction));
	}
}
