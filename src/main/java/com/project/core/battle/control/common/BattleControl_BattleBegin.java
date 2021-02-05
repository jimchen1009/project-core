package com.project.core.battle.control.common;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.skill.SkillHandler;

public class BattleControl_BattleBegin extends BattleControl_ReadyNode {

	public BattleControl_BattleBegin(BattleControlId battleControlId) {
		super(battleControlId, BattleStage.BattleBegin);
	}

	@Override
	protected void executeReadyCommand(BattleContext battleContext) {
		BattleUtil.updateChangeEnemyTeamUnit(battleContext);
		BattleUtil.foreachBattleTeamUnit(battleContext.getBattle(), battleTeamUnit -> {
			if (battleTeamUnit.getData().isInitializeCd()) {
				BattleUtil.foreachBattleUnit(battleTeamUnit, BattleUnit::isAlive, BattleUnit::initSkillCD);
			}
		});
		BattleUtil.foreachBattleUnit(battleContext.getBattle(), BattleUnit::isAlive, battleUnit -> {
			SkillHandler.doConditionAllSkill(battleContext, battleUnit, battleUnit, ConditionType.StartBattle);
		});
	}
}
