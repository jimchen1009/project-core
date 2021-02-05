package com.project.core.battle.skill;

import com.game.common.util.RandomUtil;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.condition.ConditionContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillCastContext extends SkillBaseRequest<SkillCastContext> implements ISkillCastContext {

	private SkillCastContext lastCastContext;			//技能链, 同时释放多个技

	private boolean autoNormalSkill;
	private boolean canChangeTargetUnit;
	private boolean canCastSkillAgain;
	private int castByOtherSkillId;

	private final Map<Long, BattleUnit> battleUnitMap;
	private final ConditionContext conditionContext;

	public SkillCastContext(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit) {
		super(requestUnit, battleSkill, targetUnit);
		this.battleUnitMap = new HashMap<>();
		this.conditionContext = new ConditionContext();
	}

	@Override
	public Collection<SkillEffectUnitConfig> getEffectUnitConfigs(SkillEffectConfig effectConfig) {
		return Collections.emptyList();
	}

	@Override
	protected SkillCastContext getMyself() {
		return this;
	}

	public void setTargetUnit(BattleUnit targetUnit) {
		this.targetUnit = targetUnit;
	}

	@Override
	public void changeTargetBattleUnit(BattleUnit battleUnit) {
		battleUnitMap.put(battleUnit.getId(), battleUnit);
	}

	public BattleUnit finalTargetBattleUnit() {
		return battleUnitMap.isEmpty() ? targetUnit : RandomUtil.select(new ArrayList<>(battleUnitMap.values()));
	}

	public ConditionContext getConditionContext() {
		return conditionContext;
	}

	public SkillCastContext getLastCastContext() {
		return lastCastContext;
	}

	public void setLastCastContext(SkillCastContext lastCastContext) {
		this.lastCastContext = lastCastContext;
	}

	/**
	 * 技能顺序从最近开始
	 * @return
	 */
	public List<SkillCastContext> getCastContextLink(){
		List<SkillCastContext> skillContextList = new ArrayList<>();
		SkillCastContext skillContext = this;
		while (skillContext != null){
			skillContextList.add(skillContext);
			skillContext = skillContext.getLastCastContext();
		}
		return skillContextList;
	}

	public SkillContext createSkillContext(BattleUnit targetUnit){
		return new SkillContext(getBattleSkill(), getRequestUnit(), targetUnit, this);
	}

	public boolean isAutoNormalSkill() {
		return autoNormalSkill;
	}

	public SkillCastContext setAutoNormalSkill(boolean autoNormalSkill) {
		this.autoNormalSkill = autoNormalSkill;
		return this;
	}

	public boolean isCanChangeTargetUnit() {
		return canChangeTargetUnit;
	}

	public SkillCastContext setCanChangeTargetUnit(boolean canChangeTargetUnit) {
		this.canChangeTargetUnit = canChangeTargetUnit;
		return this;
	}

	public boolean isCanCastSkillAgain() {
		return canCastSkillAgain;
	}

	public SkillCastContext setCanCastSkillAgain(boolean canCastSkillAgain) {
		this.canCastSkillAgain = canCastSkillAgain;
		return this;
	}


	public int getCastByOtherSkillId() {
		return castByOtherSkillId;
	}

	public SkillCastContext setCastByOtherSkillId(int castByOtherSkillId) {
		this.castByOtherSkillId = castByOtherSkillId;
		return this;
	}
}
