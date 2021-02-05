package com.project.core.battle.common;

import java.util.HashMap;
import java.util.Map;

public class IdIncInterMap {

	private Map<Long, IdIncInteger> idIncIntegerMap;

	public IdIncInterMap() {
		this.idIncIntegerMap = null;
	}

	public IdIncInteger computeIfAbsent(long id){
		return getIdIncIntegerMap().computeIfAbsent(id, key -> new IdIncInteger(id, 0));
	}

	private Map<Long, IdIncInteger> getIdIncIntegerMap() {
		if (idIncIntegerMap == null){
			idIncIntegerMap = new HashMap<>();
		}
		return idIncIntegerMap;
	}
}
