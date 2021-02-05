package com.project.core.battle.control.common;

import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControlList;

public class BattleControl_RoundLoop extends BattleControlList {

	public BattleControl_RoundLoop(BattleControlId battleControlId) {
		super(battleControlId);
	}

	@Override
	public int getMaxLoopCount(BattleContext battleContext) {
		return battleContext.getBattle().getData().getMaxRound();
	}

	@Override
	protected void onOneLoopSuccess(BattleContext battleContext, int finishLoopCount, boolean executeSkip) {
		super.onOneLoopSuccess(battleContext, finishLoopCount, executeSkip);
		Battle battle = battleContext.getBattle();
		battle.getData().incBattleRound();
		battle.updateBattleWinLos();
	}
}
