package com.project.core.battle.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public enum ConditionType {
	None(0, "无条件"),
	UseSkill(0, "使用主动技能"),
	MyHpRateBelow(1, "自身生命低于50%"),
	MyHpRateAbove(1, "自身生命高于50%"),
	TargetHpRateBelow(1, "目标生命低于50%"),
	TargetHpRateAbove(1, "目标生命高于50%"),
	IsCrit(1, "若暴击则"),
	IsBeCrit(2, "被暴击则"),
	TargetIsMiss(1, "目标闪避则"),
	SelfIsMiss(2, "自身闪避则"),
	TargetNormalAttack(2, "被普攻则"),
	NormalAttack(1, "自身普攻则"),
	TargetHasBuffType(1, "若目标存在降低攻击BUFF则"),
	SelfHasBuffType(1, "若自身存在防御增强则"),
	StartRound(0, "回合开始前"),
	StartBattle(0, "战斗开始前"),
	BeAttack(0, "受到攻击时"),
	BeDamage(0, "受到伤害则"),
	KillTarget(1, "杀死目标则"),
	TeamMateDead(0, "队友死亡则"),
	SelfDead(0, "自身死亡则"),
	RemoveTargetBuff(0, "清除任意人BUFF则"),
	EndRound(0, "大回合结束前"),
	HitTarget(1, "技能命中时"),
	RemoveSelfBuff(0, "清除自身BUFF则"),
	AddBuffType(1, "本回合使目标中毒则"),
	SkillTargetType(0, "对敌人使用技能"),
	BeforeBattle(0, "切入战斗时"),
	Heal(0, "自身恢复生命则"),
	TargetHasIsBuff(1, "若目标存在负面状态则"),
	TeamMateHpRateBelow(0, "队友受伤致生命低于50%"),
	TargetNotHasIsBuff(0, "若目标不存在正面状态则"),
	ChangeSkillCD(1, "自身被冷却延长时"),
	SelfNotHasBuffType(0, "若自身无战意爆发则"),
	HitTargetAndNotKill(0, "命中且未击杀则"),
	MyHpRateDecTo(0, "每当自身生命降低至50%时"),
	RemoveTargetOneBuff(0, "每清除1个BUFF则"),
	NotReliveTarget(0, "无复活效果则"),
	SelfHasIsBuff(0, "若自身存在负面状态则"),
	MyTeamBeCrit(0, "我方队友被暴击时"),
	BeAttackSelfHasBuffType(2, "自身受击时若自身有昏睡则"),
	TeamMateCountLTE(0, "若无存活队友"),
	EndEnemyRound(0, "敌方回合结束后"),
	EnemyCountGTE(1, "若敌方大于等于2人"),
	EnemyCountLTE(1, "若敌方小于等于1人"),
	MySummonMonsterGTE(0, "若我方场上存在4个召唤物则"),
	EnemyAddPositiveBuff(0, "若敌方任意获得正面BUFF时(连续触发)"),
	EnemyAddPositiveBuff_TriggerOnce(0, "若敌方任意获得正面BUFF时(仅触发1次)"),
	TeamMateBeCrit(0, "任意队友被暴击"),
	;


	/**
	 * 	BATTLE_USE = 0;
	 * 	REQUEST_USE = 1;
	 * 	TARGET_USE = 2;
	 */
	private final int useID;

	ConditionType(int useID, String comment) {
		this.useID = useID;
	}

	private static Collection<ConditionType> REQUEST_USAGE = Arrays.stream(ConditionType.values()).filter(conditionType -> conditionType.useID == 1).collect(Collectors.toSet());
	private static Collection<ConditionType> TARGET_USAGE = Arrays.stream(ConditionType.values()).filter(conditionType -> conditionType.useID == 2).collect(Collectors.toSet());

	public static Collection<ConditionType> getRequestUsage() {
		return REQUEST_USAGE;
	}

	public static Collection<ConditionType> getTargetUsage() {
		return TARGET_USAGE;
	}
}
