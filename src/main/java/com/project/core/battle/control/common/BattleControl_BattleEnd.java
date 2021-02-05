package com.project.core.battle.control.common;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.BattleControlId;

public class BattleControl_BattleEnd extends BattleControl_BattleNode {

	public BattleControl_BattleEnd(BattleControlId battleControlId) {
		super(battleControlId, BattleStage.BattleEnd);
	}

	@Override
	protected void execute1(BattleContext battleContext) {
		battleContext.setExecuteCompleted(true);
	}

	@Override
	protected void onSkipSuccess(BattleContext battleContext) {

	}
}
