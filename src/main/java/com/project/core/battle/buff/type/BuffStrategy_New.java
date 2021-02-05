package com.project.core.battle.buff.type;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;

import java.util.List;

public class BuffStrategy_New extends BuffStrategyBase {

	@Override
	protected boolean doAddBuff(BattleContext battleContext, BuffContainer.Container typeContainer, BuffContext buffContext) {
		List<Buff> removeBuffList = typeContainer.getBuffList();
		for (Buff removeBuff : removeBuffList) {
			typeContainer.removeBuff(removeBuff);
			doRemoveBuffActionAndFeature(battleContext, buffContext.changeBuff(removeBuff));
		}
		typeContainer.addBuff(buffContext.getBuff());
		doAddBuffActionAndFeature(battleContext, buffContext);
		return true;
	}
}
