package com.project.core.battle.model;

import com.project.core.battle.attribute.BaseAttributes;
import com.project.core.battle.BattleUnitType;

import java.util.ArrayList;
import java.util.List;

public class BattleUnitData implements IBattleXXXData<BattleUnitData> {

	private final long userId;			    	    //用户ID[ 玩家的UserId]
	private final long uniqueId;			    	//唯一ID[ 玩家的数据]
	private final BattleUnitType unitType;			//类型
	private final int index;				   	    //位置,从0开始~
	private final int modelId;						//模型ID
	private final int skinId;	   					//皮肤ID
	private final int AIId;	   					    //AI的ID
	private final BaseAttributes attributes;		//当前属性
	private final BaseAttributes baseAttributes;    //初始属性

	private List<BattleSkillData> skillDataList;
	private List<BattleBuffData> buffDataList;


	public BattleUnitData(long userId, long uniqueId, BattleUnitType unitType, int index, int modelId, int skinId, int AIId, BaseAttributes attributes) {
		this.userId = userId;
		this.uniqueId = uniqueId;
		this.unitType = unitType;
		this.index = index;
		this.modelId = modelId;
		this.skinId = skinId;
		this.AIId = AIId;
		this.attributes = attributes;
		this.baseAttributes = attributes.copy();
		this.skillDataList = new ArrayList<>();
		this.buffDataList = new ArrayList<>();
	}

	public long getUserId() {
		return userId;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public BattleUnitType getUnitType() {
		return unitType;
	}

	public int getIndex() {
		return index;
	}

	public int getModelId() {
		return modelId;
	}

	public int getSkinId() {
		return skinId;
	}

	public int getAIId() {
		return AIId;
	}

	public BaseAttributes getAttributes() {
		return attributes;
	}

	public BaseAttributes getBaseAttributes() {
		return baseAttributes;
	}

	public List<BattleSkillData> getSkillDataList() {
		return skillDataList;
	}

	public void setSkillDataList(List<BattleSkillData> skillDataList) {
		this.skillDataList = skillDataList;
	}

	public List<BattleBuffData> getBuffDataList() {
		return buffDataList;
	}

	public void setBuffDataList(List<BattleBuffData> buffDataList) {
		this.buffDataList = buffDataList;
	}

	@Override
	public BattleUnitData deepCopy() {
		BattleUnitData battleUnitData = new BattleUnitData(userId, uniqueId, unitType, index, modelId, skinId, AIId, attributes.copy());
		battleUnitData.setSkillDataList(BattleDataUtil.deepCopy(skillDataList));
		battleUnitData.setBuffDataList(BattleDataUtil.deepCopy(buffDataList));
		return battleUnitData;
	}
}
