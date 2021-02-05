package com.project.core.battle.buff.condition;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.buff.BuffContext;

public enum BuffConditionType implements IEnumBase, IBuffCondition {

	Reject(1, "配置拒绝类型", new BuffCondition_Reject()),
	Resist(2, "技能效果抵抗", new BuffCondition_Resist()),
	Feature(3, "BUFF特性御制", new BuffCondition_Feature()),
	;

	public final int id;
	public final IBuffCondition handler;

	BuffConditionType(int id, String comment, IBuffCondition handler) {
		this.id = id;
		this.handler = handler;
	}

	@Override
	public int getId() {
		return id;
	}


	@Override
	public boolean isLimit(BattleContext context, BuffContext buffContext) {
		return handler.isLimit(context, buffContext);
	}
}
