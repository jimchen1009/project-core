package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BattleControl_Node extends BattleControl implements IBattleControlNode {

	private static final Logger logger = LoggerFactory.getLogger(BattleControl_Node.class);

	private final BattleStage battleStage;

	public BattleControl_Node(BattleControlId battleControlId, BattleStage battleStage) {
		super(battleControlId);
		this.battleStage = battleStage;
	}

	@Override
	public final BattleStage getBattleStage() {
		return battleStage;
	}

	@Override
	public void onInitialization(BattleContext battleContext) {
		super.onInitialization(battleContext);
		Battle battle = battleContext.getBattle();
		battle.setBattleStage(battleStage);
		battle.getControlManager().setCtrlInitializeDate(battleContext.getCurrent());
	}

	@Override
	protected ResultCode executeCondition(BattleContext battleContext) {
		if (!battleContext.getBattle().isBattleStage(this.battleStage)) {
			return ResultCode.BATTLE_STAGE_ERROR;
		}
		return ResultCode.SUCCESS;
	}

	@Override
	protected final ResultCode execute0(BattleContext battleContext) {
		executeNode(battleContext);
		System.out.println("阶段[" + battleStage.getComment() + "] 执行");
		return ResultCode.SUCCESS;
	}

	protected abstract void executeNode(BattleContext battleContext);

	@Override
	protected final ResultCode skip0(BattleContext battleContext) {
		boolean success = skipNode(battleContext);
		if (success){
			return ResultCode.SUCCESS;
		}
		else {
			return ResultCode.FAILURE;
		}
	}

	protected abstract boolean skipNode(BattleContext battleContext);

	@Override
	protected final void interrupt0(BattleContext battleContext, BattleInterrupt battleInterrupt) {
		interruptNode(battleContext, battleInterrupt);
	}

	protected void interruptNode(BattleContext battleContext, BattleInterrupt battleInterrupt){

	}
}
