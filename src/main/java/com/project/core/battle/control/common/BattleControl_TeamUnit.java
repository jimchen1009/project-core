package com.project.core.battle.control.common;

import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.BattleTeamType;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControl_Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BattleControl_TeamUnit<T, E> extends BattleControl_Command<T, E> {

	private static final Logger logger = LoggerFactory.getLogger(BattleControl_TeamUnit.class);

	protected final int teamUnitIndex;

	public BattleControl_TeamUnit(BattleControlId battleControlId, BattleStage battleStage, int teamUnitIndex) {
		super(battleControlId, battleStage.crateChild(teamUnitIndex));
		this.teamUnitIndex = teamUnitIndex;
	}

	protected final BattleTeamType getTeamType(){
		return BattleTeamType.getIndexTeamType(teamUnitIndex);
	}

	protected final boolean executeCommand(BattleContext battleContext) {
		Battle battle = battleContext.getBattle();
		BattleTeamUnit battleTeamUnit = battle.getBattleUnitManager().getIndexBattleTeamUnit(teamUnitIndex);
		List<BattleUnit> battleUnitList = battleTeamUnit.getBattleUnitList(BattleUnit::isAlive);
		for (BattleUnit battleUnit : battleUnitList) {
			if (battleUnit.getUserId() <= 0) {
				continue;
			}
			if (!battle.getOperateManager().containerOperate(battleUnit.getUserId())){
				return false;
			}
		}
		return executeTeamUnit(battleContext, battleTeamUnit);
	}

	protected abstract boolean executeTeamUnit(BattleContext battleContext, BattleTeamUnit teamUnit);
}
