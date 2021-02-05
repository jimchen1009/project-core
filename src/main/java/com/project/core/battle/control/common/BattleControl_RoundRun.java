package com.project.core.battle.control.common;

import com.game.common.util.ResultCode;
import com.game.common.util.TupleCode;
import com.project.core.battle.Battle;
import com.project.core.battle.BattleConstant;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.BattleTeamType;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleUnitManager;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.ai.BattleAIService;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControl_Command;
import com.project.core.battle.operate.BattleOperate;
import com.project.core.battle.operate.BattleOperateManager;
import com.project.core.battle.operate.BattleOperates;
import com.project.core.battle.operate.OperateContext;
import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.SkillHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleControl_RoundRun extends BattleControl_Command<BattleOperates, BattleOperates> {

	private static final Logger logger = LoggerFactory.getLogger(BattleControl_RoundRun.class);

	protected final int teamUnitIndex;

	public BattleControl_RoundRun(BattleControlId battleControlId, int teamUnitIndex) {
		super(battleControlId, BattleStage.RoundRunTeam.crateChild(teamUnitIndex));
		this.teamUnitIndex = teamUnitIndex;
	}

	@Override
	protected TupleCode<BattleOperates> checkCondition0(BattleContext battleContext, BattleOperates requestCommand) {
		Battle battle = battleContext.getBattle();
		BattleOperates operates = battle.getOperateManager().getOperates(requestCommand.getUserId());
		if (operates != null){
			return new TupleCode<>(ResultCode.BATTLE_CONTROL_REQUEST);
		}
		BattleUnitManager battleUnitManager = battle.getBattleUnitManager();
		List<BattleOperate> operateList = new ArrayList<>();
		for (BattleOperate operate : requestCommand.getOperateList()) {
			BattleUnit battleUnit = battleUnitManager.getIdBattleUnit(operate.getOperatorId());
			if (battleUnit == null || battleUnit.getTeamUnit().getTeamUnitIndex() != teamUnitIndex){
				continue;
			}
			if (battleUnit.getUserId() != operate.getOperatorId()) {
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
	protected final void execute1(BattleContext battleContext, BattleOperates executeCommand) {
		Battle battle = battleContext.getBattle();
		BattleOperateManager operateManager = battle.getOperateManager();
		operateManager.addOperate(executeCommand);
		if (!isAllBattleUnitOperated(battleContext)){
			return;
		}
		executeRunRound(battleContext);
		battleContext.setExecuteCompleted(true);
	}

	protected abstract void executeRunRound(BattleContext battleContext);


	protected void castOperationSkill(BattleContext battleContext, int teamUnitIndex){
		Battle battle = battleContext.getBattle();
		BattleTeamUnit teamUnit = battle.getBattleUnitManager().getIndexBattleTeamUnit(teamUnitIndex);
		if (teamUnit == null){
			return;
		}
		BattleOperateManager operateManager = battle.getOperateManager();
		for (int index = 0; index < BattleConstant.BATTLE_MAX_INDEX; index++) {
			BattleUnit battleUnit = teamUnit.getIndexBattleUnit(index);
			if (battleUnit == null) {
				continue;
			}
			BattleOperate battleOperates = operateManager.getOperate(battleUnit.getId());
			if (battleOperates == null){
				battleOperates = BattleAIService.getInstance().randomOperate(battleContext, battleUnit);
			}
			OperateContext operateContext = new OperateContext(battleUnit, battleOperates);
			BuffUtil.foreachBuffFeature(battleUnit, feature -> feature.resetBattleOperate(battleContext, operateContext));
			battleOperates = operateContext.getFinalOperate();

			BattleSkill battleSkill = battleUnit.getSkill(battleOperates.getUseSkillId());
			if (battleSkill == null || battleSkill.isPassiveSkill()){
				return;
			}
			if (!BattleConstant.SELECT_SKILL_INDEX.contains(battleSkill.getIndex())) {
				return;
			}
			BattleUnit targetUnit = battle.getBattleUnitManager().getIdBattleUnit(battleOperates.getTargetId());
			SkillHandler.castActiveSkill(battleContext, battleUnit, battleSkill, targetUnit, castContext -> castContext.setAutoNormalSkill(true).setCanChangeTargetUnit(true));
		}

		if (teamUnit.getTeamType().equals(BattleTeamType.TeamA)) {
			onTeamAUnitOperation(battleContext, teamUnit);
		}
		else if (teamUnit.getTeamType().equals(BattleTeamType.TeamB)){
			onTeamBUnitOperation(battleContext, teamUnit);
		}
	}

	protected void onTeamAUnitOperation(BattleContext battleContext, BattleTeamUnit teamUnit){
		BattleUtil.foreachBattleUnit(teamUnit, BattleUnit::isAlive, battleUnit -> {
			BuffUtil.decBattleUnitBuffRound(battleContext, battleUnit, BuffDecPoint.MyEndRound);
		});
	}

	protected void onTeamBUnitOperation(BattleContext battleContext, BattleTeamUnit teamUnit){
		BattleUtil.foreachBattleUnit(teamUnit, BattleUnit::isAlive, battleUnit -> {
			BuffUtil.decBattleUnitBuffRound(battleContext, battleUnit, BuffDecPoint.EnemyEndRound);
		});
	}

	private boolean isAllBattleUnitOperated(BattleContext battleContext){
		Battle battle = battleContext.getBattle();
		BattleTeamUnit battleTeamUnit = battle.getBattleUnitManager().getIndexBattleTeamUnit(teamUnitIndex);
		BattleOperateManager operateManager = battle.getOperateManager();
		List<BattleUnit> battleUnitList = battleTeamUnit.getBattleUnitList();
		for (BattleUnit battleUnit : battleUnitList) {
			if (battleUnit.isDead() || battleUnit.getUserId() <= 0) {
				continue;
			}
			BattleOperates battleOperates = operateManager.getOperates(battleUnit.getUserId());
			if (battleOperates == null){
				return false;
			}
		}
		return true;
	}
}
