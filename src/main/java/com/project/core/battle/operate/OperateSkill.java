package com.project.core.battle.operate;

public class OperateSkill {

	private final long requestId;
	private long targetId;
	private int useSkillId;
	private boolean selectByAI;

	public OperateSkill(long requestId, long targetId, int useSkillId) {
		this.requestId = requestId;
		this.targetId = targetId;
		this.useSkillId = useSkillId;
		this.selectByAI = false;
	}

	public long getRequestId() {
		return requestId;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public int getUseSkillId() {
		return useSkillId;
	}

	public void setUseSkillId(int useSkillId) {
		this.useSkillId = useSkillId;
	}

	public boolean isSelectByAI() {
		return selectByAI;
	}

	public OperateSkill setSelectByAI(boolean selectByAI) {
		this.selectByAI = selectByAI;
		return this;
	}

	public OperateSkill resetOperate(long targetId, int useSkillId){
		return new OperateSkill(requestId, targetId, useSkillId).setSelectByAI(selectByAI);
	}
}
