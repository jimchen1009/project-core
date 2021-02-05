package com.project.core.battle.skill.damage;

import com.game.common.util.CommonUtil;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.skill.SkillContext;
import com.project.core.battle.skill.effect.ISkillEffectContext;

import java.util.ArrayList;
import java.util.List;

public class SkillDamageHandler_Final extends SkillDamageHandler {

	@Override
	protected void execute0(BattleContext battleContext, SkillContext skillContext, SkillDamage damageOutput) {
		BattleUnit targetUnit = skillContext.getTargetUnit();
		ISkillEffectContext effectContext = skillContext.getSkillEffectContext();
		IConditionContext conditionContext = skillContext.getConditionContext();
		int damage = damageOutput.getDamage();
		List<BattleUnit> battleUnitList = new ArrayList<>();
		BuffUtil.foreachBuffFeature(targetUnit, feature -> feature.getProtectBattleUnit(battleContext, effectContext, battleUnitList));
		if (!battleUnitList.isEmpty()){
			targetUnit = CommonUtil.findOneUtilOkay(battleUnitList, BattleUnit::isAlive);
		}
		if (damage > 0){
			damage = BuffUtil.adjustFinalDamage(battleContext, conditionContext, effectContext, targetUnit, damageOutput.getDamageType(), damage);
		}
		damageOutput.addSkillUnitDamage(targetUnit).incDamage(damage);
//		if (damage > 0 && BuffUtil.predicateFeatureUtilOneOkBool(targetUnit, feature -> feature.isFeature(BuffFeatureType.DamageLink))){
//			List<BattleUnit> linkDamageUnits = damageOutput.getLinkDamageUnits();
//			if (linkDamageUnits == null) {
//				List<BattleUnit> linkDamageUnits0 = new ArrayList<>();
//				BattleUtil.foreachBattleUnit(targetUnit.getIndexTeamUnit(), battleUnit -> {
//					if (battleUnit.isDead() || !BuffUtil.predicateFeatureUtilOneOkBool(battleUnit, feature -> feature.isFeature(BuffFeatureType.DamageLink))) {
//						return;
//					}
//					linkDamageUnits0.add(battleUnit);
//				});
//				linkDamageUnits = linkDamageUnits0;
//				damageOutput.setLinkDamageUnits(linkDamageUnits);
//			}
//			int linkDamage = Math.max(1, damage / linkDamageUnits.size());
//			for (BattleUnit linkBattleUnit : linkDamageUnits) {
//				int curDamage = linkDamage;
//				if (linkBattleUnit.getId() == targetUnit.getId()) {
//					curDamage = BuffUtil.adjustFinalDamage(battleContext, skillContext, skillContext, damageOutput.getDamageType(), damage);
//					damageOutput.getUnitDamage(linkBattleUnit, true).incDamage(curDamage);
//				}
//				else {
//					damageOutput.getUnitDamage(linkBattleUnit, false).incDamage(curDamage);
//				}
//			}
//		}
//		else {
//			damageOutput.getUnitDamage(targetUnit, true).incDamage(damage);
//		}
		damageOutput.setDamage(damage);
	}
}
