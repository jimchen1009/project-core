package com.project.core.battle.selector;

import com.game.common.util.CommonUtil;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

abstract class BattleUnitSelectorTeamUnitOne extends BattleUnitSelector {

	public BattleUnitSelectorTeamUnitOne(Predicate<BattleUnit> predicate) {
		super(predicate);
	}

	protected BattleUnit findOneOrRandomBattleUnit(BattleContext context, List<BattleUnit> battleUnitList, BattleUnit targetUnit) {
		if (targetUnit != null && CommonUtil.findOneUtilOkayBool(battleUnitList, battleUnit -> battleUnit.getId() == targetUnit.getId())){
			return targetUnit;
		}
		return context.getBattle().getRandom().select(battleUnitList);
	}

	protected List<BattleUnit> findOneOrRandomSingleBattleUnit(BattleContext context, List<BattleUnit> battleUnitList, BattleUnit targetUnit) {
		BattleUnit battleUnit = findOneOrRandomBattleUnit(context, battleUnitList, targetUnit);
		return battleUnit == null ? Collections.emptyList() : Collections.singletonList(battleUnit);
	}
}
