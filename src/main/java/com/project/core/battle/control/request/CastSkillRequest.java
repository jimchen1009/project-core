package com.project.core.battle.control.request;

public class CastSkillRequest {

	private final long getCastorId;
	private final int skillId;
	private final long getSelectId;

	public CastSkillRequest(long getCastorId, int skillId, long getSelectId) {
		this.getCastorId = getCastorId;
		this.skillId = skillId;
		this.getSelectId = getSelectId;
	}

	public long getGetCastorId() {
		return getCastorId;
	}

	public int getSkillId() {
		return skillId;
	}

	public long getGetSelectId() {
		return getSelectId;
	}
}
