package com.project.core.battle.control.common;

import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControl_Node;

public abstract class BattleControl_BattleNode extends BattleControl_Node {

	public BattleControl_BattleNode(BattleControlId battleControlId, BattleStage battleStage) {
		super(battleControlId, battleStage);
	}

	@Override
	protected boolean skip1(BattleContext battleContext) {
		Battle battle = battleContext.getBattle();
		if (!battle.hasBattleWinLos()) {
			return false;
		}
		onSkipSuccess(battleContext);
		battleContext.setExecuteCompleted(true);
		return true;
	}

	protected abstract void onSkipSuccess(BattleContext battleContext);
}
