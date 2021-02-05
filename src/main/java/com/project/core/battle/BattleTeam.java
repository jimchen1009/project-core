package com.project.core.battle;

import com.game.common.util.CommonUtil;
import com.project.core.battle.model.BattleTeamData;
import com.project.core.battle.model.BattleTeamUnitData;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BattleTeam extends BattleXXX<BattleTeamData> {

	private final List<BattleTeamUnit> teamUnitList;		//一方阵营有多个队伍

	public BattleTeam(Battle battle, BattleTeamData teamData) {
		super(teamData);
		List<BattleTeamUnit> teamUnitList = teamData.getTeamUnitDataList().stream()
				.sorted(Comparator.comparingInt(BattleTeamUnitData::getTeamUnitIndex))
				.map(teamUnitData -> new BattleTeamUnit(battle, teamData.getTeamType(), teamUnitData))
				.collect(Collectors.toList());
		this.teamUnitList = Collections.unmodifiableList(teamUnitList);
	}

	public List<BattleTeamUnit> getTeamUnitList() {
		return teamUnitList;
	}

	public List<BattleTeamUnit> getTeamUnitList(Predicate<BattleTeamUnit> predicate) {
		return teamUnitList.stream().filter(predicate).collect(Collectors.toList());
	}

	public BattleTeamUnit getIndexTeamUnit(int teamUnitIndex){
		return CommonUtil.findOneUtilOkay(teamUnitList, battleTeamUnit -> battleTeamUnit.getTeamUnitIndex() == teamUnitIndex);
	}

	public Collection<Long> getAllTeamUnitUserIds(){
		return teamUnitList.stream()
				.filter(teamUnit -> teamUnit.getTeamUnitUserId() > 0)
				.map(BattleTeamUnit::getTeamUnitUserId).collect(Collectors.toSet());
	}

	public Collection<Long> getAllBattleUnitUserIds(){
		Set<Long> allBattleUnitUserIds = new HashSet<>();
		BattleUtil.foreachBattleUnit(this, battleUnit -> battleUnit.getUserId() > 0, battleUnit -> allBattleUnitUserIds.add(battleUnit.getUserId()));
		return allBattleUnitUserIds;
	}

	public boolean isAllDead(){
		for (BattleTeamUnit battleTeamUnit : teamUnitList) {
			if (!battleTeamUnit.getBattleUnitList(BattleUnit::isAlive).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
