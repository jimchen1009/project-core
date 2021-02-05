package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;

import java.util.ArrayList;
import java.util.List;

public class ActorPlay {

	private final long requestId;
	private final long targetId;
	private final ActorType actorType;
	private final int actorId;
	private final List<ActorAction> actorActions;



	public ActorPlay(long requestId, long targetId, ActorType actorType, int actorId) {
		this.requestId = requestId;
		this.targetId = targetId;
		this.actorType = actorType;
		this.actorId = actorId;
		this.actorActions = new ArrayList<>();
	}

	public ActorPlay(BattleUnit requestUnit, BattleUnit targetUnit, ActorType actorType, int actorId) {
		this(requestUnit == null ? 0: requestUnit.getId(), targetUnit == null ? 0 :targetUnit.getId(), actorType, actorId);
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
