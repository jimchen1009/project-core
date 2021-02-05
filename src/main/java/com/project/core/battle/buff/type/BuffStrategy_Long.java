package com.project.core.battle.buff.type;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;

import java.util.List;
import java.util.stream.Collectors;

public class BuffStrategy_Long extends BuffStrategyBase {

	@Override
	protected boolean doAddBuff(BattleContext battleContext, BuffContainer.Container typeContainer, BuffContext buffContext) {
		List<Buff> removeBuffList = typeContainer.getBuffList().stream()
				.filter(buff -> buff.getRemainRound() < buffContext.getBuff().getRemainRound())
				.collect(Collectors.toList());
		for (Buff removeBuff : removeBuffList) {
			typeContainer.removeBuff(removeBuff);
			doRemoveBuffActionAndFeature(battleContext, buffContext.changeBuff(removeBuff));
		}
		if (!typeContainer.emptyBuffList()) {
			return false;
		}
		typeContainer.addBuff(buffContext.getBuff());
		doAddBuffActionAndFeature(battleContext, buffContext);
		return true;
	}
}
