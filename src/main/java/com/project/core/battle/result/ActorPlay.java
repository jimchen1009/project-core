package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;

import java.util.ArrayList;
import java.util.List;

public class ActorPlay {

	private final long requestId;
	private final ActorType actorType;
	private final int actorId;
	private final long targetId;
	private final List<ActorAction> actorActions;

	public ActorPlay(BattleUnit requestUnit, BattleUnit targetUnit, ActorType actorType, int actorId) {
		this.requestId = requestUnit == null ? 0 : requestUnit.getId();
		this.targetId = targetUnit == null ? 0 : targetUnit.getId();
		this.actorType = actorType;
		this.actorId = actorId;
		this.actorActions = new ArrayList<>();
	}

	public long getRequestId() {
		return requestId;
	}

	public ActorType getActorType() {
		return actorType;
	}

	public int getActorId() {
		return actorId;
	}

	public void addActorAction(ActorAction actorAction){
		actorActions.add(actorAction);
	}

	@Override
	public String toString() {
		return "{" +
				"requestId=" + requestId +
				", actorType=" + actorType +
				", actorId=" + actorId +
				", targetId=" + targetId +
				", actorActions=" + actorActions +
				'}';
	}
}
