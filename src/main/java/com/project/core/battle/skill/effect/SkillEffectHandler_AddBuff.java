package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.BuffHandler;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.model.BattleBuffData;
import com.project.core.battle.skill.BattleSkill;

public class SkillEffectHandler_AddBuff extends SkillEffectHandler {

	private int buffId;
	private int round;

	@Override
	protected void initParam(String param) {
		String[] strings = param.split("#");
		this.buffId = Integer.parseInt(strings[0]);
		this.round = strings.length > 1 ? Integer.parseInt(strings[1]) : 0;
	}

	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		Buff buff = BuffUtil.createBuff(battleContext.getBattle(), requestUnit, new BattleBuffData(buffId, round));
		if (buff == null){
			return false;
		}
		BuffHandler.checkAllAndAddBuff(battleContext, new BuffContext(targetUnit, buff, true));
		return true;
	}
}
