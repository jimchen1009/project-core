package com.project.core.battle.control;

import com.game.common.util.ResultCode;
import com.game.common.util.TupleCode;
import com.project.core.battle.Battle;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleStage;
import com.project.core.battle.control.common.BattleControl_BattleBegin;
import com.project.core.battle.control.common.BattleControl_BattleEnd;
import com.project.core.battle.control.common.BattleControl_BattleLine;
import com.project.core.battle.control.common.BattleControl_RoundBegin;
import com.project.core.battle.control.common.BattleControl_RoundEnd;
import com.project.core.battle.control.pve.BattleControl_RoundRunPVE;
import com.project.core.battle.model.BattleData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class BattleControlService {

	private static final AtomicLong ID_GEN = new AtomicLong();

	private static Map<BattleType, BattleControl> type2ControlMap = new HashMap<>();

	static {
		type2ControlMap.put(BattleType.PVE, createBattlePVEControl());
	}

	private static BattleControl createBattlePVEControl(){
		return createBattleControl(BattleType.PVE, battleRoundControl -> battleRoundControl.addBattleControl(BattleControl_RoundRunPVE::new));
	}

	private static BattleControl createBattlePVPControl(){
		return createBattleControl(BattleType.PVE, battleRoundControl -> {
		});
	}

	private static BattleControl createBattleControl(BattleType battleType, Consumer<BattleControl_RoundLoop> consumer){
		BattleControl_BattleLine battleLine = new BattleControl_BattleLine(new BattleControlId(battleType.name()));
		battleLine.addBattleControl(BattleControl_BattleBegin::new);
		battleLine.addBattleControl(battleControlId -> {
			BattleControl_RoundLoop BattleRoundLoop = new BattleControl_RoundLoop(battleControlId);
			BattleRoundLoop.addBattleControl(BattleControl_RoundBegin::new);
			consumer.accept(BattleRoundLoop);
			BattleRoundLoop.addBattleControl(BattleControl_RoundEnd::new);
			return BattleRoundLoop;
		});
		battleLine.addBattleControl(BattleControl_BattleEnd::new);
		return battleLine;
	}


	public static BattleControl getBattleControl(BattleType battleType){
		return type2ControlMap.get(battleType);
	}


	public static Battle createBattle(BattleType battleType, BattleData battleData){
		Battle battle = new Battle(battleType, ID_GEN.incrementAndGet(), battleData);
		BattleContext battleContext = battle.createInitializeBattle();
		BattleControl battleControl = getBattleControl(battle.getBattleType());
		battleControl.onInitialization(battleContext);
		return battle;
	}

	/**
	 * @param battle
	 * @param requestCommand 执行命令
	 * @return
	 */
	public static TupleCode<BattleContext> execute(long operateUserId, Battle battle, Object requestCommand){
		return execute(operateUserId, battle, battleContext -> battleContext.setRequestCommand(requestCommand));
	}

	/**
	 * 执行AI
	 * @param operateUserId
	 * @param battle
	 * @return
	 */
	public static TupleCode<BattleContext> executeAI(long operateUserId, Battle battle){
		return execute(operateUserId, battle, battleContext -> battleContext.setExecuteAI(true));
	}


	private static TupleCode<BattleContext> execute(long operateUserId, Battle battle, Consumer<BattleContext> consumer){
		TupleCode<BattleContext> resultCode = invoke(operateUserId, battle, (battleControl, battleContext) -> {
			consumer.accept(battleContext);
			return battleControl.execute(battleContext);
		});
		if (resultCode.isSuccess()){
			TupleCode<BattleContext> skipCode = resultCode;
			while (skipCode.isSuccess()){
				skipCode = invoke(operateUserId, battle, BattleControl::skip);
			}
			BattleContext battleContext = resultCode.getData();
			System.out.println("执行结果:" + battleContext.getActorPlayer() + "\n");
		}
		return resultCode;
	}

	private static TupleCode<BattleContext> invoke(long operateUserId, Battle battle, BiFunction<BattleControl, BattleContext, ResultCode> function){
		if (battle.isBattleStage(BattleStage.BattleFinal)) {
			return new TupleCode<>(ResultCode.BATTLE_CONTROL_TAG);
		}
		BattleContext battleContext = new BattleContext(operateUserId, battle);
		BattleControl battleControl = getBattleControl(battle.getBattleType());
		ResultCode resultCode = function.apply(battleControl, battleContext);
		if (!resultCode.isSuccess()) {
			return new TupleCode<>(resultCode);
		}
		if (!battle.isBattleStage(BattleStage.BattleFinal)) {
			battleControl.onInitialization(battleContext);
		}
		return new TupleCode<>(battleContext);
	}
}
