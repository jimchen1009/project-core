package com.project.core.battle.buff.type;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;

public class BuffStrategy_AddBehind extends BuffStrategyBase {

	@Override
	protected boolean doAddBuff(BattleContext battleContext, BuffContainer.Container typeContainer, BuffContext buffContext) {
		if (typeContainer.emptyBuffList()) {
			typeContainer.addBuff(buffContext.getBuff());
			doAddBuffActionAndFeature(battleContext, buffContext);
		}
		else {
			typeContainer.incOtherBuffRound(buffContext.getBuff());
			doAddBuffActionOnly(battleContext, buffContext);
		}
		return true;
	}

	@Override
	protected void onDirectRemoveBuffEvent(BattleContext battleContext, BattleUnit battleUnit, BuffContainer.Container typeContainer) {
		super.onDirectRemoveBuffEvent(battleContext, battleUnit, typeContainer);
		Buff buff = typeContainer.popOtherToBuffList();
		if (buff == null){
			return;
		}
		doAddBuffFeatureOnly(battleContext, new BuffContext(buff, true));
	}
}
