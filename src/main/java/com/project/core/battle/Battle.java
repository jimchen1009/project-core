package com.project.core.battle;

import com.project.core.battle.buff.BuffHandler;
import com.project.core.battle.control.BattleType;
import com.project.core.battle.model.BattleData;
import com.project.core.battle.operate.BattleOperateManager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Battle extends BattleXXX<BattleData> {

	private final BattleType battleType;
	private final long battleId;
	private final long createTime;
	private final AtomicLong uniqueIdGen;

	private final BattleUnitManager battleUnitManager;
	private final BattleControlManager controlManager;
	private final BattleOperateManager operateManager;
	private final BattleRandom battleRandom;

	private BattleStage battleStage;
	private BattleWinLos battleWinLos;

	public Battle(BattleType battleType, long uniqueId, BattleData battleData) {
		super(battleData);
		this.battleType = battleType;
		this.battleId = uniqueId;
		this.createTime = System.currentTimeMillis();
		this.uniqueIdGen = new AtomicLong();

		//最后初始化战斗对象
		List<BattleTeam> battleTeamList = battleData.getBattleTeamDataList().stream()
				.map(battleTeamData -> new BattleTeam(this, battleTeamData))
				.collect(Collectors.toList());
		this.battleUnitManager = new BattleUnitManager(battleTeamList);
		this.controlManager = new BattleControlManager();
		this.operateManager = new BattleOperateManager();
		this.battleRandom = new BattleRandom(battleData.getRandomSeed());
		this.battleStage = BattleStage.None;
		this.battleWinLos = null;			//初始化没有胜负
	}

	public BattleType getBattleType() {
		return battleType;
	}

	public long getBattleId() {
		return battleId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public long genObjectId(){
		return uniqueIdGen.incrementAndGet();
	}

	public BattleUnitManager getBattleUnitManager() {
		return battleUnitManager;
	}

	public BattleControlManager getControlManager() {
		return controlManager;
	}

	public BattleOperateManager getOperateManager() {
		return operateManager;
	}

	public BattleRandom getRandom() {
		return battleRandom;
	}

	/**
	 * 创建战斗之后的初始化接口
	 */
	public BattleContext createInitializeBattle() {
		if (!isBattleStage(BattleStage.None)) {
			return null;
		}
		BattleContext context = new BattleContext(0, Battle.this);
		BattleUtil.foreachBattleUnit(this, null, battleUnit -> {
			BuffHandler.createInitializeBattle(context, battleUnit);
		});
		getData().setCurRound(0);
		setBattleStage(BattleStage.BattleBegin);
		return context;
	}

	public BattleStage getBattleStage() {
		return battleStage;
	}

	public boolean isBattleStage(BattleStage battleStage) {
		return this.battleStage.equals(battleStage);
	}

	public void setBattleStage(BattleStage battleStage) {
		this.battleStage = battleStage;
	}

	public BattleWinLos getBattleWinLos() {
		return battleWinLos;
	}

	public boolean hasBattleWinLos(){
		return battleWinLos != null;
	}

	public Battle updateBattleWinLos() {
		if (battleWinLos == null) {
			battleWinLos = BattleUtil.checkBattleWinLos(this);
		}
		return this;
	}
}
