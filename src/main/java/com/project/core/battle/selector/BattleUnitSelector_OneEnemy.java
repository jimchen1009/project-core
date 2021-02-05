package com.project.core.battle.selector;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.Collections;
import java.util.List;

public class BattleUnitSelector_OneEnemy extends BattleUnitSelectorTeamUnitOne {

	public BattleUnitSelector_OneEnemy() {
		super(BattleUnit::isAlive);
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		BattleTeamUnit enemyTeamUnit = getEnemyTeamUnit(context, requestUnit);
		if (enemyTeamUnit == null){
			return Collections.emptyList();
		}
		List<BattleUnit> battleUnitList = getBattleUnitList(enemyTeamUnit, conditionContext, battleSkill);
		return findOneOrRandomSingleBattleUnit(context, battleUnitList, targetUnit);
	}
}
