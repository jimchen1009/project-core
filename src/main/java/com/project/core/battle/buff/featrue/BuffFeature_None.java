package com.project.core.battle.buff.featrue;

import com.alibaba.fastjson.JSONObject;

public class BuffFeature_None extends BuffFeature {

	@Override
	protected void initParams(JSONObject params) {
	}

	@Override
	public BuffFeatureType getFeatureType() {
		return BuffFeatureType.None;
	}
}
