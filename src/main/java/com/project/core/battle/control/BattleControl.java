package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleControlManager;

import java.util.function.Supplier;

public abstract class BattleControl implements IBattleControl {

	private final BattleControlId battleControlId;

	public BattleControl(BattleControlId battleControlId) {
		this.battleControlId = battleControlId;
	}

	@Override
	public final BattleControlId getBattleControlId() {
		return battleControlId;
	}

	@Override
	public void onInitialization(BattleContext battleContext){
	}

	@Override
	public final ResultCode execute(BattleContext battleContext) {
		ResultCode resultCode = executeCondition(battleContext);
		if (resultCode.isSuccess()) {
			resultCode = execute0(battleContext);
		}
		return resultCode;
	}

	protected abstract ResultCode executeCondition(BattleContext battleContext);

	protected abstract ResultCode execute0(BattleContext battleContext);

	@Override
	public final ResultCode skip(BattleContext battleContext) {
		return skip0(battleContext);
	}

	protected abstract ResultCode skip0(BattleContext battleContext);

	@Override
	public final void interrupt(BattleContext battleContext, BattleInterrupt battleInterrupt) {
		interrupt0(battleContext, battleInterrupt);
	}

	protected abstract void interrupt0(BattleContext battleContext, BattleInterrupt battleInterrupt);

	protected final <T> T getControlCache(BattleContext battleContext, Supplier<T> supplier){
		BattleControlManager controlManager = battleContext.getBattle().getControlManager();
		T controlCache = controlManager.getCache(getBattleControlId(), supplier);
		return controlCache;
	}

	protected final void setControlCache(BattleContext battleContext, Object controlCache){
		BattleControlManager controlManager = battleContext.getBattle().getControlManager();
		controlManager.addCache(getBattleControlId(), controlCache);
	}
}

