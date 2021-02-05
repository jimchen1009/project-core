package com.project.core.battle;

import com.project.core.battle.skill.BattleSkill;
import com.project.core.battle.skill.SkillCastContext;

import java.util.HashMap;
import java.util.Map;

public class BattleUnitContext implements IBattleContext {

	private final BattleUnit battleUnit;
	private final Map<Integer, SkillCastContext> skillContextMap;

	public BattleUnitContext(BattleUnit battleUnit) {
		this.battleUnit = battleUnit;
		this.skillContextMap = new HashMap<>();
	}

	public BattleUnit getBattleUnit() {
		return battleUnit;
	}

	public SkillCastContext createSkillCastContext(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit){
		SkillCastContext skillContext = new SkillCastContext(requestUnit, battleSkill, targetUnit);
		SkillCastContext oldSkillContext = skillContextMap.get(battleSkill.getSkillId());
		if (oldSkillContext == null) {
			skillContextMap.put(skillContext.getSkillId(), skillContext);
		}
		else{
			oldSkillContext.setLastCastContext(skillContext);
		}
		return skillContext;
	}

	public SkillCastContext getSkillContext(int skillId){
		return skillContextMap.get(skillId);
	}
}
