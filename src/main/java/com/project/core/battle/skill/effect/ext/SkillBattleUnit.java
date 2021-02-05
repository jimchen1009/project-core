package com.project.core.battle.skill.effect.ext;

import com.game.common.util.BitOperator;

public class SkillBattleUnit {

	private final long id;
	private int ignoreGeneraId;

	public SkillBattleUnit(long id) {
		this.id = id;
		this.ignoreGeneraId = 0;
	}

	public long getId() {
		return id;
	}

	public boolean isIgnoreGenera(int generaId) {
		return BitOperator.checkBitValue(ignoreGeneraId, generaId);
	}

	public void addIgnoreGenera(int generaId) {
		ignoreGeneraId = BitOperator.setBitValue(ignoreGeneraId, generaId);
	}

	public void mergeFrom(SkillBattleUnit sourceBattleUnit){
		this.ignoreGeneraId |= sourceBattleUnit.ignoreGeneraId;
	}
}
