package com.project.core.battle.skill.effect;

import com.game.common.util.DecideBool;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.IConditionContextLink;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ext.SkillBattleUnit;
import com.project.core.battle.skill.heal.SkillHealUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillEffectContext implements ISkillEffectContext, IConditionContext {

	private final IConditionContextLink conditionContextLink;

	private int skillDamageRate;
	private int ignoreDefRate;
	private int skillDamage;
	private int normalSkillDamage;

	private DecideBool decideCrit = new DecideBool(false);
	private DecideBool decideHit = new DecideBool(false);
	private DecideBool decideMiss = new DecideBool(false);
	private DecideBool decideNeverHurt = new DecideBool(false);
	private DecideBool decideHasDamage = new DecideBool(false);

	protected EnumMap<EffectParamKey, Object> effectValueMap;

	private Map<Long, SkillBattleUnit> effectBattleUnitMap;
	private List<SkillHealUnit> skillHealUnitList;
	private List<SkillDamageUnit> skillDamageUnitList;


	public SkillEffectContext() {
		this(null);
	}

	public SkillEffectContext(IConditionContextLink conditionContextLink) {
		this.conditionContextLink = conditionContextLink;
		this.effectValueMap = new EnumMap<>(EffectParamKey.class);
		this.effectBattleUnitMap = new HashMap<>();
	}

	@Override
	public int getSkillDamageRate() {
		return skillDamageRate;
	}

	@Override
	public void incSkillDamageRate(int incValue) {
		this.skillDamageRate += incValue;
	}

	@Override
	public int getIgnoreDefRate() {
		return ignoreDefRate;
	}

	@Override
	public void incIgnoreDefRate(int incValue) {
		this.ignoreDefRate += incValue;
	}

	@Override
	public int getSkillDamage() {
		return skillDamage;
	}

	@Override
	public void incSkillDamage(int incValue) {
		this.skillDamage += incValue;
	}

	@Override
	public int getNormalSkillDamage() {
		return normalSkillDamage;
	}

	@Override
	public void incNormalSkillDamage(int incValue) {
		this.normalSkillDamage += incValue;
	}

	@Override
	public DecideBool getDecideCrit() {
		return decideCrit;
	}

	@Override
	public DecideBool getDecideHit() {
		return decideHit;
	}

	@Override
	public DecideBool getDecideMiss() {
		return decideMiss;
	}

	@Override
	public DecideBool getDecideNeverHurt() {
		return decideNeverHurt;
	}

	@Override
	public DecideBool getDecideHasDamage() {
		return decideHasDamage;
	}

	@Override
	public <T> T getEffectValue(EffectParamKey paramKey) {
		//noinspection unchecked
		return (T) effectValueMap.get(paramKey);
	}

	@Override
	public <T> T getEffectValue(EffectParamKey paramKey, T defaultValue) {
		//noinspection unchecked
		return (T) effectValueMap.getOrDefault(paramKey, defaultValue);
	}

	@Override
	public SkillBattleUnit getSkillBattleUnit(long id) {
		return effectBattleUnitMap.get(id);
	}

	@Override
	public boolean castSkillMiss() {
		return decideMiss.getCurrentValue();
	}

	@Override
	public SkillBattleUnit computeSkillBattleUnit(long id) {
		return effectBattleUnitMap.computeIfAbsent(id, SkillBattleUnit::new);
	}

	@Override
	public Collection<SkillBattleUnit> getAllSkillBattleUnits() {
		return effectBattleUnitMap.values();
	}

	@Override
	public void addSkillHealUnit(SkillHealUnit skillHealUnit) {
		if (skillHealUnitList == null) {
			skillHealUnitList = new ArrayList<>();
		}
		skillHealUnitList.add(skillHealUnit);
	}

	@Override
	public Collection<SkillHealUnit> getAllSkillHealUnits() {
		return skillHealUnitList == null ? Collections.emptyList() : skillHealUnitList;
	}

	@Override
	public void addSkillDamageUnit(SkillDamageUnit damageUnit) {
		if (skillDamageUnitList == null) {
			skillDamageUnitList = new ArrayList<>();
		}
		skillDamageUnitList.add(damageUnit);
	}

	@Override
	public Collection<SkillDamageUnit> getAllSkillDamageUnits() {
		return skillDamageUnitList == null ? Collections.emptyList() : skillDamageUnitList;
	}

	@Override
	public boolean castHitTargetUnit() {
		return conditionContextLink != null && conditionContextLink.castHitTargetUnit();
	}
}
