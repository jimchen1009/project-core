package com.project.core.battle.common;

import com.game.common.util.CommonUtil;
import com.game.common.util.IncInteger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdIncInteger extends IncInteger {

	private final long id;

	public IdIncInteger(long id, int value) {
		super(value);
		this.id = id;
	}

	public IdIncInteger(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public static Map<Long, IdIncInteger> mergeTpMap(List<IdIncInteger> idIncIntegerLists){
		Map<Long, List<IdIncInteger>> longListMap = CommonUtil.splitUp1Into1Group(new HashMap<>(), idIncIntegerLists, ArrayList::new, IdIncInteger::getId);
		Map<Long, IdIncInteger> id2IncIntegerMap = new HashMap<>();
		for (Map.Entry<Long, List<IdIncInteger>> entry : longListMap.entrySet()) {
			int sum = entry.getValue().stream().mapToInt(IdIncInteger::getValue).sum();
			id2IncIntegerMap.put(entry.getKey(), new IdIncInteger(entry.getKey(), sum));
		}
		return id2IncIntegerMap;
	}
}
