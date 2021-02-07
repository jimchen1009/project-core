package com.project.core.battle.buff;

import com.alibaba.fastjson.JSONObject;
import com.game.common.util.IEnumBase;
import com.project.config.DataConfigUtil;
import com.project.config.model.T_skill_buff;
import com.project.config.model.T_skill_buffData;
import com.project.core.battle.buff.featrue.BuffFeatureFactory;
import com.project.core.battle.buff.featrue.BuffFeatureType;
import com.project.core.battle.buff.featrue.IBuffFeature;
import com.project.core.battle.condition.ConditionConfig;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.condition.IConditionConfig;
import com.project.core.battle.condition.IConditionHandler;
import com.project.core.config.data.DataConfigs;
import jodd.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: skill_fight.xlsx[export_buff], 对应JSON文件: t_skill_buff.json
*
* 文件创建时间:2020-08-24 14:32:49 创建者:chenjingjun

*/

public class BuffConfig implements IConditionConfig {

    private static Map<Integer, BuffConfig> configs;
    private static List<BuffConfig> configList;

    static {
        reloadConfig(DataConfigs.getDataSource(T_skill_buff.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_skill_buff dataSource){
        Map<Integer, BuffConfig> configMap = DataConfigUtil.reloadConfig(dataSource, BuffConfig.class, BuffConfig::getPrimaryId);

        List<BuffConfig> configList = configMap.values().stream().sorted(Comparator.comparing(BuffConfig::getPrimaryId))
        .collect(Collectors.toList());

        BuffConfig.configs = configMap;
        BuffConfig.configList = Collections.unmodifiableList(configList);
    }

    public static List<BuffConfig> getAllConfigs() {
        return configList;
    }

    public static BuffConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static BuffConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static BuffConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }

    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_skill_buffData data;
    private final ConditionConfig conditionConfig;

    public BuffConfig(T_skill_buffData data) {
        this.data = data;
        this.conditionConfig = ConditionConfig.getConfig(data.getCondition());
    }

    @Override
    public ConditionType getConditionType() {
        return conditionConfig.getConditionType();
    }

    @Override
    public IConditionHandler getConditionHandler() {
        return conditionConfig.getConditionHandler();
    }

    public List<IBuffFeature> createFeatureList(Buff buff) {
        return createBuffFeatures(buff, data.getFeatures());
    }

    private List<IBuffFeature> createBuffFeatures(Buff buff, String features){
        if (StringUtil.isEmpty(features)) {
            return Collections.emptyList();
        }
        List<JSONObject> featureParams = JSONObject.parseArray(features, JSONObject.class);
        if (featureParams.isEmpty()){
            return Collections.emptyList();
        }
        List<IBuffFeature> featureList = new ArrayList<>();
        for (JSONObject featureParam : featureParams) {
            String type = featureParam.getString("type");
            BuffFeatureType featureType = IEnumBase.findOne(BuffFeatureType.values(), type);
            if (featureType == null) {
                featureType = BuffFeatureType.None;
            }
            IBuffFeature feature = BuffFeatureFactory.createInstance(buff, featureType, featureParam);
            featureList.add(feature);
        }
        return Collections.unmodifiableList(featureList);
    }

    public int getPrimaryId() {
        return data.getId();
    }

    public int getId(){
        return data.getId();
    }

    public String getEffectId(){
        return data.getEffectId();
    }

    public String getName(){
        return data.getName();
    }

    public int buffGenera(){
        return data.getGeneraId();
    }

    public int getType(){
        return data.getType();
    }

    public int getValue(){
        return data.getValue();
    }

    public int getMaxRound(){
        return data.getMaxRound();
    }
}
