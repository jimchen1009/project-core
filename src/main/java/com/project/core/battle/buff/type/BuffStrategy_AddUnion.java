package com.project.core.battle.buff.type;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;

public class BuffStrategy_AddUnion extends BuffStrategyBase {

	@Override
	protected boolean doAddBuff(BattleContext battleContext, BuffContainer.Container typeContainer, BuffContext buffContext) {
		typeContainer.addBuff(buffContext.getBuff());
		doAddBuffActionAndFeature(battleContext, buffContext);
		return true;
	}
}
