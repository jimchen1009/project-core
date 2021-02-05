package com.project.core.battle.operate;

import java.util.Collections;
import java.util.List;

public class BattleOperates {

	private final long userId;
	private List<BattleOperate> operateList;

	public BattleOperates(long userId, List<BattleOperate> operateList) {
		this.userId = userId;
		this.operateList = Collections.unmodifiableList(operateList);
	}

	public long getUserId() {
		return userId;
	}

	public List<BattleOperate> getOperateList() {
		return operateList;
	}

	public void setOperateList(List<BattleOperate> operateList) {
		this.operateList = operateList;
	}
}
