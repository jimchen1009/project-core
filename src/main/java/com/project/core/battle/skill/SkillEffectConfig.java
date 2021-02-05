package com.project.core.battle.skill;

import com.game.common.util.CommonUtil;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.condition.ConditionConfig;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.selector.IBattleUnitSelector;
import com.project.core.battle.skill.effect.SkillEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillEffectConfig {

	private final int index;
	private final int skillId;
	private final ConditionConfig conditionConfig;
	private final int effectRate;
	private final IBattleUnitSelector battleUnitSelector;
	private final List<SkillEffectUnitConfig> effectUnitConfigList;
	private final Map<SkillEffectType, List<SkillEffectUnitConfig>> typeUnitConfigs;
	private final Map<SkillOccasion, List<SkillEffectUnitConfig>> occasionUnitConfigs;
	private final boolean isHitOrResistEffect;
	private final boolean isIgnoreSkillCD;


	public SkillEffectConfig(int index, int skillId, ConditionConfig conditionConfig, int effectRate, IBattleUnitSelector battleUnitSelector, List<SkillEffectUnitConfig> effectUnitConfigList) {
		this.index = index;
		this.skillId = skillId;
		this.conditionConfig = conditionConfig;
		this.effectRate = effectRate;
		this.battleUnitSelector = battleUnitSelector;
		this.effectUnitConfigList = effectUnitConfigList;
		this.typeUnitConfigs = CommonUtil.splitUp1Into1Group(new HashMap<>(), effectUnitConfigList, ArrayList::new, SkillEffectUnitConfig::getEffectType);
		this.occasionUnitConfigs = CommonUtil.splitUp1Into1Group(new HashMap<>(), effectUnitConfigList, ArrayList::new, SkillEffectUnitConfig::getOccasionType);
		this.isHitOrResistEffect = CommonUtil.findOneUtilOkay(effectUnitConfigList, config -> config.getTypeConfig().isHitOrResistEffect()) != null;
		this.isIgnoreSkillCD = CommonUtil.findOneUtilOkay(effectUnitConfigList, config -> config.getEffectType().equals(SkillEffectType.IgnoreSkillCD)) != null;
	}

	public int getIndex() {
		return index;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getEffectRate() {
		return effectRate;
	}

	public ConditionConfig getConditionConfig() {
		return conditionConfig;
	}

	public ConditionType getSkillConditionType (){
		return conditionConfig.getConditionType();
	}

	public IBattleUnitSelector getBattleUnitSelector() {
		return battleUnitSelector;
	}

	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs() {
		return effectUnitConfigList;
	}

	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillEffectType effectType) {
		return typeUnitConfigs.getOrDefault(effectType, Collections.emptyList());
	}


	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillOccasion occasionType) {
		return occasionUnitConfigs.getOrDefault(occasionType, Collections.emptyList());
	}

	public boolean checkCanDoEffect(BattleContext context, IConditionContext conditionContext, BattleUnit requestUnit, BattleUnit targetUnit) {
		if (!getConditionConfig().getConditionHandler().isSatisfy(context, requestUnit, targetUnit, conditionContext)) {
			return false;
		}
		if (!randomDoEffect(context, requestUnit, targetUnit)) {
			return false;
		}
		return true;
	}

	public boolean isIgnoreSkillCD() {
		return isIgnoreSkillCD;
	}

	public boolean randomDoEffect(BattleContext context, BattleUnit requestUnit, BattleUnit targetUnit) {

		int effectRate = this.effectRate;
		int initHitRate = 0;
		int initResistRate = 0;
		int showResistRate = 0;
		if (isHitOrResistEffect && requestUnit != null && targetUnit != null) {
			initHitRate = requestUnit.getAttribute(Attribute.effect_hit);
			initResistRate = targetUnit.getAttribute(Attribute.effect_resist);
			if (initHitRate > 0 || initResistRate > 0) {
				effectRate = this.effectRate * (1000 + initHitRate - initResistRate) / 1000;
				showResistRate = this.effectRate * Math.max(0, (initResistRate - initHitRate)) / 1000;
			}
		}

		boolean randomSuccess;
		boolean isResist = false;
		if (effectRate >= 1000) {
			randomSuccess = true;
		}
		else if (effectRate <= 0) {
			randomSuccess = false;
			isResist = initResistRate > 0;
		}
		else {
			int randInt = context.getBattle().getRandom().nextInt(1000) + 1;
			if (randInt <= effectRate) {
				randomSuccess = true;
			}
			else {
				if (showResistRate > 0 && randInt <= effectRate + showResistRate){
					isResist = true;
				}
				randomSuccess = false;
			}
		}

		if (randomSuccess) {
			if (initHitRate > 0) {
//				requestUnit.tryTriggerEquipSetEffectByAttr("effect_hit");
			}
		}
		else {
			if (initResistRate > 0 && isResist) {
//				targetUnit.tryTriggerEquipSetEffectByAttr("effect_resist");
//				BattleResultNotifier.get().onEffectResist(targetUnit);
			}
		}

		return randomSuccess;
	}

}
