package com.project.core.battle.selector;

import com.game.common.util.CommonUtil;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.List;
import java.util.function.Predicate;

public abstract class BattleUnitSelector implements IBattleUnitSelector {

	private final Predicate<BattleUnit> predicate;

	public BattleUnitSelector(Predicate<BattleUnit> predicate) {
		this.predicate = predicate;
	}

	protected List<BattleUnit> getBattleUnitList(BattleTeamUnit battleTeamUnit, IConditionContext conditionContext, BattleSkill battleSkill){
		return battleTeamUnit.getBattleUnitList( battleUnit -> battleUnit.getUnitType().isSelectSupport() && predicate.test(battleUnit));
	}

	@Override
	public BattleUnit redirectSelectBattleUnit(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		List<BattleUnit> battleUnitList = selectBattleUnits(context, conditionContext, requestUnit, battleSkill, targetUnit);
		BattleUnit oneUtilOkay;
		if (targetUnit != null) {
			oneUtilOkay = CommonUtil.findOneUtilOkay(battleUnitList, battleUnit -> battleUnit.getId() == targetUnit.getId());
		}
		else {
			oneUtilOkay = battleUnitList.isEmpty() ? null : context.getBattle().getRandom().select(battleUnitList);
		}
		return oneUtilOkay;
	}
}
