package com.project.core.battle.operate;

public class BattleOperate {

	private final long operatorId;
	private long targetId;
	private int useSkillId;
	private boolean selectByAI;

	public BattleOperate(long operatorId, long targetId, int useSkillId) {
		this.operatorId = operatorId;
		this.targetId = targetId;
		this.useSkillId = useSkillId;
		this.selectByAI = false;
	}

	public long getOperatorId() {
		return operatorId;
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

	public BattleOperate setSelectByAI(boolean selectByAI) {
		this.selectByAI = selectByAI;
		return this;
	}

	public BattleOperate resetOperate(long targetId, int useSkillId){
		return new BattleOperate(operatorId, targetId, useSkillId).setSelectByAI(selectByAI);
	}
}
