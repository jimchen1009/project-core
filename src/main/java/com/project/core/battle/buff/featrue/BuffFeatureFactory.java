package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;
import com.game.common.util.ClassFactory;
import com.project.core.battle.buff.Buff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuffFeatureFactory {

	private static final Logger logger = LoggerFactory.getLogger(BuffFeatureFactory.class);

	private static final ClassFactory<BuffFeature> CLASS_FACTORY = new ClassFactory<>(BuffFeature.class, "_");

	public static IBuffFeature createInstance(Buff buff, BuffFeatureType featureType, JSONObject params) {
		BuffFeature feature = CLASS_FACTORY.createInstance(featureType.name());
		feature.initialize(buff, params);
		return feature;
	}
}
