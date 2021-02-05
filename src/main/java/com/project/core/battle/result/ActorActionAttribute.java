package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.buff.Buff;

public class ActorActionAttribute extends ActorAction {

	public ActorActionAttribute(BattleUnit requestUnit, BattleUnit targetUnit, Attribute attribute) {
		super(requestUnit, targetUnit, ActionType.Attribute);
		this.setActionId(attribute.getId());
		this.setAdditionId1(targetUnit.getAttribute(attribute));
	}
}
