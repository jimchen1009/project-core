package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;

import java.util.Collection;

public abstract class SkillBaseRequest<T extends SkillBaseRequest> {

	protected final BattleUnit requestUnit;
	protected BattleUnit targetUnit;
	private BattleSkill battleSkill;
	private boolean checkSkillCD;
	private boolean resetSkillCD;
	private boolean doActorAction;

	public SkillBaseRequest(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		this.requestUnit = requestUnit;
		this.battleSkill = battleSkill;
		this.targetUnit = targetUnit;
		this.checkSkillCD = false;
	}

	public BattleUnit getRequestUnit() {
		return requestUnit;
	}

	public BattleSkill getBattleSkill() {
		return battleSkill;
	}

	public int getSkillId(){
		return battleSkill.getSkillId();
	}

	public BattleUnit getTargetUnit() {
		return targetUnit;
	}

	public boolean isCheckSkillCD() {
		return checkSkillCD;
	}

	public T setCheckSkillCD(boolean checkSkillCD) {
		this.checkSkillCD = checkSkillCD;
		return getMyself();
	}

	public boolean needResetSkillCD() {
		return checkSkillCD && resetSkillCD;
	}

	public T setResetSkillCD(boolean resetSkillCD) {
		this.resetSkillCD = resetSkillCD;
		return getMyself();
	}

	public boolean isDoActorAction() {
		return doActorAction;
	}

	public T setDoActorAction(boolean doActorAction) {
		this.doActorAction = doActorAction;
		return getMyself();
	}

	public abstract Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillEffectConfig effectConfig);

	protected abstract T getMyself();
}
