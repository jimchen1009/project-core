package com.project.core.battle;

import com.game.common.expression.IExprParams;
import com.game.common.util.CommonUtil;
import com.game.common.util.IncInteger;
import com.project.core.battle.attribute.Attribute;
import com.project.core.battle.attribute.AttributeType;
import com.project.core.battle.attribute.BaseAttributes;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.condition.IConditionContext;
import com.project.core.battle.model.BattleSkillData;
import com.project.core.battle.model.BattleUnitData;
import com.project.core.battle.skill.BattleSkill;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BattleUnit extends BattleXXX<BattleUnitData>{

	private final BattleTeamUnit teamUnit;
	private final long id;
	private final List<BattleSkill> skillList;
	private final BuffContainer buffContainer;
	private boolean isDead;

	public BattleUnit(Battle battle, BattleTeamUnit teamUnit, BattleUnitData data) {
		super(data);
		this.teamUnit = teamUnit;
		this.id = data.getUnitType().genObjectId(battle);
		this.skillList = data.getSkillDataList().stream()
				.sorted(Comparator.comparingInt(BattleSkillData::getIndex))
				.map(BattleSkill::new)
				.collect(Collectors.toList());
		this.buffContainer = new BuffContainer(this);
		this.isDead = data.getAttributes().getHp() <= 0;
	}

	public BattleUnitType getUnitType() {
		return getData().getUnitType();
	}

	public BattleTeamType getTeamType() {
		return teamUnit.getTeamType();
	}

	public BattleTeamUnit getTeamUnit() {
		return teamUnit;
	}

	public long getUserId() {
		return getData().getUserId();
	}

	@Override
	protected void dataUpdate() {
		getData().setSkillDataList(skillList.stream().map(BattleXXX::getData).collect(Collectors.toList()));
		getData().setBuffDataList(buffContainer.getBuffList().stream().map(BattleXXX::getData).collect(Collectors.toList()));
	}

	public long getId() {
		return id;
	}

	public BaseAttributes getAttributes() {
		return getData().getAttributes();
	}

	public BaseAttributes getBaseAttributes() {
		return getData().getBaseAttributes();
	}

	public BuffContainer getBuffContainer() {
		return buffContainer;
	}

	public void initSkillCD() {
		skillList.forEach(BattleSkill::resetToInitCD);
	}

	public void onRoundBegin() {
		skillList.forEach(BattleSkill::onRoundBegin);
	}

	public List<BattleSkill> getSkillList() {
		return skillList;
	}

	public BattleSkill getSkill(int skillId){
		return CommonUtil.findOneUtilOkay(skillList, skill -> skill.getSkillId() == skillId);
	}

	public BattleSkill getSkillByIndex(int index){
		return CommonUtil.findOneUtilOkay(skillList, skill -> skill.getData().getIndex() == index);
	}

	public BattleSkill getNormalAttackSkill() {
		return getSkillByIndex(0);
	}

	public boolean isAlive(){
		return !isDead();
	}

	public boolean isDead(){
		return isDead;
	}

	public void setDead(){
		isDead = true;
		teamUnit.addCurRoundDeadUnitId(this);
	}

	public int getAttribute(Attribute attribute) {
		return getAttribute(attribute, null);
	}

	public void setAttribute(Attribute attribute, int value){
		getAttributes().set(attribute, value);
	}

	public int getAttribute(Attribute attribute, IConditionContext context) {
		int value = getAttributes().get(attribute);
		if (attribute.getType().equals(AttributeType.Defence)) {
			IncInteger incInteger = new IncInteger(value);
			BuffUtil.foreachBuffFeature(this, feature -> feature.getExtraAttribute(attribute, incInteger), context);
			value = incInteger.getValue();
		}
		return value;
	}

	/**
	 * 千分比
	 * @return
	 */
	public int getHpRate() {
		return (int) ((long) getAttribute(Attribute.hp) * 1000 / getAttribute(Attribute.thp));
	}


	public IExprParams attributeParams(IExprParams params) {
		return getAttributes().attributeParams(params);
	}
}
