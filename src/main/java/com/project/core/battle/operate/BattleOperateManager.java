package com.project.core.battle.operate;

import java.util.HashMap;
import java.util.Map;

public class BattleOperateManager {

	private final Map<Long, BattleOperate> idOperateMap;
	private final Map<Long, BattleOperates> userOperatesMap;

	public BattleOperateManager() {
		this.idOperateMap = new HashMap<>();
		this.userOperatesMap = new HashMap<>();
	}

	public void addOperate(BattleOperates operates){
		for (BattleOperate operate : operates.getOperateList()) {
			idOperateMap.put(operate.getOperatorId(), operate);
		}
		userOperatesMap.put(operates.getUserId(), operates);
	}

	public BattleOperate getOperate(long id){
		return idOperateMap.get(id);
	}

	public BattleOperates getOperates(long userId){
		return userOperatesMap.get(userId);
	}

	public void removeAllOperates(){
		idOperateMap.clear();
		userOperatesMap.clear();
	}
}
