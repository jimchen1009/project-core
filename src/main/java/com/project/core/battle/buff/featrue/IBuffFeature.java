package com.project.core.battle.buff.featrue;

import com.game.common.util.SupplyHolder;
import com.game.common.util.IncInteger;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.operate.OperateContext;
import com.project.core.battle.skill.SkillCastContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.damage.DamageModifier;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.ISkillEffectContext;
import com.project.core.battle.skill.heal.SkillHealUnit;

import java.util.List;

public interface IBuffFeature {

	Buff getBuff();

	BuffFeatureType getFeatureType();

	boolean isFeature(BuffFeatureType featureType);

	boolean isLimitAddBuff(BattleContext battleContext, BuffContext buffContext);

	boolean isSkillLimit(BattleContext battleContext, IConditionContext conditionContext);

	boolean canCastSkill(BattleContext battleContext, IConditionContext conditionContext);

	void onBattleStartRound(BattleContext context);

	void onBattleEndRound(BattleContext context);

	void onIncBuffRound(BattleContext context, BuffContext buffContext);

	void onDecBuffRound(BattleContext context);

	void onFeatureAdd(BattleContext context);

	void onFeatureRemove(BattleContext context);

	void resetBattleOperate(BattleContext context, OperateContext operateContext);

	void castResetTargetUnit(BattleContext context, SkillCastContext castContext);

	boolean canIgnoreDefRate();

	void beforeCastSkillRequest(BattleContext context, SkillContext skillContext);

	void beforeCastSkillTarget(BattleContext context, SkillContext skillContext);

	void getExtraAttribute(Attribute attribute, IncInteger incInteger);

	void getAttackAddDamage(BattleContext context, ISkillEffectContext effectContext, IncInteger addAtkDamage);

	void adjustFinalDamage(BattleContext context, ISkillEffectContext effectContext, DamageModifier modifier);

	void getProtectBattleUnit(BattleContext context, ISkillEffectContext effectContext, List<BattleUnit> battleUnitList);

	boolean canHeal();

	boolean checkRemoveFeature(BattleContext context);

	void onBeDamage(BattleContext context, SkillDamageUnit damageUnit);

	void onEndRoundHeal(BattleContext context, SupplyHolder<SkillHealUnit> holder);
}
