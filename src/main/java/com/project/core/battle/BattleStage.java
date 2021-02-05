package com.project.core.battle;

import java.util.Objects;

public class BattleStage{

	private static final int MAX_ID = 1000;

	public static BattleStage None = new BattleStage(0, "无");

	public static BattleStage BattleBegin = new BattleStage(MAX_ID, "开始战斗");

	public static BattleStage RoundBegin = new BattleStage(2 * MAX_ID, "开始回合");

	public static BattleStage RoundRunTeam = new BattleStage(4 * MAX_ID, "执行回合");

	public static BattleStage RoundEnd = new BattleStage(6 * MAX_ID, "结束回合");

	public static BattleStage BattleEnd = new BattleStage(7 * MAX_ID, "结束战斗");

	public static BattleStage BattleFinal = new BattleStage(8 * MAX_ID, "终止战斗");

	private final int id;
	private final String comment;

	private BattleStage(int id, String comment) {
		this.id = id;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}

	public BattleStage crateChild(int id){
		if (id >= MAX_ID){
			throw new UnsupportedOperationException(String.valueOf(id));
		}
		return new BattleStage(id, comment + id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BattleStage that = (BattleStage) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", comment='" + comment + '\'' +
				'}';
	}
}
