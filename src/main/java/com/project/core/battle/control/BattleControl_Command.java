package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.game.common.util.TupleCode;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.common.BattleControl_BattleNode;

public abstract class BattleControl_Command<T, E> extends BattleControl_BattleNode {

	public BattleControl_Command(BattleControlId battleControlId, BattleStage battleStage) {
		super(battleControlId, battleStage);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final ResultCode checkCondition(BattleContext battleContext) {
		ResultCode resultCode = super.checkCondition(battleContext);
		if (!resultCode.isSuccess()){
			return resultCode;
		}
		T requestCommand = (T)battleContext.getRequestCommand();
		if (requestCommand == null) {
			return ResultCode.BATTLE_PARAM_SUPPORT;
		}
		TupleCode<E> tupleCode = checkCondition0(battleContext, requestCommand);
		if (tupleCode.isSuccess()) {
			battleContext.setExecuteCommand(tupleCode.getData());
		}
		return tupleCode.getCode();
	}

	protected abstract TupleCode<E> checkCondition0(BattleContext battleContext, T requestCommand) ;

	@SuppressWarnings("unchecked")
	@Override
	protected final void execute1(BattleContext battleContext) {
		execute1(battleContext, (E)battleContext.getExecuteCommand());
	}

	protected abstract void execute1(BattleContext battleContext, E executeCommand);
}
