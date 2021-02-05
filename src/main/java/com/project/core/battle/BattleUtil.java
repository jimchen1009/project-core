package com.project.core.battle;

import com.game.common.util.CommonUtil;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.buff.featrue.IBuffFeature;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BattleUtil {

	/**
	 * 按照默认顺序遍历所有的战斗对象
	 * @param battle
	 * @param predicate
	 * @param consumer
	 */
	public static void foreachBattleUnit(Battle battle, Predicate<BattleUnit> predicate, Consumer<BattleUnit> consumer){
		List<BattleTeam> battleTeamList = battle.getBattleUnitManager().getBattleTeamList();
		for (BattleTeam battleTeam : battleTeamList) {
			foreachBattleUnit(battleTeam, predicate, consumer);
		}
	}


	/**
	 * 按照默认顺序遍历所有的战斗对象
	 * @param battle
	 * @param consumer
	 */
	public static void foreachBattleUnitBuffFeature(Battle battle, Predicate<BattleUnit> predicate, Consumer<IBuffFeature> consumer){
		List<BattleTeam> battleTeamList = battle.getBattleUnitManager().getBattleTeamList();
		for (BattleTeam battleTeam : battleTeamList) {
			foreachBattleUnit(battleTeam, predicate, battleUnit -> BuffUtil.foreachBuffFeature(battleUnit, consumer));
		}
	}

	/**
	 * 按照默认顺序遍历所有的战斗对象
	 * @param battleTeam
	 * @param predicate
	 * @param consumer
	 */
	public static void foreachBattleUnit(BattleTeam battleTeam, Predicate<BattleUnit> predicate, Consumer<BattleUnit> consumer){
		List<BattleTeamUnit> teamUnitList = battleTeam.getTeamUnitList();
		for (BattleTeamUnit teamUnit : teamUnitList) {
			foreachBattleUnit(teamUnit, predicate, consumer);
		}
	}

	/**
	 * 按照默认顺序遍历所有的战斗对象
	 * @param teamUnit
	 * @param predicate
	 * @param consumer
	 */
	public static void foreachBattleUnit(BattleTeamUnit teamUnit, Predicate<BattleUnit> predicate, Consumer<BattleUnit> consumer){
		List<BattleUnit> battleUnitList = teamUnit.getBattleUnitList();
		for (BattleUnit battleUnit : battleUnitList) {
			if (predicate == null ||predicate.test(battleUnit)){
				consumer.accept(battleUnit);
			}
		}
	}

	/**
	 * 按照默认顺序遍历所有单元队伍
	 * @param battle
	 * @param consumer
	 */
	public static void foreachBattleTeamUnit(Battle battle, Consumer<BattleTeamUnit> consumer){
		List<BattleTeam> battleTeamList = battle.getBattleUnitManager().getBattleTeamList();
		for (BattleTeam battleTeam : battleTeamList) {
			foreachBattleTeamUnit(battleTeam, consumer);
		}
	}

	/**
	 * 按照默认顺序遍历所有单元队伍
	 * @param battleTeam
	 * @param consumer
	 */
	public static void foreachBattleTeamUnit(BattleTeam battleTeam, Consumer<BattleTeamUnit> consumer){
		for (BattleTeamUnit battleTeamUnit : battleTeam.getTeamUnitList()) {
			consumer.accept(battleTeamUnit);
		}
	}

	/**
	 * 判定输赢
	 * @param battle
	 * @return
	 */
	public static BattleWinLos checkBattleWinLos(Battle battle) {
		if (battle.getBattleWinLos() != null) {
			return battle.getBattleWinLos();
		}
		BattleWinLos battleWinLos = null;
		BattleUnitManager unitManager = battle.getBattleUnitManager();
		if (unitManager.getBattleTeam(BattleTeamType.TeamA).isAllDead()) {
			battleWinLos = BattleWinLos.TeamB;
		}
		else if (unitManager.getBattleTeam(BattleTeamType.TeamB).isAllDead()){
			battleWinLos = BattleWinLos.TeamA;
		}
		else if (battle.getData().reachMaxRound()) {
			battleWinLos = BattleWinLos.Tie;
		}
		return battleWinLos;
	}

	/**
	 * 是否够挂了
	 * @param battle
	 * @return
	 */
	public static boolean isAllDead(Battle battle) {
		BattleUnitManager unitManager = battle.getBattleUnitManager();
		return unitManager.getBattleTeam(BattleTeamType.TeamA).isAllDead() || unitManager.getBattleTeam(BattleTeamType.TeamB).isAllDead();
	}

	/**
	 * 自动切换敌对阵型
	 * @param battle
	 */
	public static void updateChangeEnemyTeamUnit(Battle battle) {
		BattleUnitManager unitManager = battle.getBattleUnitManager();
		List<BattleTeamUnit> teamUnitListA = unitManager.getBattleTeam(BattleTeamType.TeamA).getTeamUnitList(BattleTeamUnit::isAlive);
		List<BattleTeamUnit> teamUnitListB = unitManager.getBattleTeam(BattleTeamType.TeamB).getTeamUnitList(BattleTeamUnit::isAlive);
		updateChangeEnemyTeamUnit(battle, teamUnitListA, teamUnitListB);
		updateChangeEnemyTeamUnit(battle, teamUnitListB, teamUnitListA);
	}

	/**
	 * 自动切换敌对阵型
	 * @param teamUnitListA
	 * @param teamUnitListB
	 */
	private static void updateChangeEnemyTeamUnit(Battle battle, List<BattleTeamUnit> teamUnitListA, List<BattleTeamUnit> teamUnitListB) {
		if (teamUnitListB.isEmpty()){
			return;
		}
		for (BattleTeamUnit battleTeamUnit : teamUnitListA) {
			if (CommonUtil.findOneUtilOkayBool(teamUnitListB, teamUnit -> teamUnit.getEnemyUnitIndex() == battleTeamUnit.getTeamUnitIndex())) {
				continue;
			}
			BattleTeamUnit teamUnit = battle.getRandom().select(teamUnitListB);
			battleTeamUnit.setEnemyUnitIndex(teamUnit.getTeamUnitIndex());
		}
	}
}
