package com.project.core.battle.buff;

import com.game.common.util.IEnumBase;
import com.project.config.DataConfigUtil;
import com.project.config.model.T_skill_buff_type;
import com.project.config.model.T_skill_buff_typeData;
import com.project.core.battle.buff.type.BuffStrategy;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.config.data.DataConfigs;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: skill_fight.xlsx[export_buff效果类型], 对应JSON文件: t_skill_buff_type.json
*
* 文件创建时间:2020-08-24 14:32:49 创建者:chenjingjun

*/

public class BuffTypeConfig {

    private static Map<Integer, BuffTypeConfig> configs;
    private static List<BuffTypeConfig> configList;

    static {
        reloadConfig(DataConfigs.getDataSource(T_skill_buff_type.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_skill_buff_type dataSource){
        Map<Integer, BuffTypeConfig> configMap = DataConfigUtil.reloadConfig(dataSource, BuffTypeConfig.class, BuffTypeConfig::getPrimaryId);

        List<BuffTypeConfig> configList = configMap.values().stream().sorted(Comparator.comparing(BuffTypeConfig::getPrimaryId))
        .collect(Collectors.toList());

        BuffTypeConfig.configs = configMap;
        BuffTypeConfig.configList = Collections.unmodifiableList(configList);
    }

    public static List<BuffTypeConfig> getAllConfigs() {
        return configList;
    }

    public static BuffTypeConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static BuffTypeConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static BuffTypeConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }

    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_skill_buff_typeData data;
    private final BuffStrategy strategy;

    private EnumMap<BuffCasterType, BuffDecPoint> casterType2DecPointMap;

    public BuffTypeConfig(T_skill_buff_typeData data) {
        this.data = data;
        this.strategy = IEnumBase.findOne(BuffStrategy.values(), data.getStrategy());
        this.casterType2DecPointMap = new EnumMap<>(BuffCasterType.class);

        initializeBuffDecPoint(BuffCasterType.Own, data.getFromSelfDecRoundPoint());
        initializeBuffDecPoint(BuffCasterType.TeamMate, data.getFromTeamMateDecRoundPoint());
        initializeBuffDecPoint(BuffCasterType.Enemy, data.getFromEnemyDecRoundPoint());
    }

    private void initializeBuffDecPoint(BuffCasterType casterType, int decPointId) {
        BuffDecPoint buffDecPoint = IEnumBase.findOne(BuffDecPoint.values(), decPointId);
        if (buffDecPoint == null) {
            buffDecPoint = BuffDecPoint.NoDec;
        }
        this.casterType2DecPointMap.put(casterType, buffDecPoint);
    }

    public int getPrimaryId() {
        return data.getId();
    }

    public int getId(){
        return data.getId();
    }

    public BuffStrategy getStrategy(){
        return strategy;
    }

    public List<Integer> getExcludeTypes(){
        return data.getExcludeTypes() == null ? Collections.emptyList() : data.getExcludeTypes();
    }

    public List<Integer>  getRejectTypes(){
        return data.getRejectTypes() == null ? Collections.emptyList() : data.getRejectTypes();
    }

    public boolean isCanRemove(){
        return data.isCanRemove();
    }

    public boolean getCanChangeRound(){
        return data.isCanChangeRound();
    }

    public boolean isCanIgnore(){
        return data.isCanIgnore();
    }

    public BuffDecPoint getBuffDecPoint(BuffCasterType casterType) {
        return casterType2DecPointMap.getOrDefault(casterType, BuffDecPoint.EndRound);
    }
}
