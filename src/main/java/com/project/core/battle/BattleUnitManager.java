package com.project.core.battle;

import com.game.common.util.CommonUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BattleUnitManager implements Serializable {

	private final List<BattleTeam> battleTeamList;
	private final Map<Long, BattleUnit> idBattleUnitMap;
	private final Map<Long, BattleTeamUnit> userTeamUnitMap;

	public BattleUnitManager(List<BattleTeam> battleTeamList) {
		Map<Long, BattleUnit> battleUnitMap = new HashMap<>();
		Map<Long, BattleTeamUnit> userTeamUnitMap = new HashMap<>();
		//按照ID来排序出手顺序~
		battleTeamList = battleTeamList.stream().sorted(Comparator.comparingInt(battleTeam -> battleTeam.getData().getTeamType().getId())).collect(Collectors.toList());
		for (BattleTeam battleTeam : battleTeamList) {
			for (BattleTeamUnit battleTeamUnit : battleTeam.getTeamUnitList()) {
				for (BattleUnit battleUnit : battleTeamUnit.getBattleUnitList()) {
					battleUnitMap.put(battleUnit.getId(), battleUnit);
				}
				if (battleTeamUnit.getTeamUnitUserId() <= 0) {
					continue;
				}
				if (userTeamUnitMap.containsKey(battleTeamUnit.getTeamUnitUserId())) {
					throw new UnsupportedOperationException();
				}
				userTeamUnitMap.put(battleTeamUnit.getTeamUnitUserId(), battleTeamUnit);
			}
		}
		this.battleTeamList = Collections.unmodifiableList(battleTeamList);
		this.idBattleUnitMap = battleUnitMap;
		this.userTeamUnitMap = userTeamUnitMap;
	}

	public BattleTeam getBattleTeam(BattleTeamType teamType){
		return CommonUtil.findOneUtilOkay(battleTeamList, battleTeam -> battleTeam.getData().getTeamType().equals(teamType));
	}

	public List<BattleTeam> getBattleTeamList() {
		return battleTeamList;
	}

	public BattleTeamUnit getIndexBattleTeamUnit(int teamIndex){
		for (BattleTeam battleTeam : battleTeamList) {
			BattleTeamUnit oneUtilOkay = CommonUtil.findOneUtilOkay(battleTeam.getTeamUnitList(), battleTeamUnit -> battleTeamUnit.getTeamUnitIndex() == teamIndex);
			if (oneUtilOkay != null){
				return oneUtilOkay;
			}
		}
		return null;
	}

	public BattleUnit getIdBattleUnit(long id){
		return idBattleUnitMap.get(id);
	}

	public Collection<Long> getAllUserIds(){
		return userTeamUnitMap.keySet();
	}
}
