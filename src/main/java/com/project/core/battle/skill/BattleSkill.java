package com.project.core.battle.skill;


import com.project.core.battle.BattleXXX;
import com.project.core.battle.model.BattleSkillData;

import java.util.Objects;

public class BattleSkill extends BattleXXX<BattleSkillData> {

	private final SkillConfig skillConfig;
	private int beginRoundCd;

	public BattleSkill(BattleSkillData data) {
		super(data);
		this.skillConfig = Objects.requireNonNull(SkillConfig.getConfig(data.getSkillId()));
	}

	public int getIndex() {
		return getData().getIndex();
	}

	public int getSkillId() {
		return getData().getSkillId();
	}

	public void resetToInitCD() {
		getData().setCd(skillConfig.getInitCd());
	}

	public void onRoundBegin() {
		this.beginRoundCd = getCD();
	}

	public int resetCD() {
		int oldCD = getData().getCd();
		int newCD = Math.max(0, skillConfig.getCd());
		getData().setCd(newCD);
		return newCD - oldCD;
	}

	public int getCD(){
		return getData().getCd();
	}

	public boolean decEndRoundCD() {
		if (getData().getCd() <= 0) {
			return false;
		}
		getData().decCd(1);
		return true;
	}

	public boolean isPassiveSkill(){
		return skillConfig.isPassiveSkill();
	}

	public boolean isActiveSkill(){
		return skillConfig.isActiveSkill();
	}

	public SkillConfig getSkillConfig() {
		return skillConfig;
	}
}
