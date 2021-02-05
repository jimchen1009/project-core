package com.project.core.battle.model;

import com.project.core.battle.BattleTeamType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleData implements IBattleXXXData<BattleData> {

	private final String name;
	private final BattleTeamData teamA;
	private final BattleTeamData teamB;
	private final BattleTeamData neutral;
	private final long randomSeed;			//随机种子

	private int curRound = 0;				// 本次战斗回合数
	private int maxRound = 99;				// 本次战斗最大回合数
	private int totalRound = 0;				// 总战斗回合数


	public BattleData(String name, BattleTeamData teamA, BattleTeamData teamB, BattleTeamData neutral, long randomSeed) {
		this.name = name;
		this.teamA =  checkIllegalBattleTeam(teamA, BattleTeamType.TeamA);
		this.teamB =  checkIllegalBattleTeam(teamB, BattleTeamType.TeamB);
		this.neutral = neutral == null ? null : checkIllegalBattleTeam(neutral, BattleTeamType.Neutral);
		this.randomSeed = randomSeed == 0 ? System.currentTimeMillis() : randomSeed;
	}

	public String getName() {
		return name;
	}

	public BattleTeamData getTeamA() {
		return teamA;
	}


	public BattleTeamData getTeamB() {
		return teamB;
	}


	public BattleTeamData getNeutral() {
		return neutral;
	}

	public long getRandomSeed() {
		return randomSeed;
	}

	public List<BattleTeamData> getBattleTeamDataList(){
		List<BattleTeamData> teamDataList = new ArrayList<>(3);
		teamDataList.add(teamA);
		teamDataList.add(teamB);
		if (neutral != null){
			teamDataList.add(neutral);
		}
		return teamDataList;
	}

	public int getCurRound() {
		return curRound;
	}

	public void setCurRound(int curRound) {
		this.curRound = curRound;
	}

	public int getMaxRound() {
		return maxRound;
	}

	public void setMaxRound(int maxRound) {
		this.maxRound = maxRound;
	}

	public int getTotalRound() {
		return totalRound;
	}

	public void incBattleRound() {
		curRound++;
		totalRound++;
	}

	public boolean reachMaxRound(){
		return curRound >= maxRound;
	}

	public void setTotalRound(int totalRound) {
		this.totalRound = totalRound;
	}

	private BattleTeamData checkIllegalBattleTeam(BattleTeamData battleTeamData, BattleTeamType teamType){
		if (battleTeamData.getTeamType().equals(teamType)){
			return battleTeamData;
		}
		throw new IllegalArgumentException(battleTeamData.getTeamType().name());
	}

	@Override
	public BattleData deepCopy() {
		BattleData battleData = new BattleData(name, teamA, teamB, neutral, randomSeed);
		battleData.setCurRound(curRound);
		battleData.setMaxRound(maxRound);
		battleData.setTotalRound(totalRound);
		return battleData;
	}
}
