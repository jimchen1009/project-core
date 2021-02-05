package com.project.core.battle.model;

import java.util.List;
import java.util.stream.Collectors;

public class BattleTeamUnitData implements IBattleXXXData<BattleTeamUnitData> {

	private final int teamUnitIndex;
	private int enemyUnitIndex;
	private final long teamUnitUserId;
	private List<BattleUnitData> battleUnitDataList;
	private int maxReliveCount;
	private int usedReliveCount;
	private boolean initializeCd;

	private int addDamageRate;			// 队伍伤害加成

	public BattleTeamUnitData(int teamUnitIndex, int enemyUnitIndex, List<BattleUnitData> battleUnitDataList) {
		this.teamUnitIndex = teamUnitIndex;
		this.enemyUnitIndex = enemyUnitIndex;
		this.teamUnitUserId = calculateTeamUnitUserId(battleUnitDataList);
		this.battleUnitDataList = battleUnitDataList;
	}

	public int getTeamUnitIndex() {
		return teamUnitIndex;
	}


	public int getEnemyUnitIndex() {
		return enemyUnitIndex;
	}

	public void setEnemyUnitIndex(int enemyUnitIndex) {
		this.enemyUnitIndex = enemyUnitIndex;
	}

	/**
	 * @return 0 混合小队 否则个人小队
	 */
	public long getTeamUnitUserId() {
		return teamUnitUserId;
	}

	private long calculateTeamUnitUserId(List<BattleUnitData> battleUnitDataList){
		List<Long> userIdList = battleUnitDataList.stream()
				.filter(battleUnitData -> battleUnitData.getUserId() > 0)
				.map(BattleUnitData::getUserId)
				.collect(Collectors.toList());
		return userIdList.size() == 1 ? userIdList.get(0) : 0;
	}

	public List<BattleUnitData> getBattleUnitDataList() {
		return battleUnitDataList;
	}

	public void setBattleUnitDataList(List<BattleUnitData> battleUnitDataList) {
		this.battleUnitDataList = battleUnitDataList;
	}

	public int getMaxReliveCount() {
		return maxReliveCount;
	}

	public void setMaxReliveCount(int maxReliveCount) {
		this.maxReliveCount = maxReliveCount;
	}

	public int getUsedReliveCount() {
		return usedReliveCount;
	}

	public void setUsedReliveCount(int usedReliveCount) {
		this.usedReliveCount = usedReliveCount;
	}


	public boolean isInitializeCd() {
		return initializeCd;
	}

	public void setInitializeCd(boolean initializeCd) {
		this.initializeCd = initializeCd;
	}

	public int getAddDamageRate() {
		return addDamageRate;
	}

	public void setAddDamageRate(int addDamageRate) {
		this.addDamageRate = addDamageRate;
	}

	@Override
	public BattleTeamUnitData deepCopy() {
		BattleTeamUnitData teamUnitData = new BattleTeamUnitData(teamUnitIndex, enemyUnitIndex, BattleDataUtil.deepCopy(battleUnitDataList));
		teamUnitData.setInitializeCd(initializeCd);
		teamUnitData.setMaxReliveCount(maxReliveCount);
		teamUnitData.setUsedReliveCount(usedReliveCount);
		teamUnitData.setAddDamageRate(addDamageRate);
		return teamUnitData;
	}
}
