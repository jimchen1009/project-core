package com.project.core.battle.buff.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.BuffContext;

import java.util.Objects;

public abstract class BuffCondition implements IBuffCondition {

	@Override
	public boolean isLimit(BattleContext context, BuffContext buffContext) {
		boolean conditionLimit = isAddBuffLimit(context, Objects.requireNonNull(buffContext));
		if (conditionLimit){
			onConditionLimit(context, buffContext);
		}
		return conditionLimit;
	}

	/**
	 * BUFF是否被限制了
	 * @param context
	 * @param buffContext
	 * @return
	 */
	protected abstract boolean isAddBuffLimit(BattleContext context, BuffContext buffContext);

	/**
	 * BUFF被该条件限制后通知接口
	 * @param context
	 * @param buffContext
	 */
	protected void onConditionLimit(BattleContext context, BuffContext buffContext){

	}
}
