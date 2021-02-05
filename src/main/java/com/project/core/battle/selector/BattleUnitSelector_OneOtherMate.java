package com.project.core.battle.selector;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.List;
import java.util.stream.Collectors;

public class BattleUnitSelector_OneOtherMate extends BattleUnitSelectorTeamUnitOne {

	public BattleUnitSelector_OneOtherMate() {
		super(BattleUnit::isAlive);
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		List<BattleUnit> battleUnitList = getBattleUnitList(requestUnit.getTeamUnit(), conditionContext, battleSkill).stream()
				.filter( battleUnit -> battleUnit.getId() != requestUnit.getId())
				.collect(Collectors.toList());
		return findOneOrRandomSingleBattleUnit(context, battleUnitList, targetUnit);
	}
}
