package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.project.core.battle.BattleContext;

public interface IBattleControl {

	/**
	 * 获取唯一ID
	 * @return
	 */
	BattleControlId getBattleControlId();


	/**
	 * @param battleContext
	 */
	void onInitialization(BattleContext battleContext);

	/**
	 * 執行成功就執行下一步
	 * @param battleContext
	 * @return
	 */
	ResultCode execute(BattleContext battleContext);

	/**
	 * 是否满足跳过的条件
	 * @param battleContext
	 * @return
	 */
	ResultCode skip(BattleContext battleContext);

	/**
	 * 中断了
	 * @param battleContext
	 */
	void interrupt(BattleContext battleContext, BattleInterrupt battleInterrupt);
}
