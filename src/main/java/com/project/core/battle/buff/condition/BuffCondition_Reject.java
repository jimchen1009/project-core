package com.project.core.battle.buff.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffConfig;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.BuffTypeConfig;
import com.project.core.battle.buff.BuffUtil;

public class BuffCondition_Reject extends BuffCondition {

	@Override
	protected boolean isAddBuffLimit(BattleContext context, BuffContext buffContext) {
		BuffConfig addBuffConfig = buffContext.getBuff().getBuffConfig();

		Buff okBuff = BuffUtil.predicateBuffUtilOneOk(buffContext.getBattleUnit(), buff -> {
			BuffConfig buffConfig = buff.getBuffConfig();
			BuffTypeConfig typeConfig = BuffTypeConfig.getConfig(buffConfig.getType());
			return typeConfig.getRejectTypes().contains(addBuffConfig.getType());
		});
		return okBuff != null;
	}
}
