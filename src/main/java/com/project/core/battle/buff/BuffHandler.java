package com.project.core.battle.buff;

import com.game.common.util.CommonUtil;
import com.game.common.util.IEnumBase;
import com.game.common.util.TupleBool;
import com.project.core.battle.BattleContext;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.buff.condition.BuffConditionType;
import com.project.core.battle.buff.condition.IBuffCondition;
import com.project.core.battle.model.BattleBuffData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BuffHandler {

	private static final Logger logger = LoggerFactory.getLogger(BuffHandler.class);

	private static List<IBuffCondition> ConditionList = Arrays.stream(BuffConditionType.values()).sorted(Comparator.comparingInt(IEnumBase::getId)).collect(Collectors.toList());


	/**
	 * 创建战斗之后的初始化接口
	 */
	public static void createInitializeBattle(BattleContext context, BattleUnit battleUnit) {
		List<BattleBuffData> buffDataList = battleUnit.getData().getBuffDataList();
		for (BattleBuffData buffData : buffDataList) {
			Buff addBuff = BuffUtil.createBuff(context.getBattle(), battleUnit, buffData);
			checkAllAndAddBuff(context, new BuffContext(null, battleUnit, addBuff, false));
		}
	}

	/**
	 * @param battleContext
	 * @param buffContext
	 * @return 添加BUFF的结果
	 */
	public static TupleBool<BuffContext> checkAllAndAddBuff(BattleContext battleContext, BuffContext buffContext) {
		for (IBuffCondition condition : ConditionList) {
			if (condition.isLimit(battleContext, buffContext)) {
				return new TupleBool<>(false, buffContext);
			}
		}
		BuffTypeConfig typeConfig = buffContext.getBuff().getTypeConfig();
		boolean success = typeConfig.getStrategy().directAddBuff(battleContext, buffContext);
		return new TupleBool<>(success, buffContext);
	}

	/**
	 *
	 * @param battleContext
	 * @param buffContext
	 */
	public static void directRemoveBuff(BattleContext battleContext, BuffContext buffContext) {
		buffContext.getBuffStrategy().directRemoveBuff(battleContext, buffContext);
	}

	/**
	 * 检查并且移除掉BUFF
	 * @param battleContext
	 * @param buffContext
	 */
	public static void checkAndRemoveBuff(BattleContext battleContext, BuffContext buffContext) {
		if (!CommonUtil.findOneUtilOkayBool(buffContext.getBuff().getFeatureList(), feature -> feature.checkRemoveFeature(battleContext))) {
			return;
		}
		buffContext.getBuffStrategy().directRemoveBuff(battleContext, buffContext);
	}
}
