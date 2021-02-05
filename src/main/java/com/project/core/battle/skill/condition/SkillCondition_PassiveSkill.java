package com.project.core.battle.skill.condition;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.SkillConfig;
import com.project.core.battle.skill.SkillEffectConfig;
import com.project.core.battle.skill.effect.SkillEffectType;

import java.util.Collection;

public class SkillCondition_PassiveSkill extends SkillCondition {


	@Override
	protected boolean isSkillLimit0(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		SkillConfig skillConfig = battleSkill.getSkillConfig();
		if (!skillConfig.isPassiveSkill()){
			return false;
		}
		Collection<SkillEffectConfig> effectConfigs = skillConfig.getEffectConfigs(SkillEffectType.NotCastThisPassiveSkill);
		if (effectConfigs.isEmpty()) {
			return false;
		}
		for (SkillEffectConfig effectConfig : effectConfigs) {
			if (effectConfig.checkCanDoEffect(context, conditionContext, requestUnit, targetUnit)) {
				return true;
			}
		}
		return false;
	}
}
