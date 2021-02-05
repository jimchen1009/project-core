package com.project.core.battle.model;

import com.project.core.battle.BattleTeamType;

import java.util.List;

public class BattleTeamData implements IBattleXXXData<BattleTeamData> {

	private final BattleTeamType teamType;
	private final List<BattleTeamUnitData> teamUnitDataList;

	public BattleTeamData(BattleTeamType teamType, List<BattleTeamUnitData> teamUnitDataList) {
		this.teamType = teamType;
		this.teamUnitDataList = teamUnitDataList;
	}

	public BattleTeamType getTeamType() {
		return teamType;
	}


	public List<BattleTeamUnitData> getTeamUnitDataList() {
		return teamUnitDataList;
	}

	@Override
	public BattleTeamData deepCopy() {
		BattleTeamData teamData = new BattleTeamData(teamType, BattleDataUtil.deepCopy(teamUnitDataList));
		return teamData;
	}
}
