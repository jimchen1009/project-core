package com.project.core.battle.buff.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.BuffUtil;

public class BuffCondition_Feature extends BuffCondition {

	@Override
	protected boolean isAddBuffLimit(BattleContext context, BuffContext buffContext) {
		return BuffUtil.predicateFeatureUtilOneOkBool(buffContext.getBattleUnit(), feature -> feature.isLimitAddBuff(context, buffContext));
	}
}
