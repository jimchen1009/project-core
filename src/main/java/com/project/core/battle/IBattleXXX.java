package com.project.core.battle;

import com.project.core.battle.model.IBattleXXXData;

import java.io.Serializable;

public interface IBattleXXX<T extends IBattleXXXData<T>> extends Serializable {

	T getData();

	T dataDeepCopy();
}
