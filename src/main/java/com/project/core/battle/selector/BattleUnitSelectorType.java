package com.project.core.battle.selector;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.List;

public enum BattleUnitSelectorType implements IEnumBase, IBattleUnitSelector {
	None(0, "", new BattleUnitSelector_None()),
	ONE_ENEMY(1, "敌方选定单体", new BattleUnitSelector_OneEnemy()),
	ALL_MATE(3, "己方全体", new BattleUnitSelector_AllTeamMate()),
	MYSELF(4, "自己", new BattleUnitSelector_MySelft()),
	ONE_MATE(7, "己方选定单体", new BattleUnitSelector_OneTeamMate()),
	ONE_MIN_HP_MATE(10, "血最少的我方单体", new BattleUnitSelector_OneMinHpMate()),

	ONE_OTHER_MATE(101, "己方选定单体, 除了自己", new BattleUnitSelector_OneOtherMate()),
	;

	private final int id;
	private final String comment;
	private final IBattleUnitSelector selector;


	BattleUnitSelectorType(int id, String comment, IBattleUnitSelector selector) {
		this.id = id;
		this.comment = comment;
		this.selector = selector;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<BattleUnit> selectBattleUnits(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return selector.selectBattleUnits(context, conditionContext, requestUnit, battleSkill, targetUnit);
	}

	@Override
	public BattleUnit redirectSelectBattleUnit(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		return selector.redirectSelectBattleUnit(context, conditionContext, requestUnit, battleSkill, targetUnit);
	}
}
