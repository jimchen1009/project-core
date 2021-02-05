package com.project.core.battle.buff;

import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.battle.buff.featrue.IBuffFeature;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.model.BattleBuffData;
import com.project.core.battle.skill.damage.DamageModifier;
import com.project.core.battle.skill.damage.DamageType;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.effect.ext.SkillBattleUnit;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BuffUtil {

	public static Buff createBuff(Battle battle, BattleUnit casterBattleUnit, BattleBuffData buffData) {
		BuffConfig buffConfig = BuffConfig.getConfig(buffData.getBuffId());
		if (buffConfig == null) {
			return null;
		}

		if (buffData.getRound() <= 0) {
			buffData.setRound(1);
		}

		return new Buff(battle.genObjectId(), buffData, casterBattleUnit);
	}

	public static Buff predicateBuffUtilOneOk(BattleUnit battleUnit, Predicate<Buff> predicate){
		List<Buff> buffList = battleUnit.getBuffContainer().getBuffList();
		for (Buff buff : buffList) {
			if (predicate.test(buff)){
				return buff;
			}
		}
		return null;
	}


	public static boolean predicateFeatureUtilOneOkBool(BattleUnit battleUnit, Predicate<IBuffFeature> predicate){
		return predicateFeatureUtilOneOk(battleUnit, predicate, null) != null;
	}

	public static boolean predicateFeatureUtilOneOkBool(BattleUnit battleUnit, Predicate<IBuffFeature> predicate, IConditionContext conditionContext){
		return predicateFeatureUtilOneOk(battleUnit, predicate, conditionContext) != null;
	}

	public static IBuffFeature predicateFeatureUtilOneOk(BattleUnit battleUnit, Predicate<IBuffFeature> predicate){
		return predicateFeatureUtilOneOk(battleUnit, predicate, null);
	}

	public static IBuffFeature predicateFeatureUtilOneOk(BattleUnit battleUnit, Predicate<IBuffFeature> predicate, IConditionContext conditionContext){
		List<Buff> buffList = battleUnit.getBuffContainer().getBuffList();
		for (Buff buff : buffList) {
			if (predicateCondition(battleUnit, buff, conditionContext)){
				continue;
			}
			for (IBuffFeature feature : buff.getFeatureList()) {
				if (predicate.test(feature)) {
					return feature;
				}
			}
		}
		return null;
	}

	public static void foreachBuffFeature(BattleUnit battleUnit, Consumer<IBuffFeature> consumer){
		foreachBuffFeature(battleUnit, consumer, null);
	}

	public static void foreachBuffFeature(BattleUnit battleUnit, Consumer<IBuffFeature> consumer, IConditionContext conditionContext){
		List<Buff> buffList = battleUnit.getBuffContainer().getBuffList();
		for (Buff buff : buffList) {
			if (predicateCondition(battleUnit, buff, conditionContext)){
				continue;
			}
			foreachBuffFeature(buff, consumer);
		}
	}

	public static void foreachBuffFeature(Buff buff, Consumer<IBuffFeature> consumer){
		List<IBuffFeature> featureList = buff.getFeatureList();
		for (IBuffFeature feature : featureList) {
			consumer.accept(feature);
		}
	}

	private static boolean predicateCondition(BattleUnit battleUnit, Buff buff, IConditionContext conditionContext){
		if (conditionContext == null) {
			return false;
		}
		BuffTypeConfig typeConfig = buff.getTypeConfig();
		if (!typeConfig.isCanIgnore()){
			return true;
		}
		SkillBattleUnit skillBattleUnit = conditionContext.getSkillBattleUnit(battleUnit.getId());
		return skillBattleUnit != null && skillBattleUnit.isIgnoreGenera(buff.getGeneraId());
	}

	public static void decBattleUnitBuffRound(BattleContext battleContext, BattleUnit battleUnit, BuffDecPoint decPoint) {
		if (battleUnit.isDead()){
			return;
		}
		Collection<BuffContainer.Container> typeContainerList = battleUnit.getBuffContainer().getTypeContainers();
		for (BuffContainer.Container container : typeContainerList) {
			BuffTypeConfig typeConfig = BuffTypeConfig.getConfig(container.getTypeId());
			if (typeConfig == null){
				continue;
			}
			typeConfig.getStrategy().decBattleUnitBuffRound(battleContext, battleUnit, container, decPoint);
		}
	}

	public static int adjustFinalDamage(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext, BattleUnit battleUnit, DamageType damageType, int damage) {
		DamageModifier modifier = new DamageModifier(damageType, damage);
		BuffUtil.foreachBuffFeature(battleUnit, feature  -> feature.adjustFinalDamage(battleContext, effectContext, modifier), conditionContext);
		return modifier.getDamage();
	}
}
