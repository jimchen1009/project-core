package com.project.core.battle.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BattleDataUtil {

	public static <T extends IBattleXXXData<T>> List<T> deepCopy(Collection<T> battleDateList){
		return battleDateList.stream().map(IBattleXXXData::deepCopy).collect(Collectors.toList());
	}
}
