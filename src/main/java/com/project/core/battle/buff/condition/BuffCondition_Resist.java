package com.project.core.battle.buff.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.BuffContext;

public class BuffCondition_Resist extends BuffCondition {

	@Override
	protected boolean isAddBuffLimit(BattleContext context, BuffContext buffContext) {
		return false;
	}
}
