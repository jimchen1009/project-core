package com.project.core.battle.control.common;

import com.game.common.util.ResultCode;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControlList;

public class BattleControl_BattleLine extends BattleControlList {

	public BattleControl_BattleLine(BattleControlId battleControlId) {
		super(battleControlId, 1);
	}

	@Override
	protected ResultCode execute0(BattleContext battleContext) {
		ResultCode resultCode = super.execute0(battleContext);
		if (resultCode.isSuccess()) {
			battleContext.getBattle().updateBattleWinLos();
		}
		return resultCode;
	}

	@Override
	protected ResultCode skip0(BattleContext battleContext) {
		ResultCode resultCode = super.skip0(battleContext);
		if (resultCode.isSuccess()) {
			battleContext.getBattle().updateBattleWinLos();
		}
		return resultCode;
	}

	@Override
	protected void onOneLoopSuccess(BattleContext battleContext, int finishLoopCount, boolean executeSkip) {
		super.onOneLoopSuccess(battleContext, finishLoopCount, executeSkip);
		battleContext.getBattle().setBattleStage(BattleStage.BattleFinal);
	}
}
