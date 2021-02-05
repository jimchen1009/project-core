package com.project.core.battle.operate;

import com.game.common.util.CommonUtil;

import java.util.Collections;
import java.util.List;

public class OperateSkills {

	private List<OperateSkill> operateList;

	public OperateSkills(List<OperateSkill> operateList) {
		this.operateList = Collections.unmodifiableList(operateList);
	}

	public List<OperateSkill> getOperateList() {
		return operateList;
	}

	public OperateSkill getOperate(long requestId){
		return CommonUtil.findOneUtilOkay(operateList, operateSkill -> operateSkill.getRequestId() == requestId);
	}

	public void setOperateList(List<OperateSkill> operateList) {
		this.operateList = operateList;
	}
}
