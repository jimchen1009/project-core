package com.project.core.battle.skill.effect;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.BuffHandler;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;

import java.util.ArrayList;
import java.util.List;

public class SkillEffectHandler_ChangeBuffDuration extends SkillEffectHandler {

	private int changeValue;
	private int buffGenera;

	@Override
	protected void initParam(String param) {
		String[] strings = param.split("#");
		changeValue = Integer.parseInt(strings[0]);
		buffGenera = Integer.parseInt(strings[1]);
	}


	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		if (changeValue == 0) {
			return false;
		}
		List<BuffContext> buffContextList = new ArrayList<>();
		BuffUtil.foreachBuff(targetUnit, conditionContext, buff -> {
			if (buff.buffGenera() != buffGenera) {
				return;
			}
			buffContextList.add(new BuffContext(requestUnit, targetUnit, buff, true));
		});
		BuffUtil.changeBattleUnitBuffRound(battleContext, buffContextList, changeValue);
		return !buffContextList.isEmpty();
	}
}
