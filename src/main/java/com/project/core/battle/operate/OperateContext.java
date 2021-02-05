package com.project.core.battle.operate;

import com.project.core.battle.BattleUnit;

import java.util.ArrayList;
import java.util.List;

public class OperateContext {

	private final BattleUnit battleUnit;
	private final BattleOperate operate;

	private List<BattleOperate> resetOperateList;

	public OperateContext(BattleUnit battleUnit, BattleOperate operate) {
		this.battleUnit = battleUnit;
		this.operate = operate;
	}

	public BattleOperate getOperate() {
		return operate;
	}

	public void addResetOperate(BattleOperate resetOperate){
		if (resetOperateList == null) {
			resetOperateList = new ArrayList<>();
		}
		resetOperateList.add(resetOperate);
	}

	public BattleOperate getFinalOperate(){
		/**
		 * 在有多个重置效果的情况下,需要
		 */
		return resetOperateList == null ? operate : resetOperateList.get(0);
	}
}
