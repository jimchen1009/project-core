package com.project.core.battle.model;

import com.game.common.util.RandomUtil;
import com.project.core.battle.BattleTeamType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BiConsumer;

public class BattleDataBuilder {

	private final InnerClassA innerClassTeamA;
	private final InnerClassA innerClassTeamB;
	private BattleTeamUnitData neutralTeamUnitData;

	public BattleDataBuilder() {
		this.innerClassTeamA = new InnerClassA(BattleTeamType.TeamA);
		this.innerClassTeamB = new InnerClassA(BattleTeamType.TeamB);
	}

	/**
	 * @param teamADataUnitList 队伍A 的数据
	 * @param teamBDataUnitList 队伍B 的数据
	 */
	public BattleDataBuilder addTeamUnitWithEnemy(List<BattleUnitData> teamADataUnitList, List<BattleUnitData> teamBDataUnitList){
		return addTeamUnitWithEnemy(teamADataUnitList, teamBDataUnitList, null);
	}

	/**
	 *
	 * @param teamADataUnitList 队伍A 的数据
	 * @param teamBDataUnitList 队伍B 的数据
	 * @param consumer
	 * @return
	 */
	public BattleDataBuilder addTeamUnitWithEnemy(List<BattleUnitData> teamADataUnitList, List<BattleUnitData> teamBDataUnitList, BiConsumer<BattleTeamUnitData, BattleTeamUnitData> consumer){
		int teamUnitIndexA = innerClassTeamA.poolTeamUnitIndex();
		int teamUnitIndexB = innerClassTeamB.poolTeamUnitIndex();
		BattleTeamUnitData teamUnitDataA = new BattleTeamUnitData(teamUnitIndexA, teamUnitIndexB, teamADataUnitList);
		innerClassTeamA.teamUnitDataAList.add(teamUnitDataA);
		BattleTeamUnitData teamUnitDataB = new BattleTeamUnitData(teamUnitIndexB, teamUnitIndexA, teamBDataUnitList);
		innerClassTeamB.teamUnitDataAList.add(teamUnitDataB);
		if (consumer != null){
			consumer.accept(teamUnitDataA, teamUnitDataB);
		}
		return this;
	}

	/**
	 * 增加队伍A的数据
	 * @param teamADataUnitList
	 */
	public BattleDataBuilder addATeamUnitUseLastEnemy(List<BattleUnitData> teamADataUnitList){
		int teamUnitIndexA = innerClassTeamA.poolTeamUnitIndex();
		int teamUnitIndexB = RandomUtil.select(innerClassTeamB.teamUnitDataAList).getTeamUnitIndex();
		innerClassTeamA.teamUnitDataAList.add(new BattleTeamUnitData(teamUnitIndexA, teamUnitIndexB, teamADataUnitList));
		return this;
	}

	/**
	 * 第三方的数据
	 * @param neutralTeamUnitData
	 */
	public BattleDataBuilder setNeutralTeamUnitData(BattleTeamUnitData neutralTeamUnitData){
		this.neutralTeamUnitData = neutralTeamUnitData;
		return this;
	}

	public BattleData build(String name, long randomSeed){
		BattleTeamData teamData = neutralTeamUnitData == null ? null : new BattleTeamData(BattleTeamType.Neutral, Collections.singletonList(neutralTeamUnitData));
		return new BattleData(name, innerClassTeamA.buildTeamData(), innerClassTeamB.buildTeamData(), teamData, randomSeed);
	}

	private static class InnerClassA {
		private final BattleTeamType teamType;
		private final List<BattleTeamUnitData> teamUnitDataAList;
		private final Queue<Integer> teamUnitIndexes;

		public InnerClassA(BattleTeamType teamType) {
			this.teamType = teamType;
			this.teamUnitDataAList = new ArrayList<>();
			this.teamUnitIndexes = new ArrayDeque<>(teamType.getTeamUnitIndexes());
		}

		public int poolTeamUnitIndex() {
			return Objects.requireNonNull(teamUnitIndexes.poll());
		}

		public BattleTeamData buildTeamData(){
			return new BattleTeamData(teamType, teamUnitDataAList);
		}
	}
}
