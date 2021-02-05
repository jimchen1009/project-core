package com.project.core.battle.skill.condition;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public enum SkillConditionType implements IEnumBase, ISkillCondition {
	Buff(1, "", new SkillCondition_Buff()),
	PassiveSkill(2, "",  new SkillCondition_PassiveSkill()),
	;

	private final int id;
	private final ISkillCondition handler;

	SkillConditionType(int id, String comment, ISkillCondition handler) {
		this.id = id;
		this.handler = handler;
	}

	@Override
	public int getId() {
		return 0;
	}


	@Override
	public boolean isSkillLimit(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return handler.isSkillLimit(context, conditionContext, requestUnit, battleSkill, targetUnit);
	}
}
