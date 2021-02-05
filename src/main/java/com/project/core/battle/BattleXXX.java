package com.project.core.battle;

import com.project.core.battle.model.IBattleXXXData;

public class BattleXXX<T extends IBattleXXXData<T>> implements IBattleXXX<T> {

	private final T data;

	public BattleXXX(T data) {
		this.data = data;
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public T dataDeepCopy() {
		dataUpdate();
		return data.deepCopy();
	}

	/**
	 * 复制之前修正数据
	 */
	protected void dataUpdate() {

	}
}
