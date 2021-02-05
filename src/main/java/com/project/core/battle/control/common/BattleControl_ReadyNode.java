package com.project.core.battle.control.common;

import com.game.common.util.ResultCode;
import com.game.common.util.TupleCode;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.BattleControlId;
import com.project.core.battle.control.BattleControl_Command;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 所有玩家: 需要准备节点
 */
public abstract class BattleControl_ReadyNode extends BattleControl_Command<Object, Long> {

	private final long delayAiDuration;

	public BattleControl_ReadyNode(BattleControlId battleControlId, BattleStage battleStage) {
		this(battleControlId, battleStage, 0, TimeUnit.MILLISECONDS);
	}

	public BattleControl_ReadyNode(BattleControlId battleControlId, BattleStage battleStage, int AiDuration, TimeUnit timeUnit) {
		super(battleControlId, battleStage);
		this.delayAiDuration = timeUnit.toMillis(AiDuration);
	}

	@Override
	protected void onSkipSuccess(BattleContext battleContext) {

	}

	@Override
	protected final TupleCode<Long> getExecuteCommand(BattleContext battleContext, Object requestCommand) {
		return new TupleCode<>(System.currentTimeMillis());
	}

	@Override
	protected final TupleCode<Long> getExecuteAICommand(BattleContext battleContext) {
		Date initializeDate = battleContext.getBattle().getControlManager().getCtrlInitializeDate();
		long currentTime = battleContext.getCurrent().getTime();
		if (currentTime - initializeDate.getTime() < delayAiDuration) {
			return new TupleCode<>(ResultCode.BATTLE_CONTROL_DELAY);
		}
		return new TupleCode<>(currentTime);
	}

	@Override
	protected final boolean executeCommand(BattleContext battleContext) {
		Collection<Long> allUserIds = battleContext.getBattle().getBattleUnitManager().getAllUserIds();
		for (Long userId : allUserIds) {
			if (!battleContext.getBattle().getOperateManager().containerOperate(userId)) {
				return false;
			}
		}
		executeReadyCommand(battleContext);
		return true;
	}

	protected void executeReadyCommand(BattleContext battleContext){

	}

}
