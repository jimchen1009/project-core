package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.Buff;

public class ActorActionBuff extends ActorAction {

	public ActorActionBuff(BattleUnit requestUnit, BattleUnit targetUnit, ActionType actionType, Buff buff, boolean isDoAction) {
		super(requestUnit, targetUnit, actionType);
		this.setActionId(buff.getBuffId());
		this.setAdditionId1(buff.getRemainRound());
		this.setAdditionId2(isDoAction ? 1 : 0);
	}
}
