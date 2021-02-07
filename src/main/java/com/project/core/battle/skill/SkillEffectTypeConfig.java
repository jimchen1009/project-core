package com.project.core.battle.skill;

import com.game.common.util.IEnumBase;
import com.project.config.DataConfigUtil;
import com.project.config.model.T_skill_effect_type;
import com.project.config.model.T_skill_effect_typeData;
import com.project.core.battle.skill.effect.SkillEffectType;
import com.project.core.config.data.DataConfigs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: skill_fight.xlsx[export_技能效果类型], 对应JSON文件: t_skill_effect_type.json
*
* 文件创建时间:2020-08-24 18:30:57 创建者:chenjingjun

*/

public class SkillEffectTypeConfig {

    private static Map<Integer, SkillEffectTypeConfig> configs;
    private static List<SkillEffectTypeConfig> configList;


    static {
        reloadConfig(DataConfigs.getDataSource(T_skill_effect_type.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_skill_effect_type dataSource){
        Map<Integer, SkillEffectTypeConfig> configMap = DataConfigUtil.reloadConfig(dataSource, SkillEffectTypeConfig.class, SkillEffectTypeConfig::getPrimaryId);

        List<SkillEffectTypeConfig> configList = configMap.values().stream().sorted(Comparator.comparing(SkillEffectTypeConfig::getPrimaryId))
        .collect(Collectors.toList());

        SkillEffectTypeConfig.configs = configMap;
        SkillEffectTypeConfig.configList = Collections.unmodifiableList(configList);

        StringBuilder builder = new StringBuilder();
        for (SkillEffectTypeConfig typeConfig : configList) {
            String format = String.format("%s(%s, \"%s\"),", typeConfig.data.getName(), typeConfig.getPrimaryId(), typeConfig.data.getComment());
            builder.append("\t").append(format).append("\n");
        }
        builder.append("\t").append(";");
        System.out.println(builder.toString());
    }

    public static List<SkillEffectTypeConfig> getAllConfigs() {
        return configList;
    }

    public static SkillEffectTypeConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static SkillEffectTypeConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static SkillEffectTypeConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }

    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_skill_effect_typeData data;
    private final SkillOccasion skillOccasion;
    private final SkillEffectType effectType;

    public SkillEffectTypeConfig(T_skill_effect_typeData data) {
        this.data = data;
        this.skillOccasion = IEnumBase.findOne(SkillOccasion.values(), data.getExecuteOccasion());
        this.effectType = IEnumBase.findOne(SkillEffectType.values(), data.getName());
    }

    public int getPrimaryId() {
        return data.getId();
    }

    public int getId(){
        return data.getId();
    }


    public SkillOccasion getSkillOccasion() {
        return skillOccasion;
    }

    public SkillEffectType getEffectType() {
        return effectType;
    }

    public boolean isHitOrResistEffect(){
        return data.isHitOrResistEffect();
    }

}
