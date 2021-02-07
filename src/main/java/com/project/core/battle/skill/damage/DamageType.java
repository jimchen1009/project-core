package com.project.core.battle.skill.damage;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public enum DamageType implements IEnumBase {
	Skill("普通", true, true, SkillDamageUnit.class),
	QuakeBack("反震", false, false, SkillDamageUnit.class),
	StrikeBack("反击", false, false, SkillDamageUnit.class),
	Poison("中毒", false, false, SkillDamageUnit.class),
	Protect("保护", false, false, SkillDamageUnit.class),
	DecHp("直接扣血", false, false, SkillDamageUnit.class),
	;

	public final String des;
	public final boolean canQuakeBack;		// 能否反震
	public final boolean canStrikeBack;		// 能否反击
	public final Class<? extends SkillDamageUnit> aClass;

	DamageType(String des, boolean canQuakeBack, boolean canStrikeBack, Class<? extends SkillDamageUnit> aClass) {
		this.des = des;
		this.canQuakeBack = canQuakeBack;
		this.canStrikeBack = canStrikeBack;
		this.aClass = aClass;
	}

	@Override
	public int getId() {
		return ordinal();
	}

	public SkillDamageUnit createDamageUnit(BattleUnit requestUnit, BattleUnit damageUnit){
		try {
			Constructor<? extends SkillDamageUnit> constructor = aClass.getConstructor(BattleUnit.class, BattleUnit.class, DamageType.class);
			return constructor.newInstance(requestUnit, damageUnit, this);
		}
		catch (Throwable throwable) {
			logger.error("{}", this.name(), throwable);
			return new SkillDamageUnit(requestUnit, damageUnit, this);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(DamageType.class);
}
