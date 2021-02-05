package com.project.core.battle.selector;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class BattleUnitSelector_None extends BattleUnitSelector {

	public BattleUnitSelector_None() {
		super(BattleUnit::isAlive);
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return targetUnit == null ? Collections.emptyList() : Collections.singletonList(targetUnit);
	}

	@Override
	public BattleUnit redirectSelectBattleUnit(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return targetUnit;
	}
}
