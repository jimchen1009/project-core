package com.project.core.battle.operate;

import com.game.common.util.RandomUtil;
import com.project.core.battle.BattleUnit;

import java.util.ArrayList;
import java.util.List;

public class OperateContext {

	private final BattleUnit battleUnit;
	private final OperateSkill operate;

	private List<OperateSkill> resetOperateList;

	public OperateContext(BattleUnit battleUnit, OperateSkill operate) {
		this.battleUnit = battleUnit;
		this.operate = operate;
	}

	public OperateSkill getOperate() {
		return operate;
	}

	public void addResetOperate(OperateSkill resetOperate){
		if (resetOperateList == null) {
			resetOperateList = new ArrayList<>();
		}
		resetOperateList.add(resetOperate);
	}

	public OperateSkill getFinalOperate(){
		/**
		 * 在有多个重置效果的情况下,需要
		 */
		return resetOperateList == null ? operate : RandomUtil.select(resetOperateList);
	}
}
