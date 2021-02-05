package com.project.core.battle.control.pvp;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleTeamType;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.common.BattleControl_RoundRun;

public class BattleControl_RoundRunPVP extends BattleControl_RoundRun {

	public BattleControl_RoundRunPVP(BattleControlId battleControlId, BattleTeamType teamType) {
		super(battleControlId, teamType.firstTeamUnitIndex());
	}

	@Override
	protected void executeRunRound(BattleContext battleContext, BattleTeamUnit teamUnit) {
		executeOperationSkill(battleContext, teamUnit);
	}

	@Override
	protected void onSkipSuccess(BattleContext battleContext) {
	}
}
