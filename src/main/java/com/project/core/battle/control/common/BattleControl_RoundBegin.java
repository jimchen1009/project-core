package com.project.core.battle.control.common;

import com.game.common.util.TupleCode;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControl_Command;
import com.project.core.battle.skill.SkillHandler;

public class BattleControl_RoundBegin extends BattleControl_Command<Long, Long> {

	public BattleControl_RoundBegin(BattleControlId battleControlId) {
		super(battleControlId, BattleStage.RoundBegin);
	}

	@Override
	protected TupleCode<Long> getExecuteCommand(BattleContext battleContext, Long requestCommand) {
		return new TupleCode<>(requestCommand);
	}

	@Override
	protected TupleCode<Long> getExecuteAICommand(BattleContext battleContext) {
		return new TupleCode<>(battleContext.getOperateUserId());
	}

	@Override
	protected boolean executeCommand(BattleContext battleContext) {
		BattleUtil.foreachBattleTeamUnit(battleContext.getBattle(), BattleTeamUnit::clearCurRoundDeadUniqueIds);
		BattleUtil.foreachBattleUnit(battleContext.getBattle(), BattleUnit::isAlive, BattleUnit::onRoundBegin);

		BattleUtil.foreachBattleUnitBuffFeature(battleContext.getBattle(), BattleUnit::isAlive, feature -> feature.onBattleStartRound(battleContext));

		BattleUtil.foreachBattleUnit(battleContext.getBattle(), BattleUnit::isAlive, battleUnit -> {
			SkillHandler.doConditionAllSkill(battleContext, battleUnit,  battleUnit, ConditionType.MyHpRateBelow);
		});
		BattleUtil.foreachBattleUnit(battleContext.getBattle(), BattleUnit::isAlive, battleUnit -> {
			SkillHandler.doConditionAllSkill(battleContext, battleUnit, battleUnit,  ConditionType.MyHpRateAbove);
		});
		return true;
	}

	@Override
	protected void onSkipSuccess(BattleContext battleContext) {
	}
}
