package com.project.core.battle.skill.effect;

import com.game.common.util.IEnumBase;

public enum SkillEffectType implements IEnumBase {
	None(0, "无"),
	Damage(10010, "造成伤害"),
	MyLostHpDamage(10011, "造成伤害(残血伤害高)，不会暴击"),
	TargetHpRateDamage(10012, "造成伤害(打敌上限)，不会暴击"),
	TargetCurHpRateDamage(10013, "造成伤害(打敌当前百分比)，不会暴击"),
	MeAndTargetBothDamage(10014, "造成伤害(打敌自伤血)，不会暴击，自伤血为流逝生命"),
	SkillDamage(10015, "造成伤害(后置伤害)"),
	CritDamage(10016, "造成暴击伤害"),
	AddCritDamage(10017, "提高XX暴击伤害"),
	DecHpRate(10018, "流逝生命(对己生命上限伤害)，不会暴击"),
	AddBuff(10019, "流逝生命(对己当前生命百分比伤害)，不会暴击"),
	AddBuffRound(10020, "附加BUFF"),
	Heal(10021, "BUFF+X回合"),
	SuckHp(10050, "瞬回血(治疗)X%+自身攻击*系数Y+自身生命*系数Z"),
	HealByCriticRate(10060, "瞬回血(吸血)X%"),
	AddHealRate(10070, "瞬回血(暴击)A"),
	Relive(10080, "治疗量增加"),
	RemoveBuff(10090, "使指定坑位的角色复活并回血，瞬回血(复活)X%+自身攻击*系数Y+自身生命*系数Z"),
	ChangeBuffDuration(10180, "清除X个Y面状态"),
	IgnoreTargetDef(10190, "延长Y面X回合"),
	HealToHigherHpRate(10200, "无视X%防御"),
	ChangeAllSkillCD(10210, "治疗至与生命较高一方"),
	ChangeCurSkillCD(10211, "平均全队血量"),
	ChangeOneSkillCD(10220, "XX技能：加攻技能，叠加10次"),
	CastSkill(10230, "增减所有技能CD，最高不超过原技能最大CD，最低即冷却0"),
	CastSkillId(10231, "本技能结束后给本技能增减技能CD，最高不超过原技能最大CD，最低即冷却0"),
	SkillNoResetCD(10240, "增减X技能CD，最高不超过原技能最大CD，最低即冷却0"),
	AddSkillDamageRateOnTargetBuff(10250, "释放X技能"),
	AddSkillDamageRateOnMyBuff(10251, "释放技能X"),
	TransferBuff(10260, "XX BUFF无效"),
	ResistBuff(10270, "本技能不进入CD"),
	IgnoreSkillCD(10271, "本技能不进入CD"),
	BuffRoundAddDamage(10280, "敌方每有1个Y面状态附加技能系数X"),
	BuffRoundAddHeal(10281, "我方每有1个Y面状态附加技能系数X"),
	BuffRoundChange(10290, "从A转移X个Y面状态"),
	BuffRoundAddCritDamage(10300, "BUFF抵抗"),
	BuffRoundChangeWithTarget(10311, "A BUFF每个回合数增加X技能系数的伤害"),
	SummonMonster(10312, "每个A BUFF每个回合数增加治疗量，治疗量=目标生命*系数X+施法者攻击*系数Y+施法者生命*系数Z"),
	SummonScene(10313, "增减A BUFF的回合数X个回合"),
	IgnoreBuffType(10314, "A BUFF每个回合数增加X暴击伤害"),
	AddHitRate(10315, "使 N 增减A BUFF的回合数X个回合(指定目标)"),
	ChangeSkill(10320, "召唤怪物A，属性为B"),
	Transform(10321, "召唤场景怪物A"),
	TransformRevert(10330, "无视所有X面状态"),
	DecTotalHp(10340, "此技能提高XX%命中率"),
	IgnoreHitOrResistEffect(10350, "更换技能组"),
	ResetSkillMainTarget(10351, "变身"),
	NoAwakeSleep(10352, "变身还原"),
	NotDead(10360, "削减生命上限，生命上限最低保留40%"),
	NotCastThisPassiveSkill(10370, "此逻辑无视命中和抵抗"),
	MaxBeAttackDamagePercent(10380, "以此逻辑的目标作为主要目标"),
	AddBuffCountLimit(10390, "此技能不会打醒处在昏睡的目标"),
	TeamAddDamageRate(10400, "不会死亡"),
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
