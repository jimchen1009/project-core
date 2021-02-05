package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.game.common.util.TupleCode;
import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.common.BattleControl_BattleNode;

public abstract class BattleControl_Command<T, E> extends BattleControl_BattleNode {

	public BattleControl_Command(BattleControlId battleControlId, BattleStage battleStage) {
		super(battleControlId, battleStage);
	}


	@Override
	protected final ResultCode executeCondition(BattleContext battleContext) {
		ResultCode resultCode = super.executeCondition(battleContext);
		if (!resultCode.isSuccess()){
			return resultCode;
		}
		Battle battle = battleContext.getBattle();
		long requestUserId = battleContext.getOperateUserId();
		if (battle.getOperateManager().containerOperate(requestUserId)){
			return ResultCode.BATTLE_CONTROL_REQUEST;
		}
		TupleCode<E> tupleCode;
		if (!battleContext.isExecuteAI()) {
			T requestCommand = battleContext.getRequestCommand();
			if (requestCommand == null) {
				return ResultCode.BATTLE_PARAM_SUPPORT;
			}
			tupleCode = getExecuteCommand(battleContext, requestCommand);
		}
		else {
			tupleCode = getExecuteAICommand(battleContext);
		}
		if (!tupleCode.isSuccess()) {
			return tupleCode.getCode();
		}
		battleContext.setExecuteCommand(tupleCode.getData());
		return ResultCode.SUCCESS;
	}

	protected abstract TupleCode<E> getExecuteCommand(BattleContext battleContext, T requestCommand) ;

	protected abstract TupleCode<E> getExecuteAICommand(BattleContext battleContext);

	@SuppressWarnings("unchecked")
	@Override
	protected final void executeNode(BattleContext battleContext) {
		E executeCommand = (E) battleContext.getExecuteCommand();
		Battle battle = battleContext.getBattle();
		long requestUserId = battleContext.getOperateUserId();
		battle.getOperateManager().addOperate(requestUserId, executeCommand);
		if (executeCommand(battleContext)){
			battleContext.setExecuteCompleted(true);
			battleContext.getBattle().getOperateManager().removeAllOperates();
		}
	}

	protected abstract boolean executeCommand(BattleContext battleContext);
}
