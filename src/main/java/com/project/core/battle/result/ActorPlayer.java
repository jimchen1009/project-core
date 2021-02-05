package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.skill.BattleSkill;

import java.util.ArrayList;
import java.util.List;

public class ActorPlayer {

	private final List<ActorPlay> actorPlayList;

	public ActorPlayer() {
		this.actorPlayList = new ArrayList<>();
	}

	public void addActorAction(ActorAction actorAction){
		ActorPlay current = getCurrent();
		if (current == null){
			throw new UnsupportedOperationException();
		}
		current.addActorAction(actorAction);
	}

	public void confirmActorSkill(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit){
		ActorPlay current = getCurrent();
		if (current != null && current.getActorType().equals(ActorType.Skill) && current.getRequestId() == requestUnit.getId() && current.getActorId() == battleSkill.getSkillId()) {
			return;
		}
		ActorPlay actorPlay = new ActorPlay(requestUnit, targetUnit, ActorType.Skill, battleSkill.getSkillId());
		actorPlayList.add(actorPlay);
	}

	public ActorPlay addActorPlayer(ActorPlay actorPlay){
		actorPlayList.add(actorPlay);
		return actorPlay;
	}

	private ActorPlay getCurrent(){
		return actorPlayList.isEmpty() ? null : actorPlayList.get(actorPlayList.size() - 1);
	}

	@Override
	public String toString() {
		return actorPlayList.toString();
	}
}
