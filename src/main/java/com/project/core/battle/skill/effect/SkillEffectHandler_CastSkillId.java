package com.project.core.battle.skill.effect;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.model.BattleSkillData;
import com.project.core.battle.result.ActionType;
import com.project.core.battle.result.ActorAction;
import com.project.core.battle.selector.BattleUnitSelectorType;
import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.SkillConfig;
import com.project.core.battle.skill.SkillHandler;

public class SkillEffectHandler_CastSkillId extends SkillEffectHandler {

	private int skillId;
	private boolean canCastSkillAgain;
	private BattleUnitSelectorType castSelectorType;
	private boolean canChangeTargetUnit;
	private BattleUnitSelectorType targetSelectorType;

	@Override
	protected void initParam(String param) {
		String[] strings = param.split("#");
		this.skillId = Integer.parseInt(strings[0]);
		this.canCastSkillAgain = strings.length <= 1 || (Integer.parseInt(strings[1]) != 0);
		int castSelectorTypeId = strings.length > 2 ? Integer.parseInt(strings[2]) : 0;
		this.castSelectorType = IEnumBase.findOne(BattleUnitSelectorType.values(), castSelectorTypeId);
		this.canChangeTargetUnit = strings.length <= 4 || (Integer.parseInt(strings[4]) != 0);
		int targetSelectorTypeId = strings.length > 6 ? Integer.parseInt(strings[6]) : 0;
		this.targetSelectorType = IEnumBase.findOne(BattleUnitSelectorType.values(), targetSelectorTypeId);
	}

	@Override
	public boolean doEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		SkillConfig skillConfig = SkillConfig.getConfig(skillId);
		if (skillConfig == null) {
			return false;
		}
		BattleUnit requestUnit0 = this.castSelectorType.redirectSelectBattleUnit(battleContext, conditionContext, targetUnit, battleSkill, targetUnit);
		BattleSkill castBattleSkill = new BattleSkill(new BattleSkillData(-1, skillId, 0));

		BattleUnit targetUnit0 = targetSelectorType.redirectSelectBattleUnit(battleContext, conditionContext, requestUnit0, castBattleSkill, targetUnit);

		battleContext.addBattleAction(new ActorAction(requestUnit0, targetUnit0, ActionType.Skill).setActionId(skillId));
		SkillHandler.castActiveSkill(battleContext, requestUnit0, castBattleSkill, targetUnit0, castContext -> {
			castContext.setCheckSkillCD(false).setCastByOtherSkillId(battleSkill.getSkillId()).setCanChangeTargetUnit(canChangeTargetUnit).setCanCastSkillAgain(canCastSkillAgain);
		});
		return true;
	}
}
