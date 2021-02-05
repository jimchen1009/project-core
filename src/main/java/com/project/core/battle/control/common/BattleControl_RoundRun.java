package com.project.core.battle.control.common;

import com.game.common.util.TupleCode;
import com.project.core.battle.Battle;
import com.project.core.battle.BattleConstant;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleUnitManager;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.ai.BattleAIService;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.operate.BattleOperateManager;
import com.project.core.battle.operate.OperateContext;
import com.project.core.battle.operate.OperateSkill;
import com.project.core.battle.operate.OperateSkills;
import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.SkillHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BattleControl_RoundRun extends BattleControl_TeamUnit<OperateSkills, OperateSkills> {

	public BattleControl_RoundRun(BattleControlId battleControlId, int teamUnitIndex) {
		super(battleControlId, BattleStage.RoundRunTeam, teamUnitIndex);
	}

	@Override
	protected final TupleCode<OperateSkills> getExecuteCommand(BattleContext battleContext, OperateSkills requestCommand) {
		List<OperateSkill> operateList = new ArrayList<>();
		BattleUnitManager battleUnitManager = battleContext.getBattle().getBattleUnitManager();
		for (OperateSkill operate : requestCommand.getOperateList()) {
			BattleUnit battleUnit = battleUnitManager.getIdBattleUnit(operate.getRequestId());
			if (battleUnit == null || battleUnit.getTeamUnit().getTeamUnitIndex() != teamUnitIndex){
				continue;
			}
			if (battleUnit.getId() != operate.getRequestId()) {
				continue;
			}
			BattleSkill battleSkill = battleUnit.getSkill(operate.getUseSkillId());
			if (battleSkill == null || !battleSkill.isActiveSkill()){
				continue;
			}
			operateList.add(operate);
		}
		requestCommand.setOperateList(operateList);
		return new TupleCode<>(requestCommand);
	}

	@Override
	protected TupleCode<OperateSkills> getExecuteAICommand(BattleContext battleContext) {
		return new TupleCode<>(new OperateSkills(Collections.emptyList()));
	}

	@Override
	protected final boolean executeTeamUnit(BattleContext battleContext, BattleTeamUnit teamUnit) {
		executeRunRound(battleContext, teamUnit);
		return true;
	}

	protected abstract void executeRunRound(BattleContext battleContext, BattleTeamUnit teamUnit);


	protected void executeOperationSkill(BattleContext battleContext, BattleTeamUnit teamUnit){
		if (teamUnit == null){
			return;
		}
		Battle battle = battleContext.getBattle();
		BattleOperateManager operateManager = battle.getOperateManager();
		for (int index = 0; index < BattleConstant.BATTLE_MAX_INDEX; index++) {
			BattleUnit battleUnit = teamUnit.getIndexBattleUnit(index);
			if (battleUnit == null) {
				continue;
			}
			OperateSkills operateSkills = operateManager.getOperate(battleUnit.getUserId());
			OperateSkill operateSkill = operateSkills == null ? null : operateSkills.getOperate(battleUnit.getId());
			if (operateSkill == null){
				operateSkill = BattleAIService.getInstance().randomOperate(battleContext, battleUnit);
			}
			OperateContext operateContext = new OperateContext(battleUnit, operateSkill);
			BuffUtil.foreachBuffFeature(battleUnit, feature -> feature.resetBattleOperate(battleContext, operateContext));
			operateSkill = operateContext.getFinalOperate();

			BattleSkill battleSkill = battleUnit.getSkill(operateSkill.getUseSkillId());
			if (battleSkill == null || battleSkill.isPassiveSkill()){
				return;
			}
			if (!BattleConstant.SELECT_SKILL_INDEX.contains(battleSkill.getIndex())) {
				return;
			}
			BattleUnit targetUnit = battle.getBattleUnitManager().getIdBattleUnit(operateSkill.getTargetId());
			SkillHandler.castActiveSkill(battleContext, battleUnit, battleSkill, targetUnit, castContext -> castContext.setAutoNormalSkill(true).setCanChangeTargetUnit(true));
		}
		onMyTeamUnitOperation(battleContext, teamUnit);
		BattleTeamUnit enemyTeamUnit = BattleUtil.getEnemyTeamUnit(battle, teamUnit);
		onEnemyTeamUnitOperation(battleContext, enemyTeamUnit);
	}

	protected void onMyTeamUnitOperation(BattleContext battleContext, BattleTeamUnit myTeamUnit){
		BattleUtil.foreachBattleUnit(myTeamUnit, BattleUnit::isAlive, battleUnit -> {
			BuffUtil.decBattleUnitBuffRound(battleContext, battleUnit, BuffDecPoint.MyEndRound);
		});
	}

	protected void onEnemyTeamUnitOperation(BattleContext battleContext, BattleTeamUnit enemyTeamUnit){
		BattleUtil.foreachBattleUnit(enemyTeamUnit, BattleUnit::isAlive, battleUnit -> {
			BuffUtil.decBattleUnitBuffRound(battleContext, battleUnit, BuffDecPoint.EnemyEndRound);
		});
	}
}
