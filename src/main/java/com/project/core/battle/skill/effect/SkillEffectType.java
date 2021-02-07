package com.project.core.battle.skill.effect;

import com.game.common.util.IEnumBase;

public enum SkillEffectType implements IEnumBase {
	None(0, "无"),
	AddBuff(10020, "附加BUFFX回合Y"),
	Heal(10050, "瞬回血(治疗)X%+自身攻击*系数Y+自身生命*系数Z"),
	RemoveBuff(10180, "清除X个Y面状态的BUFF"),
	ChangeBuffDuration(10190, "延长Y面X回合"),
	HealToHigherHpRate(10210, "治疗至与生命较高一方"),
	CastSkillId(10251, "释放技能"),

	SkillDamage(10015, "技能伤害（后置）"),
	CastSkill(10250, "释放技能"),
	SkillNoResetCD(10270),
	IgnoreSkillCD(10271, "本技能不进入CD"),
	NotCastThisPassiveSkill(10410, "不能触发此被动技能"),
	MaxBeAttackDamagePercent(10420, "单次伤害不超过"),
	DecBeAttackDamageRate(10450, "受到伤害减少A%"),
	AddAttackDamageRate(10460, "伤害技能提高伤害A%"),
	AddCritRate(10470, "增加暴击率"),
	;

	public final int id;

	SkillEffectType(int id) {
		this.id = id;
	}

	SkillEffectType(int id, String comment) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}
}
