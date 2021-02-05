package com.project.core.battle;

import com.project.core.battle.buff.Buff;
import com.project.core.battle.result.ActorAction;
import com.project.core.battle.result.ActorPlay;
import com.project.core.battle.result.ActorPlayer;
import com.project.core.battle.result.ActorType;
import com.project.core.battle.skill.BattleSkill;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BattleContext implements IBattleContext {

	private final Object DEFAULT_OBJ = new Object();

	private final Battle battle;
	private final Date current;
	private final Map<String, IBattleContext> battleContext;
	private final Map<Long, BattleUnitContext> battleUnitContextMap;
	private final ActorPlayer actorPlayer;

	private final long operateUserId;
	private Object requestCommand;				//具体的请求参数
	private Object executeCommand;				//具体的执行参数
	private boolean executeCompleted;

	public BattleContext(long operateUserId, Battle battle) {
		this.operateUserId = operateUserId;
		this.battle = battle;
		this.current = new Date();
		this.battleContext = new HashMap<>();
		this.battleUnitContextMap = new HashMap<>();
		this.actorPlayer = new ActorPlayer();
		this.executeCompleted = false;
	}

	public long getOperateUserId() {
		return operateUserId;
	}

	public Battle getBattle() {
		return battle;
	}

	public Date getCurrent() {
		return current;
	}

	public Object getRequestCommand() {
		return requestCommand == null ? DEFAULT_OBJ : requestCommand;
	}

	public BattleContext setRequestCommand(Object requestCommand) {
		this.requestCommand = requestCommand;
		return this;
	}

	public Object getExecuteCommand() {
		return executeCommand;
	}

	public void setExecuteCommand(Object executeCommand) {
		this.executeCommand = executeCommand;
	}


	public boolean isExecuteCompleted() {
		return executeCompleted;
	}

	public void setExecuteCompleted(boolean executeCompleted) {
		this.executeCompleted = executeCompleted;
	}

	/**
	 * 获取战斗对象的上下文
	 * @param objectUniqueId
	 * @return
	 */
	public BattleUnitContext getBattleUnitContext(long objectUniqueId){
		return battleUnitContextMap.get(objectUniqueId);
	}

	/**
	 * 获取战斗对象的上下文
	 * @param battleUnit
	 * @return
	 */
	public BattleUnitContext getBattleUnitContext(BattleUnit battleUnit){
		return battleUnitContextMap.computeIfAbsent(battleUnit.getId(), key -> new BattleUnitContext(battleUnit));
	}

	public ActorPlayer getActorPlayer() {
		return actorPlayer;
	}

	public ActorPlay addActorPlayer(ActorPlay actorPlay){
		return actorPlayer.addActorPlayer(actorPlay);
	}

	public ActorPlay addBuffPlayer(Buff buff){
		return actorPlayer.addActorPlayer(new ActorPlay(buff.getHostUnit(), buff.getHostUnit(), ActorType.Buff, buff.getBuffId()));
	}

	public void addBattleAction(ActorAction actorAction){
		actorPlayer.addActorAction(actorAction);
	}

	public void confirmActorSkill(BattleUnit requestUnit, BattleSkill battleSkill, BattleUnit targetUnit){
		actorPlayer.confirmActorSkill(requestUnit, battleSkill, targetUnit);
	}

	/**
	 * 获取指定的上下文
	 * @param aClass
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IBattleContext> T getContext(Class<T> aClass){
		String name = aClass.getName();
		IBattleContext context = battleContext.get(name);
		return context == null ? null : (T)context;
	}

	/**
	 * 与 {@link #removeContext} 配合使用, 不能重复设置, 避免程序复用旧值而不知道
	 * @param context
	 */
	public void setContext(IBattleContext context){
		String name = context.getClass().getName();
		if (battleContext.containsKey(name)){
			throw new UnsupportedOperationException();
		}
		battleContext.put(name, context);
	}

	/**
	 * 与 {@link #setContext} 配合使用
	 * @param aClass
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IBattleContext> T removeContext(Class<T> aClass){
		return (T)battleContext.remove(aClass.getName());
	}
}
