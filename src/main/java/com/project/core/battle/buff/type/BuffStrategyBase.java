package com.project.core.battle.buff.type;

import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.Buff;
import com.project.core.battle.buff.BuffContainer;
import com.project.core.battle.buff.BuffContext;
import com.project.core.battle.buff.BuffUtil;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.battle.result.ActionType;
import com.project.core.battle.result.ActorActionBuff;
import com.project.core.battle.result.ActorPlay;
import com.project.core.battle.result.ActorType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BuffStrategyBase implements IBuffStrategy {

	@Override
	public final boolean directAddBuff(BattleContext battleContext, BuffContext buffContext) {
		//检查所有的条件
		BuffContainer.Container typeContainer = buffContext.getTypeContainer();
		boolean success = doAddBuff(battleContext, typeContainer, buffContext);
		if (success){
			//如果添加成功, 不管里面是否存在后备的BUFF, 肯定会存在一个BUFF在生效的
			Buff addBuff = buffContext.getBuff();
			List<Buff> removeBuffList = buffContext.getBuffContainer().getBuffList().stream()
					.filter(buff -> {
						List<Integer> excludeTypeList = addBuff.getTypeConfig().getExcludeTypes();
						return excludeTypeList.contains(buff.getType());
					}).collect(Collectors.toList());
			for (Buff removeBuff : removeBuffList) {
				BuffContext removeContext = buffContext.changeBuff(removeBuff);
				removeContext.getBuffStrategy().directRemoveBuff(battleContext, removeContext);
			}
		}
		return success;
	}

	protected abstract boolean doAddBuff(BattleContext battleContext, BuffContainer.Container typeContainer, BuffContext buffContext);

	@Override
	public final boolean directRemoveBuff(BattleContext battleContext, BuffContext buffContext) {
		BuffContainer.Container typeContainer = buffContext.getTypeContainer();
		Buff buff = typeContainer.removeBuff(buffContext.getBuff());
		if (buff == null){
			return false;
		}
		buffContext = buffContext.changeBuff(buff);
		doRemoveBuffActionAndFeature(battleContext, buffContext);
		onDirectRemoveBuffEvent(battleContext, buff.getHostUnit(), typeContainer);
		return true;
	}

	@Override
	public final void decBattleUnitBuffRound(BattleContext battleContext, BattleUnit battleUnit, BuffContainer.Container typeContainer, BuffDecPoint decPoint) {
		Collection<Buff> buffList = typeContainer.getBuffList();
		boolean removeOneAtLeast = false;
		for (Buff buff : buffList) {
			BuffDecPoint buffDecPoint = buff.getBuffDecPoint();
			if (!buffDecPoint.equals(decPoint)) {
				continue;
			}
			if (!buffDecPoint.canDecBuff(battleContext, buff)) {
				continue;
			}
			battleContext.addBuffPlayer(buff);
			BuffUtil.foreachBuffFeature(buff, feature -> feature.onDecBuffRound(battleContext));
			if (buff.isRemainRound()) {
				battleContext.addBattleAction(new ActorActionBuff(battleUnit, battleUnit, ActionType.BuffChange, buff));
			}
			else{
				doRemoveBuffActionAndFeature(battleContext, new BuffContext(buff, true));
				removeOneAtLeast = true;
			}
		}
		if (removeOneAtLeast){
			onDirectRemoveBuffEvent(battleContext, battleUnit, typeContainer);
		}
	}

	protected final void doAddBuffActionAndFeature(BattleContext battleContext, BuffContext buffContext){
		doAddBuffActionOnly(battleContext, buffContext);
		doAddBuffFeatureOnly(battleContext, buffContext);
	}

	protected final void doAddBuffActionOnly(BattleContext battleContext, BuffContext buffContext){
		Buff buff = buffContext.getBuff();
		buff.setHostUnit(buffContext.getBattleUnit());
		if (buffContext.isDoAction()){
			battleContext.addBattleAction(new ActorActionBuff(null, buffContext.getBattleUnit(), ActionType.BuffAdd, buff));
		}
	}

	protected final void doAddBuffFeatureOnly(BattleContext battleContext, BuffContext buffContext){
		BuffUtil.foreachBuffFeature(buffContext.getBuff(), feature -> feature.onFeatureAdd(battleContext));
	}

	protected final void doRemoveBuffActionAndFeature(BattleContext battleContext, BuffContext buffContext){
		Buff buff = buffContext.getBuff();
		if (buffContext.isDoAction()){
			battleContext.addBattleAction(new ActorActionBuff(null, buffContext.getBattleUnit(), ActionType.BuffRemove, buff));
		}
		BuffUtil.foreachBuffFeature(buff, feature -> feature.onFeatureRemove(battleContext));
	}

	protected void onDirectRemoveBuffEvent(BattleContext battleContext, BattleUnit battleUnit, BuffContainer.Container typeContainer){
	}
}
