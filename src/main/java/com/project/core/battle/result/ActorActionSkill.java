package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.skill.BattleSkill;

public class ActorActionSkill extends ActorAction {

	public ActorActionSkill(BattleUnit battleUnit, BattleSkill battleSkill, ActionType actionType) {
		super(null, battleUnit, actionType);
		this.setActionId(battleSkill.getSkillId());
		this.setAdditionId1(battleSkill.getCD());
	}
}
