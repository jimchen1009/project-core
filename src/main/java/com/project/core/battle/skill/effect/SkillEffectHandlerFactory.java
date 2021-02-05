package com.project.core.battle.skill.effect;

import com.game.common.util.ClassFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillEffectHandlerFactory {

	private static final Logger logger = LoggerFactory.getLogger(SkillEffectHandlerFactory.class);

	private static final ClassFactory<SkillEffectHandler> CLASS_FACTORY = new ClassFactory<>(SkillEffectHandler.class, "_");

	public static ISkillEffectHandler newInstance(SkillEffectType effectType, int skillId, String params){
		SkillEffectHandler effectHandler;
		try {
			effectHandler = CLASS_FACTORY.createInstance(effectType.name());
		}
		catch (Throwable throwable){
			logger.error("", throwable);
			effectHandler = new SkillEffectHandler_Default();
		}
		effectHandler.setEffectType(effectType);
		effectHandler.setSkillId(skillId);
		effectHandler.initParam(params);
		return effectHandler;
	}
}
