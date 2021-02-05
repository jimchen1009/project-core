package com.project.core.battle.skill;

import com.game.common.util.DecideBool;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionContext;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.result.ActionType;
import com.project.core.battle.result.ActorAction;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.effect.ISkillEffectHandler;
import com.project.core.battle.skill.effect.SkillEffectType;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SkillEffectUtil {

	/**
	 *
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param skillOccasion
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doOneOccasionSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, SkillOccasion skillOccasion,
												IConditionContext conditionContext, ISkillEffectContext effectContext) {
		doOneOccasionSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, skillOccasion, conditionContext, effectContext, null);
	}
	/**
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param skillOccasion
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doOneOccasionSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, SkillOccasion skillOccasion,
												IConditionContext conditionContext, ISkillEffectContext effectContext, Consumer<SkillOccasionRequest> consumer) {
		SkillOccasionRequest request = new SkillOccasionRequest(requestUnit, battleSkill, targetUnit, skillOccasion);
		if (consumer != null){
			consumer.accept(request);
		}
		doOneBaseSkillEffect(battleContext, request, conditionContext, effectContext);
	}

	/**
	 *
	 * @param battleContext
	 * @param requestUnit
	 * @param targetUnit
	 * @param skillOccasion
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doAllOccasionPassiveSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleUnit targetUnit, SkillOccasion skillOccasion, IConditionContext conditionContext,
													   ISkillEffectContext effectContext) {
		doAllOccasionPassiveSkillEffect(battleContext, requestUnit, targetUnit, skillOccasion, conditionContext, effectContext, null);
	}
	/**
	 * @param battleContext
	 * @param requestUnit
	 * @param targetUnit
	 * @param skillOccasion
	 * @param conditionContext
	 * @param effectContext
	 * @param consumer
	 */
	public static void doAllOccasionPassiveSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleUnit targetUnit, SkillOccasion skillOccasion, IConditionContext conditionContext,
													   ISkillEffectContext effectContext, Consumer<SkillOccasionRequest> consumer) {
		if (!requestUnit.isAlive()) {
			return;
		}
		if (conditionContext == null) {
			conditionContext = ConditionContext.DEFAULT;
		}
		for (BattleSkill battleSkill : requestUnit.getSkillList()) {
			if (!battleSkill.isPassiveSkill()) {
				continue;
			}
			if (!SkillUtil.isConditionLimit(battleContext, conditionContext, requestUnit, battleSkill, targetUnit)) {
				continue;
			}
			doOneOccasionSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, skillOccasion, conditionContext, effectContext, request -> {
				request.setCheckSkillCD(true);
				if (consumer == null){
					return;
				}
				consumer.accept(request);
			});
		}
	}

	/**
	 *
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param effectType
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doEffectOneSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, SkillEffectType effectType, IConditionContext conditionContext,
												   ISkillEffectContext effectContext) {
		doEffectOneSkillEffect(battleContext, requestUnit,battleSkill, targetUnit, effectType, conditionContext, effectContext, null);
	}

	/**
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param effectType
	 * @param conditionContext
	 * @param effectContext
	 * @param consumer
	 */
	public static void doEffectOneSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, SkillEffectType effectType, IConditionContext conditionContext,
											  ISkillEffectContext effectContext, Consumer<SkillEffectRequest> consumer) {
		SkillEffectRequest request = new SkillEffectRequest(requestUnit, battleSkill, targetUnit, effectType);
		if (consumer != null){
			consumer.accept(request);
		}
		doOneBaseSkillEffect(battleContext, request, conditionContext, effectContext);
	}

	/**
	 *
	 * @param battleContext
	 * @param requestUnit
	 * @param targetUnit
	 * @param effectType
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doEffectAllPassiveSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleUnit targetUnit, SkillEffectType effectType, IConditionContext conditionContext,
													 ISkillEffectContext effectContext) {
		doEffectAllPassiveSkillEffect(battleContext, requestUnit, targetUnit, effectType, conditionContext, effectContext, null);
	}

	/**
	 * @param battleContext
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doEffectAllPassiveSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleUnit targetUnit, SkillEffectType effectType, IConditionContext conditionContext,
													 ISkillEffectContext effectContext, Consumer<SkillEffectRequest> consumer) {
		for (BattleSkill battleSkill : requestUnit.getSkillList()) {
			if (!battleSkill.isPassiveSkill()) {
				continue;
			}
			if (!SkillUtil.isConditionLimit(battleContext, conditionContext, requestUnit, battleSkill, targetUnit)) {
				continue;
			}
			doEffectOneSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, effectType, conditionContext, effectContext, request -> {
				request.setCheckSkillCD(true);
				if (consumer == null){
					return;
				}
				consumer.accept(request);
			});
		}
	}

	/**
	 * @param battleContext
	 * @param request
	 * @param conditionContext
	 * @param effectContext
	 */
	public static void doOneBaseSkillEffect(BattleContext battleContext, SkillBaseRequest<?> request, IConditionContext conditionContext, ISkillEffectContext effectContext) {

		BattleSkill battleSkill = request.getBattleSkill();
		SkillConfig skillConfig = battleSkill.getSkillConfig();
		List<SkillEffectConfig> effectConfigs = skillConfig.getEffectConfigs();


		DecideBool resetSkillCd = new DecideBool(false);
		DecideBool doSkillEffect = new DecideBool(false);

		if (conditionContext == null){
			conditionContext = ConditionContext.DEFAULT;
		}

		BattleUnit requestUnit = request.getRequestUnit();
		List<Map.Entry<SkillEffectUnitConfig, BattleUnit>> entryList = new ArrayList<>();
		for (SkillEffectConfig effectConfig : effectConfigs) {
			Collection<SkillEffectUnitConfig> effectUnitConfigs = request.getEffectUnitConfigs(effectConfig);
			if (effectUnitConfigs.isEmpty()) {
				continue;
			}
			if (!resetSkillCd.getCurrentValue() && request.isCheckSkillCD() && !effectConfig.isIgnoreSkillCD() && battleSkill.getCD() > 0) {
				continue;
			}
			List<BattleUnit> battleUnitList = effectConfig.getBattleUnitSelector().selectBattleUnits(battleContext, conditionContext, requestUnit, request.getBattleSkill(), request.getTargetUnit());
			for (BattleUnit battleUnit : battleUnitList) {
				if (!effectConfig.checkCanDoEffect(battleContext, conditionContext, requestUnit, battleUnit)) {
					continue;
				}
				for (SkillEffectUnitConfig effectUnitConfig : effectUnitConfigs) {
					if (request.needResetSkillCD() && !effectConfig.isIgnoreSkillCD() && resetSkillCd.decideValue(true)) {
						battleSkill.resetCD();
					}
					if (doSkillEffect.decideValue(true) && request.isDoActorAction()) {
						if (skillConfig.isDoAction()){
							battleContext.confirmActorSkill(requestUnit, battleSkill, battleUnit);
						}
						else {
							battleContext.addBattleAction(new ActorAction(requestUnit, battleUnit, ActionType.Effect).setActionId(battleSkill.getSkillId()));
						}
					}
					if (!effectUnitConfig.getEffectHandler().doEffect(battleContext, requestUnit, battleSkill, battleUnit, conditionContext, effectContext)){
						continue;
					}
					Map.Entry<SkillEffectUnitConfig, BattleUnit> entry = new AbstractMap.SimpleEntry<>(effectUnitConfig, battleUnit);
					entryList.add(entry);
				}
			}
		}
		if (entryList.isEmpty()) {
			return;
		}
		for (Map.Entry<SkillEffectUnitConfig, BattleUnit> entry : entryList) {
			ISkillEffectHandler effectHandler = entry.getKey().getEffectHandler();
			effectHandler.onSkillSuccess(battleContext, requestUnit, battleSkill, entry.getValue(), conditionContext, effectContext);
		}
	}
}
