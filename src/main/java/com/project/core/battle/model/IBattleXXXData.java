package com.project.core.battle.model;

import java.io.Serializable;

public interface IBattleXXXData<T> extends Serializable {

	T deepCopy();
}
