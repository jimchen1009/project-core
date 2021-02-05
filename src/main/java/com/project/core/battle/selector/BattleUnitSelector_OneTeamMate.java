package com.project.core.battle.selector;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.List;

public class BattleUnitSelector_OneTeamMate extends BattleUnitSelectorTeamUnitOne {

	public BattleUnitSelector_OneTeamMate() {
		super(BattleUnit::isAlive);
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		List<BattleUnit> battleUnitList = getBattleUnitList(requestUnit.getTeamUnit(), conditionContext, battleSkill);
		return findOneOrRandomSingleBattleUnit(context, battleUnitList, targetUnit);
	}
}
