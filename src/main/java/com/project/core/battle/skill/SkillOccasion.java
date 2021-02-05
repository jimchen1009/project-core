package com.project.core.battle.skill;

import com.game.common.util.IEnumBase;

public enum SkillOccasion implements IEnumBase {
	None(0, "无"),
	CalcNormalDamage(1, "计算正常伤害"),
	CalcExtraDamage(2, "结算额外伤害"),
	DoSkillEffect(3, "触发技能"),
	AfterDoSkillEffect(4, "触发技能之后"),
	;

	public final int id;
	public final String comment;

	SkillOccasion(int id, String comment) {
		this.id = id;
		this.comment = comment;
	}

	@Override
	public int getId() {
		return id;
	}
}
