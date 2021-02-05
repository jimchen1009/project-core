package com.project.core.battle.control.pvp;

import com.project.core.battle.BattleStage;
import com.project.core.battle.BattleTeamType;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.common.BattleControl_ReadyNode;

public class BattleControl_PlayRoundPVP extends BattleControl_ReadyNode {

	public BattleControl_PlayRoundPVP(BattleControlId battleControlId,  BattleTeamType teamType) {
		super(battleControlId, BattleStage.PlayRunTeam.crateChild(teamType.getId()));
	}
}
