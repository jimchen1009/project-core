package com.project.core.battle;

import com.project.core.battle.control.BattleControlId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BattleControlManager {

	private Date ctrlInitializeDate;
	private final Map<BattleControlId, Object> controlCacheMap;

	public BattleControlManager() {
		this.controlCacheMap = new HashMap<>();
	}

	public Date getCtrlInitializeDate() {
		return ctrlInitializeDate;
	}

	public void setCtrlInitializeDate(Date ctrlInitializeDate) {
		this.ctrlInitializeDate = ctrlInitializeDate;
	}

	public void addCache(BattleControlId battleControlId, Object cache){
		controlCacheMap.put(battleControlId, cache);
	}

	@SuppressWarnings("unchecked")
	public <T> T getCache(BattleControlId battleControlId){
		Object cache = controlCacheMap.get(battleControlId);
		return cache == null ? null :(T)cache;
	}

	@SuppressWarnings("unchecked")
	public <T> T getCache(BattleControlId battleControlId, Supplier<T> supplier){
		Object cache = controlCacheMap.get(battleControlId);
		if (cache == null){
			cache = supplier.get();
			controlCacheMap.put(battleControlId, cache);
		}
		return (T)cache;
	}

	@SuppressWarnings("unchecked")
	public <T> T removeCache(BattleControlId battleControlId){
		Object cache = controlCacheMap.remove(battleControlId);
		return cache == null ? null :(T)cache;
	}
}
