package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.project.core.battle.BattleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BattleControlList extends BattleControl implements IBattleControlList {

	private final int maxLoopCount;
	private final List<IBattleControl> battleControlList;

	public BattleControlList(BattleControlId battleControlId) {
		this(battleControlId, 0);
	}

	public BattleControlList(BattleControlId battleControlId, int loopCount) {
		super(battleControlId);
		this.maxLoopCount = loopCount;
		this.battleControlList = new ArrayList<>();
	}

	public int getMaxLoopCount(BattleContext battleContext) {
		return maxLoopCount;
	}

	public BattleControlList addBattleControl(Function<BattleControlId, IBattleControl> function){
		String name = String.valueOf(battleControlList.size() + 1);
		BattleControlId battleControlId = getBattleControlId().createChild(name);
		IBattleControl iBattleControl = function.apply(battleControlId);
		battleControlList.add(iBattleControl);
		return this;
	}

	@Override
	public void onInitialization(BattleContext battleContext){
		super.onInitialization(battleContext);
		ControlCache controlCache = getControlCache(battleContext);
		int loopIndex = controlCache.getLoopIndex();
		IBattleControl iBattleControl = battleControlList.get(loopIndex);
		iBattleControl.onInitialization(battleContext);
	}

	@Override
	protected ResultCode execute0(BattleContext battleContext) {
		return executeOrSkip(battleContext, false);
	}

	@Override
	protected ResultCode checkCondition(BattleContext battleContext) {
		if (executeCompletedAll(battleContext)) {
			return ResultCode.BATTLE_CONTROL_END;
		}
		return ResultCode.SUCCESS;
	}

	@Override
	protected ResultCode skip0(BattleContext battleContext) {
		return executeOrSkip(battleContext, true);
	}

	private ResultCode executeOrSkip(BattleContext battleContext, boolean executeSkip) {
		ControlCache controlCache = getControlCache(battleContext);
		int loopIndex = controlCache.getLoopIndex();
		IBattleControl iBattleControl = battleControlList.get(loopIndex);

		ResultCode resultCode = executeSkip ? iBattleControl.skip(battleContext) : iBattleControl.execute(battleContext);
		if (!resultCode.isSuccess()) {
			return resultCode;
		}
		boolean executeCompleted;
		if (iBattleControl instanceof  IBattleControlList){
			executeCompleted = ((IBattleControlList)iBattleControl).executeCompletedAll(battleContext);
		}
		else if (iBattleControl instanceof IBattleControlNode){
			executeCompleted = battleContext.isExecuteCompleted();
		}
		else {
			throw new UnsupportedOperationException(iBattleControl.getClass().getName());
		}
		if (executeCompleted){
			int loopCount = controlCache.getLoopCount();
			controlCache.incIndex(getMaxLoopCount(battleContext), executeSkip);
			if (controlCache.getLoopCount() > loopCount || controlCache.isLoopSuccess()){
				onOneLoopSuccess(battleContext, controlCache.getLoopCount(), executeSkip);
			}
			setControlCache(battleContext, controlCache);
		}
		return resultCode;
	}

	/**
	 * 完成一个回合之后的通知
	 * @param battleContext
	 */
	protected void onOneLoopSuccess(BattleContext battleContext, int finishLoopCount, boolean executeSkip) {
	}

	@Override
	protected void interrupt0(BattleContext battleContext, BattleInterrupt battleInterrupt) {
		ControlCache controlCache = getControlCache(battleContext);
		controlCache.index2OneLoopUntilEnd(battleControlList, battleControl -> battleControl.interrupt(battleContext, battleInterrupt));
	}

	@Override
	public boolean executeCompletedAll(BattleContext battleContext) {
		ControlCache controlCache = getControlCache(battleContext);
		return controlCache.isLoopSuccess();
	}

	protected ControlCache getControlCache(BattleContext battleContext){
		return getControlCache(battleContext,() -> new ControlCache(battleControlList.size()));
	}

	/**
	 * 对应的缓存
	 */
	public static class ControlCache {

		private final int oneLoopLength;
		private int index = 0;
		private int loopCount = 0;
		private boolean isLoopSuccess;

		public ControlCache(int oneLoopLength) {
			this.oneLoopLength = oneLoopLength;
		}

		public void incIndex(int maxLoopCount, boolean executeSkip){
			int loopIndex = ++index % oneLoopLength;
			if (loopIndex == 0){
				loopCount++;
			}
			isLoopSuccess = (loopIndex == 0 && executeSkip) || (maxLoopCount > 0 && loopCount >= maxLoopCount);
		}

		public int getLoopCount() {
			return loopCount;
		}

		public int getLoopIndex(){
			return index % oneLoopLength;
		}

		public void index2OneLoopUntilEnd(List<IBattleControl> battleControlList, Consumer<IBattleControl> consumer){
			for (int i = index; i < oneLoopLength; i++) {
				consumer.accept(battleControlList.get(i));
			}
		}

		public boolean isLoopSuccess() {
			return isLoopSuccess;
		}
	}
}
