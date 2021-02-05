package com.project.core.battle.skill;

import com.game.common.util.CommonUtil;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleTeamUnit;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.battle.condition.ConditionContext;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.damage.DamageType;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.effect.SkillEffectContext;
import com.project.core.battle.skill.effect.SkillEffectType;
import com.project.core.battle.skill.heal.SkillHealUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillEffectContextUtil {

	public static List<SkillDamageUnit> doSkillDamageUnit(BattleContext battleContext, BattleSkill battleSkill, SkillEffectContext finalEffectContext){
		List<SkillDamageUnit> skillDamageUnitList = new ArrayList<>();
		Collection<SkillDamageUnit> skillDamageUnits = finalEffectContext.getAllSkillDamageUnits();
		for (SkillDamageUnit skillDamageUnit : skillDamageUnits) {
			skillDamageUnit.doDamage(battleContext, new ConditionContext(), new SkillEffectContext());
		}
		return skillDamageUnitList;
	}

	public static void onAllHealUnitSuccess(BattleContext battleContext, SkillEffectContext finalEffectContext){
		Collection<SkillHealUnit> skillHealUnits = finalEffectContext.getAllSkillHealUnits();
		for (SkillHealUnit skillHealUnit : skillHealUnits) {
			skillHealUnit.onHealSuccess(battleContext, new ConditionContext(), new SkillEffectContext());
		}
	}


	public static void onDamageUnitAfterSkill(BattleContext battleContext, BattleUnit requestUnit, List<SkillContext> skillContextList, SkillEffectContext finalEffectContext){
		for (SkillContext skillContext : skillContextList) {
			BattleUnit targetUnit = skillContext.getTargetUnit();
			if (!CommonUtil.findOneUtilOkayBool(skillContext.getDamageUnitList(), damageUnit -> damageUnit.hasDoDamage() && damageUnit.getDamageUnit().getId() == targetUnit.getId())) {
				continue;
			}
			BuffUtil.decBattleUnitBuffRound(battleContext, targetUnit, BuffDecPoint.BeAttack);

			IConditionContext conditionContext = skillContext.getConditionContext();
			ISkillEffectContext effectContext = skillContext.getSkillEffectContext();
			SkillEffectUtil.doAllOccasionPassiveSkillEffect(battleContext, targetUnit, requestUnit, SkillOccasion.DoSkillEffect, conditionContext, effectContext, request -> request.setConditionTypes(ConditionType.getTargetUsage()));
			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, targetUnit, requestUnit, SkillEffectType.CastSkill, conditionContext, effectContext, request -> request.setConditionTypes(ConditionType.getTargetUsage()));
			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, targetUnit, requestUnit, SkillEffectType.CastSkillId, conditionContext, effectContext, request -> request.setConditionTypes(ConditionType.getTargetUsage()));
		}

		for (SkillDamageUnit skillDamageUnit : finalEffectContext.getAllSkillDamageUnits()) {
			SkillEffectContext effectContext = new SkillEffectContext();
			ConditionContext conditionContext = new ConditionContext(effectContext);
			skillDamageUnit.onDamageSuccess(battleContext,  conditionContext, effectContext);
		}

		Map<Integer, BattleTeamUnit> attackCritTeamUnitMap = new HashMap<>();
		Map<Long, BattleUnit> beCritBattleUnitMap = new HashMap<>();
		for (SkillDamageUnit skillDamageUnit : finalEffectContext.getAllSkillDamageUnits()) {
			if (!skillDamageUnit.isCrit()) {
				continue;
			}
			if (!skillDamageUnit.getDamageUnit().getTeamType().equals(requestUnit.getTeamType())) {
				continue;
			}
			BattleTeamUnit teamUnit = skillDamageUnit.getAttackUnit().getTeamUnit();
			attackCritTeamUnitMap.put(teamUnit.getTeamUnitIndex(), teamUnit);
			BattleUnit damageUnit = skillDamageUnit.getDamageUnit();
			beCritBattleUnitMap.put(damageUnit.getId(), damageUnit);
		}

		for (BattleTeamUnit teamUnit : attackCritTeamUnitMap.values()) {
			BattleUtil.foreachBattleUnit(teamUnit, BattleUnit::isAlive, battleUnit -> {
				SkillHandler.doConditionAllSkill(battleContext, battleUnit,  requestUnit, ConditionType.MyTeamBeCrit, request -> request.setDoActorAction(true));
			});
		}
		for (BattleTeamUnit teamUnit : attackCritTeamUnitMap.values()) {
			BattleUtil.foreachBattleUnit(teamUnit, BattleUnit::isAlive, battleUnit -> {
				for (BattleUnit beCritBattleUnit : beCritBattleUnitMap.values()) {
					SkillHandler.doConditionAllSkill(battleContext, battleUnit,  beCritBattleUnit, ConditionType.TeamMateBeCrit, request -> request.setDoActorAction(true));
				}
			});
		}
	}
}
