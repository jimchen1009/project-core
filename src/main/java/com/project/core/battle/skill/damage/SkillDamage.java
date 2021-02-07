package com.project.core.battle.skill.damage;

import com.project.core.battle.BattleUnit;

import java.util.ArrayList;
import java.util.List;

public class SkillDamage {

	private final BattleUnit attackUnit;
	private final DamageType damageType;
	private int damage;
	private int normalDamage;
	private boolean isCrit;
	private boolean isHit;
	private boolean isMiss;

	private final List<SkillDamageUnit> unitDamageList;

	private List<BattleUnit> linkDamageUnits = null;

	public SkillDamage(BattleUnit attackUnit, DamageType damageType, int damage, int normalDamage) {
		this.attackUnit = attackUnit;
		this.damageType = damageType;
		this.damage = damage;
		this.normalDamage = normalDamage;
		this.unitDamageList = new ArrayList<>();
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getNormalDamage() {
		return normalDamage;
	}

	public void setNormalDamage(int normalDamage) {
		this.normalDamage = normalDamage;
	}

	public boolean isCrit() {
		return isCrit;
	}

	public void setCrit(boolean crit) {
		isCrit = crit;
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean hit) {
		isHit = hit;
	}

	public boolean isMiss() {
		return isMiss;
	}

	public void setMiss(boolean miss) {
		isMiss = miss;
	}

	public SkillDamageUnit addSkillUnitDamage(BattleUnit damageUnit){
		SkillDamageUnit skillUnitDamage = damageType.createDamageUnit(attackUnit, damageUnit);
		skillUnitDamage.setCrit(isCrit).setMiss(isMiss);
		unitDamageList.add(skillUnitDamage);
		return skillUnitDamage;
	}

	public List<SkillDamageUnit> getUnitDamageList() {
		return new ArrayList<>(unitDamageList);
	}

	public List<BattleUnit> getLinkDamageUnits() {
		return linkDamageUnits;
	}

	public void setLinkDamageUnits(List<BattleUnit> linkDamageUnits) {
		this.linkDamageUnits = linkDamageUnits;
	}
}
