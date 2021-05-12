package com.project.core.battle.skill;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleMath;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleUnitContext;
import com.project.core.battle.BattleUtil;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.ConditionContext;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.result.ActionType;
import com.project.core.battle.result.ActorActionSkill;
import com.project.core.battle.selector.IBattleUnitSelector;
import com.project.core.battle.skill.damage.DamageType;
import com.project.core.battle.skill.damage.SkillDamage;
import com.project.core.battle.skill.damage.SkillDamageRound;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.effect.SkillEffectContext;
import com.project.core.battle.skill.effect.SkillEffectHandler_Damage;
import com.project.core.battle.skill.effect.SkillEffectType;
import com.project.core.battle.skill.effect.combine.EffectContextCombine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class SkillHandler {

	/**
	 * @param battleContext
	 * @param requestUnit
	 * @param battleSkill
	 * @param targetUnit
	 * @param consumer
	 * @return
	 */
	public static SkillCastContext castActiveSkill(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit, Consumer<SkillCastContext> consumer) {
		if (battleSkill == null){
			return null;
		}
		if (!requestUnit.isAlive()) {
			return null;
		}
		if (!battleSkill.isActiveSkill()){
			return null;
		}
		if (BattleUtil.isAllDead(battleContext.getBattle())) {
			return null;
		}
		BattleUnitContext battleUnitContext = battleContext.getBattleUnitContext(requestUnit);
		SkillCastContext castContext = battleUnitContext.createSkillCastContext(requestUnit, battleSkill, targetUnit);
		castContext.setCastByOtherSkillId(0).setResetSkillCD(true).setCheckSkillCD(true).setCanCastSkillAgain(true).setDoActorAction(true);
		if (consumer != null){
			consumer.accept(castContext);
		}

		if (castContext.isAutoNormalSkill() && (battleSkill.getCD() > 0 || SkillUtil.isConditionLimit(battleContext, castContext.getConditionContext(), requestUnit, battleSkill, targetUnit))) {
			//主动不能释放, 换成普通攻击
			BattleSkill normalSkill = requestUnit.getNormalAttackSkill();
			if (normalSkill == null || normalSkill.getSkillId() == battleSkill.getSkillId()){
				return null;
			}
			return castActiveSkill(battleContext, requestUnit, normalSkill, targetUnit, consumer);
		}
		if (BuffUtil.predicateFeatureUtilOneOkBool(requestUnit, feature -> !feature.canCastSkill(battleContext, castContext.getConditionContext()))){
			return castContext;
		}

		SkillConfig skillConfig = castContext.getBattleSkill().getSkillConfig();

		if (castContext.isCanChangeTargetUnit()) {
			BuffUtil.foreachBuffFeature(requestUnit, feature -> feature.castResetTargetUnit(battleContext, castContext), castContext.getConditionContext());
			targetUnit = castContext.finalTargetBattleUnit();
		}
		targetUnit = skillConfig.getSelectUnitSelector().redirectSelectBattleUnit(battleContext, castContext.getConditionContext(), requestUnit, battleSkill, targetUnit);

		if (targetUnit == null){
			return null;
		}
		castContext.setTargetUnit(targetUnit);

		if (castContext.isDoActorAction() && skillConfig.isDoAction()) {
			battleContext.confirmActorSkill(requestUnit, battleSkill, targetUnit);
		}
		doCastActiveSkillEffect(battleContext, castContext);
		return castContext;
	}

	private static void doCastActiveSkillEffect(BattleContext battleContext, SkillCastContext castContext) {
		BattleUnit requestUnit = castContext.getRequestUnit();
		BattleSkill battleSkill = castContext.getBattleSkill();
		BattleUnit targetUnit = castContext.getTargetUnit();
		SkillConfig skillConfig = battleSkill.getSkillConfig();

		/***
		 * 技能前置伤害
		 */
		List<SkillContext> skillContextList = new ArrayList<>();
		if (skillConfig.getEffectConfigs(SkillEffectType.SkillDamage).isEmpty() && skillConfig.getDamageRate() > 0) {
			List<SkillContext> skillContextList0 = doCastActiveSkillDamage(battleContext, castContext, skillConfig.getSelectUnitSelector(), skillConfig.getDamageRate(), DamageType.Skill);
			skillContextList.addAll(skillContextList0);
		}

		/***
		 * 技能效果
		 */
		List<SkillEffectConfig> effectConfigs = skillConfig.getEffectConfigs();
		for (SkillEffectConfig effectConfig : effectConfigs) {
			Collection<SkillEffectUnitConfig> damageUnitConfigs = effectConfig.getEffectUnitConfigs(SkillEffectType.SkillDamage);
			for (SkillEffectUnitConfig effectUnitConfig : damageUnitConfigs) {
				SkillEffectHandler_Damage effectHandler = effectUnitConfig.getEffectHandler();
				int damageRate = effectHandler.getDamageRate();
				List<SkillContext> skillContextList1 = doCastActiveSkillDamage(battleContext, castContext, effectConfig.getBattleUnitSelector(), damageRate, DamageType.Skill);
				skillContextList.addAll(skillContextList1);
			}
		}

		SkillEffectContext noneEffectContext = new SkillEffectContext();
		SkillEffectContext myEffectContext = new SkillEffectContext();

		SkillEffectContext finalEffectContext = doCastActiveOccasionSkillEffect(battleContext, castContext, skillContextList, noneEffectContext, myEffectContext, SkillOccasion.DoSkillEffect);
		ConditionContext finalConditionContext = new ConditionContext(finalEffectContext);

		if (castContext.needResetSkillCD()) {
			SkillEffectUtil.doEffectOneSkillEffect(battleContext, targetUnit, battleSkill, requestUnit, SkillEffectType.SkillNoResetCD, finalConditionContext, finalEffectContext);
			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillEffectType.SkillNoResetCD, finalConditionContext, finalEffectContext);
			if (battleSkill.resetCD() > 0) {
				battleContext.addBattleAction(new ActorActionSkill(requestUnit, battleSkill, ActionType.SkillCD));
			}
		}
		for (SkillContext skillContext : skillContextList) {
			ConditionContext conditionContext = skillContext.getConditionContext();
			SkillEffectContext effectContext = skillContext.getSkillEffectContext();
			SkillEffectUtil.doOneOccasionSkillEffect(battleContext, requestUnit, battleSkill, skillContext.getTargetUnit(), SkillOccasion.DoSkillEffect, conditionContext, effectContext);
		}
		finalEffectContext = doCastActiveOccasionSkillEffect(battleContext, castContext, skillContextList, noneEffectContext, myEffectContext, SkillOccasion.AfterDoSkillEffect);
		finalConditionContext = new ConditionContext(finalEffectContext);

		SkillEffectContextUtil.doSkillDamageUnit(battleContext, battleSkill, finalEffectContext);


		/***
		 * 下面的完成技能之后的事情
		 */
		SkillEffectContextUtil.onDamageUnitAfterSkill(battleContext, requestUnit, skillContextList, finalEffectContext);
		SkillEffectContextUtil.onAllHealUnitSuccess(battleContext , finalEffectContext);
		SkillEffectUtil.doOneOccasionSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, SkillOccasion.DoSkillEffect, finalConditionContext, finalEffectContext, request -> request.setConditionType(ConditionType.UseSkill));
		SkillEffectUtil.doAllOccasionPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillOccasion.DoSkillEffect, finalConditionContext, finalEffectContext, request -> request.setConditionType(ConditionType.UseSkill));
		if (castContext.isCanCastSkillAgain() && requestUnit.isAlive()) {
			SkillEffectUtil.doEffectOneSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, SkillEffectType.CastSkill, finalConditionContext, finalEffectContext);
		}

		if (castContext.isCanCastSkillAgain() && requestUnit.isAlive()) {
			SkillEffectUtil.doEffectOneSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, SkillEffectType.CastSkillId, finalConditionContext, finalEffectContext);
		}

		if (castContext.isCanCastSkillAgain() && requestUnit.isAlive()) {
			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillEffectType.CastSkill, finalConditionContext, finalEffectContext);
		}

		if (castContext.isCanCastSkillAgain() && requestUnit.isAlive()) {
			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillEffectType.CastSkillId, finalConditionContext, finalEffectContext);
		}
	}

	private static List<SkillContext> doCastActiveSkillDamage(BattleContext battleContext, SkillCastContext castContext, IBattleUnitSelector battleUnitSelector, int normalDamageRate, DamageType damageType) {
		BattleSkill battleSkill = castContext.getBattleSkill();

		List<SkillContext> skillContextList = new ArrayList<>();
		List<BattleUnit> selectUnitList = battleUnitSelector.selectBattleUnits(battleContext, castContext.getConditionContext(), castContext.getRequestUnit(), battleSkill, castContext.getTargetUnit());
		for (BattleUnit targetUnit : selectUnitList) {
			if (targetUnit.isDead()) {
				continue;
			}
			SkillContext skillContext = castContext.createSkillContext(targetUnit);

			BattleUnit requestUnit = skillContext.getRequestUnit();
			SkillEffectContext effectContext = skillContext.getSkillEffectContext();
			ConditionContext conditionContext = skillContext.getConditionContext();

			SkillEffectUtil.doOneOccasionSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, SkillOccasion.CalcNormalDamage, conditionContext, effectContext);
			SkillEffectUtil.doAllOccasionPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillOccasion.CalcNormalDamage, conditionContext, effectContext);

			int normalDamage = calculateNormalDamage(battleContext, skillContext, normalDamageRate);
			if (normalDamage > 0) {
				effectContext.incSkillDamage(normalDamage);
				effectContext.incNormalSkillDamage(normalDamage);
			}

			SkillEffectUtil.doOneOccasionSkillEffect(battleContext, requestUnit, battleSkill, targetUnit, SkillOccasion.CalcExtraDamage, conditionContext, effectContext);
			SkillEffectUtil.doAllOccasionPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillOccasion.CalcExtraDamage, conditionContext, effectContext);

			BuffUtil.foreachBuffFeature(requestUnit, feature -> feature.beforeCastSkillRequest(battleContext, skillContext), conditionContext);
			BuffUtil.foreachBuffFeature(targetUnit, feature -> feature.beforeCastSkillTarget(battleContext, skillContext), conditionContext);

			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, targetUnit, requestUnit, SkillEffectType.MaxBeAttackDamagePercent, null, effectContext);
			SkillEffectUtil.doEffectAllPassiveSkillEffect(battleContext, requestUnit, targetUnit, SkillEffectType.AddCritRate, conditionContext, effectContext);

			int damage = effectContext.getSkillDamage();
			SkillDamage damageOutput = new SkillDamage(requestUnit, damageType, damage, normalDamage);
			if (damage > 0 || effectContext.getDecideHasDamage().finalDecideAndGet()) {
				for (SkillDamageRound skillDoDamage : SkillDamageRound.values()) {
					skillDoDamage.execute(battleContext, skillContext, damageOutput);
				}
			}
			else {
				damageOutput.addSkillUnitDamage(targetUnit).incDamage(damage);
			}
			List<SkillDamageUnit> unitDamageList = damageOutput.getUnitDamageList();
			for (SkillDamageUnit skillDamageUnit : unitDamageList) {
				skillDamageUnit.doDamage(battleContext, conditionContext, effectContext);
			}
			skillContext.setSkillDamageUnitList(unitDamageList);
			skillContextList.add(skillContext);
		}
		return skillContextList;
	}

	private static SkillEffectContext doCastActiveOccasionSkillEffect(BattleContext battleContext, SkillCastContext castContext, List<SkillContext> skillContextList, SkillEffectContext noneEffectContext, SkillEffectContext myEffectContext, SkillOccasion skillOccasion){
		BattleUnit requestUnit = castContext.getRequestUnit();
		BattleSkill battleSkill = castContext.getBattleSkill();
		BattleUnit targetUnit = castContext.getTargetUnit();
		SkillConfig skillConfig = battleSkill.getSkillConfig();
		List<SkillEffectConfig> effectConfigs = skillConfig.getEffectConfigs();

		for (SkillEffectConfig effectConfig : effectConfigs) {
			ConditionType conditionType = effectConfig.getSkillConditionType();
			if (conditionType.equals(ConditionType.None)) {
				doCastActiveOccasionSkillEffect(battleContext, requestUnit, battleSkill, effectConfig, targetUnit, null, noneEffectContext, skillOccasion);
				continue;
			}

			if (!ConditionType.getRequestUsage().contains(conditionType)) {
				continue;
			}

			if (skillContextList.isEmpty()) {
				doCastActiveOccasionSkillEffect(battleContext, requestUnit, battleSkill, effectConfig, targetUnit, null, myEffectContext, skillOccasion);
			}
			else {
				for (SkillContext skillContext : skillContextList) {
					IConditionContext conditionContext = skillContext.getConditionContext();
					ISkillEffectContext effectContext = skillContext.getSkillEffectContext();
					doCastActiveOccasionSkillEffect(battleContext, requestUnit, battleSkill, effectConfig, skillContext.getTargetUnit(), conditionContext, effectContext, skillOccasion);
				}
			}
		}
		SkillEffectContext finalEffectContext = EffectContextCombine.ActiveSkill.execute(new SkillEffectContext(), noneEffectContext, myEffectContext);
		for (SkillContext skillContext : skillContextList) {
			SkillEffectContext effectContext = skillContext.getSkillEffectContext();
			EffectContextCombine.ActiveSkill.execute(finalEffectContext, effectContext);
		}
		return finalEffectContext;
	}

	private static void doCastActiveOccasionSkillEffect(BattleContext battleContext, BattleUnit requestUnit, BattleSkill battleSkill, SkillEffectConfig effectConfig, BattleUnit targetUnit,
														IConditionContext conditionContext, ISkillEffectContext effectContext, SkillOccasion skillOccasion){
		Collection<SkillEffectUnitConfig> effectUnitConfigs = effectConfig.getEffectUnitConfigs(skillOccasion);
		if (effectUnitConfigs.isEmpty()) {
			return;
		}
		if (conditionContext == null){
			conditionContext = ConditionContext.DEFAULT;
		}
		List<BattleUnit> battleUnitList = effectConfig.getBattleUnitSelector().selectBattleUnits(battleContext, conditionContext, requestUnit, battleSkill, targetUnit);
		for (BattleUnit battleUnit : battleUnitList) {
			if (!effectConfig.checkCanDoEffect(battleContext, conditionContext, requestUnit, battleUnit)) {
				continue;
			}
			for (SkillEffectUnitConfig effectUnitConfig : effectUnitConfigs) {
				effectUnitConfig.getEffectHandler().doEffect(battleContext,requestUnit, battleSkill, battleUnit, conditionContext, effectContext);
			}
		}
	}


	private static int calculateNormalDamage(BattleContext battleContext, SkillContext skillContext, int normalDamageRate) {
		int skillDamageRate = normalDamageRate + skillContext.getSkillEffectContext().getSkillDamageRate();
		if (skillDamageRate <= 0){
			return 0;
		}
		// 随机数（0.95,1.05）*攻方攻击*攻方攻击/（攻方攻击+防方防御）*技能系数
		int atk = skillContext.getRequestUnit().getAttribute(Attribute.atk, skillContext.getConditionContext());
		int def = Math.max(0, calculateSelectUnitDef(battleContext, skillContext));
		float random = battleContext.getBattle().getRandom().random(0.95f, 1.05f);
		int skillDamage = Math.max(1, (int) (random * atk * atk * skillDamageRate / 1000 / (atk + def)));
		return skillDamage;
	}

	private static int calculateSelectUnitDef(BattleContext battleContext, SkillContext skillContext) {
		BattleUnit targetUnit = skillContext.getTargetUnit();
		int def = targetUnit.getAttribute(Attribute.def, skillContext.getConditionContext());
		int ignoreDefRate = skillContext.getSkillEffectContext().getIgnoreDefRate();
		if (ignoreDefRate <= 0) {
			return def;
		}

		if (BuffUtil.predicateFeatureUtilOneOkBool(targetUnit, feature -> !feature.canIgnoreDefRate(), skillContext.getConditionContext())) {
			return def;
		}

		if (ignoreDefRate >= 1000) {
			return 0;
		}

		int ignoreDef = BattleMath.rate1000(targetUnit.getAttribute(Attribute.def), ignoreDefRate);
		return Math.max(0, def - ignoreDef);
	}


	public static void doConditionAllSkill(BattleContext battleContext, BattleUnit requestUnit, BattleUnit targetUnit, ConditionType conditionType) {
		doConditionAllSkill(battleContext, requestUnit, targetUnit, conditionType, null);
	}

	/**
	 * 执行条件的所有被动技能
	 * @param battleContext
	 * @param requestUnit
	 * @param targetUnit
	 * @param conditionType
	 */
	public static void doConditionAllSkill(BattleContext battleContext, BattleUnit requestUnit, BattleUnit targetUnit, ConditionType conditionType, Consumer<SkillConditionRequest> consumer) {

		for (BattleSkill battleSkill : requestUnit.getSkillList()) {
			if (!battleSkill.isPassiveSkill()) {
				continue;
			}
			SkillEffectContext effectContext = new SkillEffectContext();
			ConditionContext conditionContext = new ConditionContext(effectContext);
			if (!SkillUtil.isConditionLimit(battleContext, conditionContext, requestUnit, battleSkill, targetUnit)) {
				continue;
			}
			SkillConditionRequest request = new SkillConditionRequest(requestUnit, battleSkill, targetUnit, conditionType)
					.setCheckSkillCD(true).setResetSkillCD(battleSkill.isPassiveSkill());
			if (consumer != null){
				consumer.accept(request);
			}
			SkillEffectUtil.doOneBaseSkillEffect(battleContext, request, conditionContext, effectContext);
			SkillEffectContextUtil.doSkillDamageUnit(battleContext, battleSkill, effectContext);
			SkillEffectContextUtil.onAllHealUnitSuccess(battleContext , effectContext);
		}
	}
}
