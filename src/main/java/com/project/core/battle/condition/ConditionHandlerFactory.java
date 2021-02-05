package com.project.core.battle.condition;

import com.game.common.util.ClassFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConditionHandlerFactory {

	private static final Logger logger = LoggerFactory.getLogger(ConditionHandlerFactory.class);

	private static final ClassFactory<ConditionHandler> CLASS_FACTORY = new ClassFactory<>(ConditionHandler.class, "_");

	public static IConditionHandler newInstance(ConditionType conditionType, String params){
		ConditionHandler conditionHandler;
		try {
			conditionHandler = CLASS_FACTORY.createInstance(conditionType.name());
		}
		catch (Throwable throwable){
//			logger.error("", throwable);
			conditionHandler = new ConditionHandler_Default();
		}
		conditionHandler.setConditionType(conditionType);
		conditionHandler.initParam(params);
		return conditionHandler;
	}
}
