package com.project.core.battle.skill;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.condition.ISkillCondition;
import com.project.core.battle.skill.condition.SkillConditionType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SkillUtil {

	private static List<ISkillCondition> ConditionList = Arrays.stream(SkillConditionType.values()).sorted(Comparator.comparingInt(IEnumBase::getId)).collect(Collectors.toList());


	public static boolean isConditionLimit(BattleContext battleContext, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit){
		for (ISkillCondition condition : ConditionList) {
			if (condition.isSkillLimit(battleContext, conditionContext, requestUnit, battleSkill, targetUnit)) {
				return true;
			}
		}
		return false;
	}

}
