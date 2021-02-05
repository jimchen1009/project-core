package com.project.core.battle.skill.effect.combine;

import com.game.common.util.DecideBool;
import com.project.core.battle.skill.effect.SkillEffectContext;
import com.project.core.battle.skill.effect.ext.SkillBattleUnit;
import com.project.core.battle.skill.heal.SkillHealUnit;

public abstract class EffectContextCombineHandler implements IEffectContextCombineHandler {

	@Override
	public SkillEffectContext execute(SkillEffectContext sourceContext, SkillEffectContext... fromContextList) {
		for (SkillEffectContext fromContext : fromContextList) {
			executeOne(sourceContext, fromContext);
		}
		return sourceContext;
	}

	protected abstract void executeOne(SkillEffectContext sourceContext, SkillEffectContext fromContext);


	protected void combineDecideBool(SkillEffectContext sourceContext, SkillEffectContext fromContext) {
		DecideBool.combineDecideBool(sourceContext.getDecideCrit(), fromContext.getDecideCrit(), true);
		DecideBool.combineDecideBool(sourceContext.getDecideHit(), fromContext.getDecideHit(), true);
		DecideBool.combineDecideBool(sourceContext.getDecideMiss(), fromContext.getDecideMiss(), true);
		DecideBool.combineDecideBool(sourceContext.getDecideNeverHurt(), fromContext.getDecideNeverHurt(), true);
		DecideBool.combineDecideBool(sourceContext.getDecideHasDamage(), fromContext.getDecideHasDamage(), true);
	}

	protected void combineSkillBattleUnit(SkillEffectContext sourceContext, SkillEffectContext fromContext) {
		for (SkillBattleUnit battleUnit : fromContext.getAllSkillBattleUnits()) {
			SkillBattleUnit skillBattleUnit = sourceContext.computeSkillBattleUnit(battleUnit.getId());
			skillBattleUnit.mergeFrom(battleUnit);
		}
	}

	protected void combineSkillHealUnit(SkillEffectContext sourceContext, SkillEffectContext fromContext) {
		for (SkillHealUnit skillHealUnit : fromContext.getAllSkillHealUnits()) {
			sourceContext.addSkillHealUnit(skillHealUnit);
		}
	}
}
