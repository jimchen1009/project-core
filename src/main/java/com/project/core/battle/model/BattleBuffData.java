package com.project.core.battle.model;

public class BattleBuffData implements IBattleXXXData<BattleBuffData> {

	private final int buffId;
	private int round;

	public BattleBuffData(int buffId, int round) {
		this.buffId = buffId;
		this.round = round;
	}

	public int getBuffId() {
		return buffId;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	@Override
	public BattleBuffData deepCopy() {
		return new BattleBuffData(buffId, round);
	}
}
