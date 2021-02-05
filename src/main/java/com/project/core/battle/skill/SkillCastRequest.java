package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;

public class SkillCastRequest {

	private final BattleUnit requestUnit;
	private final BattleUnit selectUnit;

	private boolean canResetTarget;

	public SkillCastRequest(BattleUnit requestUnit, BattleUnit selectUnit) {
		this.requestUnit = requestUnit;
		this.selectUnit = selectUnit;
		this.canResetTarget = false;
		this.canResetTarget = false;
	}

	public BattleUnit getRequestUnit() {
		return requestUnit;
	}

	public BattleUnit getSelectUnit() {
		return selectUnit;
	}

	public boolean isCanResetTarget() {
		return canResetTarget;
	}

	public SkillCastRequest setCanResetTarget(boolean canResetTarget) {
		this.canResetTarget = canResetTarget;
		return this;
	}
}
