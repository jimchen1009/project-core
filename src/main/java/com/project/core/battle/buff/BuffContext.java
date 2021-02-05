package com.project.core.battle.buff;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.type.IBuffStrategy;

import java.util.Objects;

public class BuffContext {

	private final BattleUnit battleUnit;
	private final Buff buff;
	private final boolean isDoAction;

	public BuffContext(Buff buff, boolean isDoAction) {
		this(Objects.requireNonNull(buff.getHostUnit()), buff, isDoAction);
	}

	public BuffContext(BattleUnit battleUnit, Buff buff, boolean isDoAction) {
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

	public boolean isDoAction() {
		return isDoAction;
	}

	public BuffContainer.Container getTypeContainer(){
		BuffContainer buffContainer = battleUnit.getBuffContainer();
		return buffContainer.getTypeContainer(buff.getType());
	}

	public BuffContainer getBuffContainer(){
		return battleUnit.getBuffContainer();
	}

	public IBuffStrategy getBuffStrategy(){
		return buff.getTypeConfig().getStrategy();
	}

	public BuffContext changeBuff(Buff buff){
		return new BuffContext(battleUnit, buff, isDoAction);
	}
}
