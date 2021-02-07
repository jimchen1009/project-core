package com.project.core.battle.model;

public class BattleSkillData implements IBattleXXXData<BattleSkillData> {

	private final int index;		//技能的位置,从0开始
	private final int skillId;		//技能ID
	private int cd;					//初始化的CD

	public BattleSkillData(int index, int skillId, int cd) {
		this.index = index;
		this.skillId = skillId;
		this.cd = cd;
	}

	public int getIndex() {
		return index;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getCd() {
		return cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}

	public void incCd(int inc){
		this.cd += inc;
	}

	public void decCd(int dec){
		this.cd -= dec;
	}

	@Override
	public BattleSkillData deepCopy() {
		return new BattleSkillData(index, skillId, cd);
	}
}
