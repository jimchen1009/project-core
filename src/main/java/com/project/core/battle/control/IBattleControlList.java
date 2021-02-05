package com.project.core.battle.control;

import com.project.core.battle.BattleContext;

public interface IBattleControlList extends IBattleControl {

	/**
	 * 实行成功了, 但是执行完毕了没有
	 * @param battleContext
	 * @return
	 */
	boolean executeCompletedAll(BattleContext battleContext);
}
