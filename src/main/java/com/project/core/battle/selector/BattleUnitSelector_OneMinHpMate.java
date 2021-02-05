package com.project.core.battle.selector;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BattleUnitSelector_OneMinHpMate extends BattleUnitSelector {

	public BattleUnitSelector_OneMinHpMate() {
		super(BattleUnit::isAlive);
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		List<BattleUnit> battleUnitList = getBattleUnitList(requestUnit.getTeamUnit(), conditionContext, battleSkill);
		Optional<BattleUnit> optional = battleUnitList.stream().min(Comparator.comparingInt(battleUnit -> battleUnit.getAttribute(Attribute.hp)));
		return optional.map(Collections::singletonList).orElse(Collections.emptyList());
	}
}
