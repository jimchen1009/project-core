package com.project.core.battle.condition;

import com.project.config.DataConfigUtil;

import com.project.config.model.T_skill_conditionData;
import com.project.config.model.T_skill_condition;
import com.project.core.config.data.DataConfigs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.lang.String;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: skill_fight.xlsx[export_触发条件], 对应JSON文件: t_skill_condition.json
*
* 文件创建时间:2020-12-02 16:22:37 创建者:chenjingjun

*/

public class SkillConditionConfig {

    private static Map<Integer, SkillConditionConfig> configs;
    private static List<SkillConditionConfig> configList;

    static {
        reloadConfig(DataConfigs.getDataSource(T_skill_condition.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_skill_condition dataSource){
        Map<Integer, SkillConditionConfig> configMap = DataConfigUtil.reloadConfig(dataSource, SkillConditionConfig.class, SkillConditionConfig::getPrimaryId);

        List<SkillConditionConfig> configList = configMap.values().stream().sorted(Comparator.comparing(SkillConditionConfig::getPrimaryId))
        .collect(Collectors.toList());

        SkillConditionConfig.configs = configMap;
        SkillConditionConfig.configList = Collections.unmodifiableList(configList);
    }

    public static List<SkillConditionConfig> getAllConfigs() {
        return configList;
    }

    public static SkillConditionConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static SkillConditionConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static SkillConditionConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }

    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_skill_conditionData data;

    public SkillConditionConfig(T_skill_conditionData data) {
        this.data = data;
    }

    public int getPrimaryId() {
        return data.getId();
    }

    public int getId(){
        return data.getId();
    }

    public String getComment(){
        return data.getComment();
    }

    public String getType(){
        return data.getType();
    }

    public String getParam(){
        return data.getParam();
    }

}
