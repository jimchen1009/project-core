package com.project.core.battle;

import com.game.common.util.IEnumBase;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BattleTeamType implements IEnumBase, Serializable {
	/**右边*/
	TeamA(1, Arrays.asList(1, 3, 5)),
	/**左边*/
	TeamB(2, Arrays.asList(2, 4, 6)),
	/**第三方*/
	Neutral(3, Collections.singletonList(0)),
	;

	/***
	 * 默认ID小的出手顺序优先
	 */
	public final int id;
	/**
	 * 这个ID需要对业务屏蔽, 只有组队内部使用
	 */
	private final List<Integer> teamUnitIndexes;

	BattleTeamType(int id, List<Integer> teamUnitIndexes) {
		this.id = id;
		this.teamUnitIndexes = teamUnitIndexes;
	}

	@Override
	public int getId() {
		return id;
	}

	public boolean checkTeamUnitIndex(int teamUnitIndex){
		return teamUnitIndexes.contains(teamUnitIndex);
	}

	public List<Integer> getTeamUnitIndexes() {
		return teamUnitIndexes;
	}

	public int firstTeamUnitIndex() {
		return teamUnitIndexes.get(0);
	}

	public static BattleTeamType getIndexTeamType(int teamUnitIndex){
		for (BattleTeamType teamType : BattleTeamType.values()) {
			if (teamType.getTeamUnitIndexes().contains(teamUnitIndex)) {
				return teamType;
			}
		}
		throw new UnsupportedOperationException(String.valueOf(teamUnitIndex));
	}
}
