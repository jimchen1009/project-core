package com.project.core.battle.skill.damage;

import com.game.common.util.IncInteger;

public class DamageModifier extends IncInteger {

	private DamageType damageType;
	private final int initDamage;

	public DamageModifier(DamageType damageType, int damage) {
		super(damage);
		this.damageType = damageType;
		this.initDamage = damage;
	}

	public void addDamage(int inc) {
		increaseValue(inc);
	}

	public void setDamage(int damage) {
		setValue(damage);
	}

	public void addDamageRate(int rate) {
		addDamage(initDamage * rate / 1000);
	}

	public void decDamageRate(int rate, int min) {
		// 向下取整
		int damage = getDamage();
		damage = Math.max(min, damage - (initDamage * rate) / 1000);
		setDamage(damage);
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public int getDamage() {
		return getValue();
	}
}
