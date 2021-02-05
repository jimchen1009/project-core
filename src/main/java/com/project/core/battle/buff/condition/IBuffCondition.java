package com.project.core.battle.buff.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.BuffContext;

public interface IBuffCondition {

	/**
	 * BUFF是否被限制了~
	 * @param context
	 * @return
	 */
	boolean isLimit(BattleContext context, BuffContext buffContext);
}
