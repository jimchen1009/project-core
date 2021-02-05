package com.project.core.battle.skill.effect;

public enum EffectParamKey {

	AddBuffRound,
	AddHealRate,
	AddBeHealRate,
	ResistBuff,
	Buff,
	AddCritDamage,				// 增加暴击伤害
	AddNormalDamageRate,		// 普通攻击伤害加成
	AddHitRate,					// 增加命中
	NoAwakeSleep,				// 不惊醒睡眠
	NotDead,				    // 不会死亡
	MaxBeAttackDamagePercent,	// 单次最大伤害
	AddIsBuffTypeCount,			// 增加正（负）面Buff个数
	AddBuffCount,
	AddPoisonBuff,

	AddBuffCountLimit_Id,
	AddBuffCountLimit_Result,
	DecBeAttackDamageRate,		// 减少受到的伤害
	AddAttackDamageRate,		// 增加伤害
	AddCritRate,				// 增加暴击率
	ToxicHeal,					// 毒奶造成的伤害
	RemoveIsBuffType,			// 移除正（负）面buff类型
	;
}
