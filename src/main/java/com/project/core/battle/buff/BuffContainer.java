package com.project.core.battle.buff;

import com.game.common.util.CommonUtil;
import com.project.core.battle.BattleUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BuffContainer implements Serializable {

	private final BattleUnit battleUnit;

	private final List<Buff> buffList;
	private final Map<Integer, Container> containerMap;

	public BuffContainer(BattleUnit battleUnit) {
		this.battleUnit = battleUnit;
		this.buffList = new ArrayList<>();
		this.containerMap = new HashMap<>();
	}

	public BattleUnit getBattleUnit() {
		return battleUnit;
	}

	public List<Buff> getBuffList() {
		List<Buff> buffList = new ArrayList<>();
		containerMap.values().forEach( container -> buffList.addAll(container.getBuffList()));
		return buffList;
	}

	public Buff getBuff(Predicate<Buff> predicate){
		return CommonUtil.findOneUtilOkay(buffList, buff -> buff.isRemainRound() && predicate.test(buff));
	}

	/**
	 * 获取类型容器
	 * @param buffType
	 * @return
	 */
	public Container getTypeContainer(int buffType){
		return containerMap.computeIfAbsent(buffType, Container::new);
	}

	public Collection<Container> getTypeContainers(){
		return containerMap.values();
	}

	/**
	 * 按照类型分类
	 */
	public static class Container {

		private final int typeId;
		private final List<Buff> buffList;
		private final List<Buff> otherBuffList;

		public Container(int typeId) {
			this.typeId = typeId;
			this.buffList = new ArrayList<>();
			this.otherBuffList = new LinkedList<>();
		}

		public int getTypeId() {
			return typeId;
		}

		public List<Buff> getBuffList() {
			return new ArrayList<>(buffList);
		}

		public boolean emptyBuffList() {
			return getBuffList().isEmpty();
		}

		public void incOtherBuffRound(Buff addBuff){
			Buff oneUtilOkay = CommonUtil.findOneUtilOkay(otherBuffList, buff -> addBuff.getBuffId() == buff.getBuffId());
			if (oneUtilOkay == null){
				otherBuffList.add(addBuff);
			}
			else {
				oneUtilOkay.addRemainRound(addBuff.getRemainRound());
			}
		}

		public Buff popOtherToBuffList(){
			if (otherBuffList.isEmpty()) {
				return null;
			}
			Buff addBuff = otherBuffList.remove(0);
			addBuff(addBuff);
			return addBuff;
		}

		public void addBuff(Buff addBuff){
			int index = CommonUtil.findIndexUtilOkay(buffList, buff -> buff.getBuffId() == addBuff.getBuffId());
			if (index == -1 || buffList.get(index).isRoundUsedUp()){
				buffList.add(addBuff);
			}
			else {
				buffList.set(index, addBuff);
			}
		}

		public Buff removeBuff(Buff removeBuff){
			return CommonUtil.removeOneUntilOkay(buffList, buff -> buff.getId() == removeBuff.getId());
		}
	}
}
