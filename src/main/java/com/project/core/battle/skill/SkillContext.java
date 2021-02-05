package com.project.core.battle.skill;

import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionContext;
import com.project.core.battle.skill.damage.DamageType;
import com.project.core.battle.skill.damage.SkillDamageUnit;
import com.project.core.battle.skill.effect.SkillEffectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkillContext implements ISkillContext, IConditionContextLink {

	private final BattleUnit requestUnit;
	protected final BattleUnit targetUnit;
	public final BattleSkill battleSkill;
	private final SkillCastContext castContext;
	private List<SkillDamageUnit> skillDamageUnitList;

	private SkillEffectContext skillEffectContext;
	private ConditionContext conditionContext;

	public SkillContext(BattleSkill battleSkill, BattleUnit requestUnit, BattleUnit targetUnit, SkillCastContext castContext) {
		this.battleSkill = battleSkill;
		this.requestUnit = requestUnit;
		this.targetUnit = targetUnit;
		this.castContext = castContext;
		this.skillDamageUnitList = new ArrayList<>();
		this.skillEffectContext = new SkillEffectContext(this);
		this.conditionContext = new ConditionContext(skillEffectContext);
	}

	public SkillContext(BattleSkill battleSkill, BattleUnit requestUnit, BattleUnit selectUnit) {
		this(battleSkill, requestUnit, selectUnit, null);
	}

	@Override
	public BattleSkill getBattleSkill() {
		return battleSkill;
	}

	@Override
	public BattleUnit getRequestUnit() {
		return requestUnit;
	}

	@Override
	public BattleUnit getTargetUnit() {
		return targetUnit;
	}

	@Override
	public boolean isCastSkill() {
		return castContext != null;
	}

	@Override
	public SkillEffectContext getSkillEffectContext() {
		return skillEffectContext;
	}

	@Override
	public ConditionContext getConditionContext() {
		return conditionContext;
	}

	public List<SkillDamageUnit> getDamageUnitList(BattleUnit battleUnit, DamageType damageType) {
		return skillDamageUnitList.stream()
				.filter( unitDamage -> battleUnit.getId() == unitDamage.getAttackUnit().getId() && unitDamage.isDamageType(damageType))
				.collect(Collectors.toList());
	}

	public List<SkillDamageUnit> getDamageUnitList() {
		return new ArrayList<>(skillDamageUnitList);
	}

	public void setSkillDamageUnitList(List<SkillDamageUnit> skillDamageUnitList) {
		if (this.skillDamageUnitList == null){
			return;
		}
		this.skillDamageUnitList = skillDamageUnitList;
	}

	@Override
	public boolean castHitTargetUnit() {
		return isCastSkill();
	}
}
