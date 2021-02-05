package com.project.core.battle.condition;

import com.project.config.DataConfigUtil;
import com.project.config.model.T_skill_condition;
import com.project.config.model.T_skill_conditionData;
import com.project.core.config.data.DataConfigs;
import jodd.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: skill_fight.xlsx[export_触发条件], 对应JSON文件: t_skill_condition.json
*
* 文件创建时间:2020-08-24 14:32:49 创建者:chenjingjun

*/

public class ConditionConfig implements IConditionConfig {


    private static Map<Integer, ConditionConfig> configs;
    private static List<ConditionConfig> configList;

    static {
        reloadConfig(DataConfigs.getDataSource(T_skill_condition.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_skill_condition dataSource){
//        Map<String, String> stringMap = new LinkedHashMap<>();
//        for (T_skill_conditionData data : dataSource.getDataList()) {
//            if (stringMap.containsKey(data.getDamageType())) {
//                continue;
//            }
//            String format = String.format("\t%s(\"%s\"),", data.getDamageType(), data.getComment());
//            stringMap.put(data.getDamageType(), format);
//        }
//        System.out.println(StringUtil.join(stringMap.values(), "\n"));

        Map<Integer, ConditionConfig> configMap = DataConfigUtil.reloadConfig(dataSource, ConditionConfig.class, ConditionConfig::getPrimaryId);

        List<ConditionConfig> configList = configMap.values().stream().sorted(Comparator.comparing(ConditionConfig::getPrimaryId))
        .collect(Collectors.toList());

        ConditionConfig.configs = configMap;
        ConditionConfig.configList = Collections.unmodifiableList(configList);
    }

    public static List<ConditionConfig> getAllConfigs() {
        return configList;
    }

    public static ConditionConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static ConditionConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static ConditionConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }


    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_skill_conditionData data;
    private final IConditionHandler conditionHandler;

    public ConditionConfig(T_skill_conditionData data) {
        this.data = data;
        ConditionType conditionType = ConditionType.valueOf(data.getType());
        this.conditionHandler = ConditionHandlerFactory.newInstance(conditionType, data.getParam());
    }

    public int getPrimaryId() {
        return data.getId();
    }

    @Override
    public ConditionType getConditionType(){
        return conditionHandler.getConditionType();
    }

    @Override
    public IConditionHandler getConditionHandler() {
        return conditionHandler;
    }
}
