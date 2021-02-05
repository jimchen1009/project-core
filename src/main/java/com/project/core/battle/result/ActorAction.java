package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;

public class ActorAction {

	private final long requestId;
	private final long targetId;
	private final ActionType actionType;

	private int actionId;
	private int additionId1;
	private int additionId2;

	public ActorAction(BattleUnit requestUnit, BattleUnit targetUnit, ActionType actionType) {
		this.requestId = requestUnit == null ? 0 : requestUnit.getId();
		this.targetId = targetUnit == null ? 0 : targetUnit.getId();
		this.actionType = actionType;
	}

	public ActorAction setActionId(int actionId) {
		this.actionId = actionId;
		return this;
	}

	public ActorAction setAdditionId1(int additionId1) {
		this.additionId1 = additionId1;
		return this;
	}

	public ActorAction setAdditionId2(int additionId2) {
		this.additionId2 = additionId2;
		return this;
	}

	public ActionType getActionType() {
		return actionType;
	}

	@Override
	public String toString() {
		return "{" +
				"requestId=" + requestId +
				", targetId=" + targetId +
				", actionType=" + actionType +
				", actionId=" + actionId +
				", additionId1=" + additionId1 +
				", additionId2=" + additionId2 +
				'}';
	}
}
