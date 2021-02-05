package com.project.core.battle.selector;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.List;

public class BattleUnitSelector_AllTeamMate extends BattleUnitSelector {

	public BattleUnitSelector_AllTeamMate() {
		super(BattleUnit::isAlive);
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return getBattleUnitList(requestUnit.getTeamUnit(), conditionContext, battleSkill);
	}
}
