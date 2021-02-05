package com.project.core.battle.control.pve;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleTeamType;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.common.BattleControl_RoundRun;

public class BattleControl_RoundRunPVE extends BattleControl_RoundRun {

	public BattleControl_RoundRunPVE(BattleControlId battleControlId) {
		super(battleControlId, BattleTeamType.TeamA.firstTeamUnitIndex());
	}

	@Override
	protected void executeRunRound(BattleContext battleContext) {
		castOperationSkill(battleContext, BattleTeamType.TeamA.firstTeamUnitIndex());
		castOperationSkill(battleContext, BattleTeamType.TeamB.firstTeamUnitIndex());
	}

	@Override
	protected void onSkipSuccess(BattleContext battleContext) {

	}
}
