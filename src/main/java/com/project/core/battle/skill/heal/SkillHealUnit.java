package com.project.core.battle.skill.heal;

import com.game.common.util.DecideBool;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.attribute.BaseAttributes;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.result.ActorActionHeal;
import com.project.core.battle.skill.effect.ISkillEffectContext;

public class SkillHealUnit {

	private final BattleUnit requestUnit;
	private final BattleUnit healUnit;
	private final HealType healType;
	private final BaseAttributes beforeAttributes;
	private int healHpRate;
	private int healHp;
	private boolean deathSupport;

	private int doHealHp;
	private DecideBool healBool;

	public SkillHealUnit(BattleUnit requestUnit, BattleUnit healUnit, HealType healType) {
		this.requestUnit = requestUnit;
		this.healUnit = healUnit;
		this.healType = healType;
		this.beforeAttributes = healUnit.getAttributes().copy();
		this.healHpRate = 0;
		this.healHp = 0;
		this.deathSupport = false;
		this.healBool = new DecideBool(false);
	}

	public BattleUnit getRequestUnit() {
		return requestUnit;
	}

	public BattleUnit getHealUnit() {
		return healUnit;
	}


	public SkillHealUnit incHealHpRate(int incRate) {
		this.healHpRate += incRate;
		return this;
	}

	public SkillHealUnit incHealHp(int incHp) {
		this.healHp += incHp;
		return this;
	}

	public final boolean doHeal(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext) {
		if (healBool.hasDecidedValue()) {
			return false;
		}
		if (healUnit.isAlive() || deathSupport){
			startDoHeal(battleContext, conditionContext, effectContext);
			healBool.decideValue(true);
		}
		else {
			healBool.decideValue(false);
		}
		effectContext.addSkillHealUnit(this);
		battleContext.addBattleAction(new ActorActionHeal(requestUnit, healType, healUnit, doHealHp));
		return healBool.getCurrentValue();
	}

	public final void onHealSuccess(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){
		if (!healBool.finalDecideAndGet() || doHealHp <= 0) {
			return;
		}
		doHealSuccess(battleContext, conditionContext, effectContext);
	}

	protected void doHealSuccess(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){

	}

	protected boolean startDoHeal(BattleContext battleContext, IConditionContext conditionContext, ISkillEffectContext effectContext){
		int incHp = (int) ((long) healUnit.getAttribute(Attribute.thp) * healHpRate / 1000);
		if (healHp > 0) {
			incHp += healHp;
		}
		if (BuffUtil.predicateFeatureUtilOneOkBool(healUnit, feature -> !feature.canHeal())) {
			incHp = 0;
		}
		int hp = healUnit.getAttribute(Attribute.hp);
		int maxInc = healUnit.getAttribute(Attribute.thp) - hp;
		doHealHp = Math.min(incHp, Math.max(0, maxInc));
		healUnit.setAttribute(Attribute.hp, hp + doHealHp);
		return true;
	}
}
