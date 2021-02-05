package com.project.core.battle.skill.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

public class SkillCondition_Buff extends SkillCondition {

	@Override
	protected boolean isSkillLimit0(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return BuffUtil.predicateFeatureUtilOneOkBool(requestUnit, feature -> feature.isSkillLimit(context, conditionContext));
	}
}
