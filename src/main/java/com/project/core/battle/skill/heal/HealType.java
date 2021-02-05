package com.project.core.battle.skill.heal;

import com.game.common.util.IEnumBase;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.skill.damage.DamageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public enum HealType implements IEnumBase {
	Skill("技能治疗", SkillHealUnit_Normal.class),
	Buff("BUFF治疗", SkillHealUnit_Normal.class),
	;


	private final Class<? extends SkillHealUnit> aClass;

	@Override
	public int getId() {
		return ordinal();
	}

	HealType(String comment, Class<? extends SkillHealUnit> aClass) {
		this.aClass = aClass;
	}

	public SkillHealUnit createUnitHeal(BattleUnit requestUnit, BattleUnit healUnit){
		try {
			Constructor<? extends SkillHealUnit> constructor = aClass.getConstructor(BattleUnit.class, BattleUnit.class, HealType.class);
			return constructor.newInstance(requestUnit, healUnit, this);
		}
		catch (Throwable throwable) {
			logger.error("{}", this.name(), throwable);
			return new SkillHealUnit(requestUnit, healUnit, this);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(DamageType.class);
}
