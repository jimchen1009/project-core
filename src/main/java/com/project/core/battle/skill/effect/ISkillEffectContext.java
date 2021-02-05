package com.project.core.battle.skill.effect;

import com.game.common.util.DecideBool;
import com.project.core.battle.IBattleContext;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ext.SkillBattleUnit;
import com.project.core.battle.skill.heal.SkillHealUnit;

import java.util.Collection;

public interface ISkillEffectContext extends IBattleContext {

	int getSkillDamageRate() ;

	void incSkillDamageRate(int incValue);

	int getIgnoreDefRate();

	void incIgnoreDefRate(int incValue);

	int getSkillDamage();

	void incSkillDamage(int incValue);

	int getNormalSkillDamage();

	void incNormalSkillDamage(int incValue);

	DecideBool getDecideCrit();

	DecideBool getDecideHit();

	DecideBool getDecideMiss();

	DecideBool getDecideNeverHurt();

	DecideBool getDecideHasDamage();

	<T> T getEffectValue(EffectParamKey paramKey);

	<T> T getEffectValue(EffectParamKey paramKey, T defaultValue);

	SkillBattleUnit computeSkillBattleUnit(long id);

	Collection<SkillBattleUnit> getAllSkillBattleUnits() ;

	void addSkillHealUnit(SkillHealUnit skillHealUnit);

	Collection<SkillHealUnit> getAllSkillHealUnits() ;

	void addSkillDamageUnit(SkillDamageUnit damageUnit) ;

	Collection<SkillDamageUnit> getAllSkillDamageUnits();
}