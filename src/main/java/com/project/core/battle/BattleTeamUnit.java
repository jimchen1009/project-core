package com.project.core.battle;

import com.game.common.util.CommonUtil;
import com.project.core.battle.model.BattleTeamUnitData;
import com.project.core.battle.model.BattleUnitData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BattleTeamUnit extends BattleXXX<BattleTeamUnitData> {

	private final BattleTeamType teamType;
	private final List<BattleUnit> battleUnitList;		//一个队伍里有多个人员
	private List<Long> curRoundDeadUnitIds;

	public BattleTeamUnit(Battle battle, BattleTeamType teamType, BattleTeamUnitData teamUnitData) {
		super(teamUnitData);
		this.teamType = teamType;
		this.battleUnitList = teamUnitData.getBattleUnitDataList().stream()
				.sorted(Comparator.comparingInt(BattleUnitData::getIndex))
				.map(battleUnitData -> new BattleUnit(battle, this, battleUnitData))
				.collect(Collectors.toList());
		this.curRoundDeadUnitIds = new ArrayList<>(battleUnitList.size());
	}

	public BattleTeamType getTeamType() {
		return teamType;
	}

	public int getTeamUnitIndex(){
		return getData().getTeamUnitIndex();
	}

	public int getEnemyUnitIndex(){
		return getData().getEnemyUnitIndex();
	}

	public void setEnemyUnitIndex(int enemyUnitIndex){
		getData().setEnemyUnitIndex(enemyUnitIndex);
	}

	public long getTeamUnitUserId(){
		return getData().getTeamUnitUserId();
	}

	@Override
	public void dataUpdate() {
		getData().setBattleUnitDataList(battleUnitList.stream().map(BattleXXX::getData).collect(Collectors.toList()));
	}

	public List<BattleUnit> getBattleUnitList() {
		return battleUnitList;
	}

	public List<BattleUnit> getBattleUnitList(Predicate<BattleUnit> predicate) {
		return battleUnitList.stream().filter(predicate).collect(Collectors.toList());
	}

	public void addCurRoundDeadUnitId(BattleUnit battleUnit) {
		if (curRoundDeadUnitIds.contains(battleUnit.getId())) {
			return;
		}
		curRoundDeadUnitIds.add(battleUnit.getId());
	}

	public void clearCurRoundDeadUniqueIds() {
		this.curRoundDeadUnitIds.clear();
	}

	public boolean isCurRoundDeadUnit(long id) {
		return this.curRoundDeadUnitIds.contains(id);
	}

	public Collection<Long> getCurRoundDeadUnitIds() {
		return curRoundDeadUnitIds;
	}

	public BattleUnit getIndexBattleUnit(int index){
		return CommonUtil.findOneUtilOkay(battleUnitList, battleTeamUnit -> battleTeamUnit.getData().getIndex() == index);
	}

	public BattleUnit getIdBattleUnit(long id){
		return CommonUtil.findOneUtilOkay(battleUnitList, battleTeamUnit -> battleTeamUnit.getId() == id);
	}

	public int getRemainReliveCount() {
		return getData().getMaxReliveCount() - getData().getUsedReliveCount();
	}

	public boolean isAlive() {
		return CommonUtil.findOneUtilOkay(battleUnitList, BattleUnit::isAlive) != null;
	}

	public Collection<Long> getAllUnitUserIds(){
		return battleUnitList.stream().filter( battleUnit -> battleUnit.getUserId() > 0).map(BattleUnit::getUserId).collect(Collectors.toSet());
	}
}
