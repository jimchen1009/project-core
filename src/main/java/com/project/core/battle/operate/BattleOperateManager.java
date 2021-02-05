package com.project.core.battle.operate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BattleOperateManager {

	private final Map<Long, Object> user2OperateMap;

	public BattleOperateManager() {
		this.user2OperateMap = new HashMap<>();
	}

	public void addOperate(long userId, Object operateValue){
		user2OperateMap.put(userId, operateValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T getOperate(long userId){
		return (T)user2OperateMap.get(userId);
	}

	public boolean containerOperate(long userId){
		return user2OperateMap.containsKey(userId);
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> getAllOperates(){
		return user2OperateMap.values().stream().map( value -> (T)value).collect(Collectors.toList());
	}

	public void removeAllOperates(){
		user2OperateMap.clear();
	}
}
