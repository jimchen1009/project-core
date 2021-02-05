package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.skill.heal.HealType;

public class ActorActionHeal extends ActorAction {

	public ActorActionHeal(BattleUnit requestUnit, HealType healType, BattleUnit targetUnit, int healHp) {
		super(requestUnit, targetUnit, ActionType.Heal);
		this.setActionId(healType.getId());
		this.setAdditionId1(healHp);
		this.setAdditionId2(requestUnit.getAttribute(Attribute.hp));
	}
}
