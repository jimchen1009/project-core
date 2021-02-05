package com.project.core.battle.attribute;

import com.project.config.DataConfigUtil;
import com.project.config.model.T_attribute;
import com.project.config.model.T_attributeData;
import com.project.core.config.data.DataConfigs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: attribute0.xlsx[export_裸体属性], 对应JSON文件: t_attribute.json
*
* 文件创建时间:2021-01-22 15:57:39 创建者:chenjingjun

*/

public class AttributeConfig {

    private static Map<Integer, AttributeConfig> configs;
    private static List<AttributeConfig> configList;

    static {
        reloadConfig(DataConfigs.getDataSource(T_attribute.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_attribute dataSource){
        Map<Integer, AttributeConfig> configMap = DataConfigUtil.reloadConfig(dataSource, AttributeConfig.class, AttributeConfig::getPrimaryId);

        List<AttributeConfig> configList = configMap.values().stream().sorted(Comparator.comparing(AttributeConfig::getPrimaryId))
        .collect(Collectors.toList());

        AttributeConfig.configs = configMap;
        AttributeConfig.configList = Collections.unmodifiableList(configList);
    }

    public static List<AttributeConfig> getAllConfigs() {
        return configList;
    }

    public static AttributeConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static AttributeConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static AttributeConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }

    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_attributeData data;

    public AttributeConfig(T_attributeData data) {
        this.data = data;
    }

    public int getPrimaryId() {
        return data.getId();
    }

    public int getId(){
        return data.getId();
    }

    public int getType(){
        return data.getType();
    }

    public int getLevel(){
        return data.getLevel();
    }

    public BaseAttributes createAttributes(){
        BaseAttributes attributes = new BaseAttributes();
        attributes.set(Attribute.hp, data.getHp());
        attributes.set(Attribute.thp, data.getHp());
        attributes.set(Attribute.atk, data.getAtk());
        attributes.set(Attribute.def, data.getDef());
        attributes.set(Attribute.critic, data.getCritic());
        attributes.set(Attribute.critic_dmg, data.getCritic_dmg());
        attributes.set(Attribute.dodge, data.getDodge());
        attributes.set(Attribute.effect_hit, data.getEffect_hit());
        attributes.set(Attribute.effect_resist, data.getEffect_resist());
        return attributes;
    }
}
