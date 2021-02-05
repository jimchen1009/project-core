package com.project.core.battle.result;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.skill.damage.DamageType;

public class ActorActionDamage extends ActorAction {

	public ActorActionDamage(BattleUnit attackUnit, DamageType damageType, BattleUnit targetUnit, int damage) {
		super(attackUnit, targetUnit, ActionType.Damage);
		this.setActionId(damageType.getId());
		this.setAdditionId1(damage);
		this.setAdditionId2(targetUnit.getAttribute(Attribute.hp));
	}
}
