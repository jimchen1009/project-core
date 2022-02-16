package com.project.core.battle.skill;

import com.game.common.util.CommonUtil;
import com.game.common.util.IEnumBase;
import com.project.config.DataConfigUtil;
import com.project.config.model.T_skill;
import com.project.config.model.T_skillData;
import com.project.core.battle.condition.ConditionConfig;
import com.project.core.battle.condition.ConditionType;
import com.project.core.battle.selector.BattleUnitSelectorType;
import com.project.core.battle.selector.IBattleUnitSelector;
import com.project.core.battle.skill.effect.ISkillEffectHandler;
import com.project.core.battle.skill.effect.SkillEffectHandlerFactory;
import com.project.core.battle.skill.effect.SkillEffectType;
import com.project.core.config.data.DataConfigs;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 原文件: skill_fight.xlsx[export_技能], 对应JSON文件: t_skill.json
*
* 文件创建时间:2020-08-24 14:32:48 创建者:chenjingjun

*/

public class SkillConfig {

    private static final Logger logger = LoggerFactory.getLogger(SkillConfig.class);

    private static Map<Integer, SkillConfig> configs;
    private static List<SkillConfig> configList;

    static {
        reloadConfig(DataConfigs.getDataSource(T_skill.class));
    }

    public static void touch(){

    }

    public static void reloadConfig(T_skill dataSource){
        Map<Integer, SkillConfig> configMap = DataConfigUtil.reloadConfig(dataSource, SkillConfig.class, SkillConfig::getPrimaryId);

        List<SkillConfig> configList = configMap.values().stream().sorted(Comparator.comparing(SkillConfig::getPrimaryId))
        .collect(Collectors.toList());

        SkillConfig.configs = configMap;
        SkillConfig.configList = Collections.unmodifiableList(configList);
    }

    public static List<SkillConfig> getAllConfigs() {
        return configList;
    }

    public static SkillConfig getConfig(int primaryKey){
        return configs.get(primaryKey);
    }

    public static SkillConfig getFirstConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(0);
    }

    public static SkillConfig getLastConfig(int primaryId){
        return configList.isEmpty() ? null : configList.get(configList.size() - 1);
    }

    /**
    * ==================================== 包装类 ========================================================
    * 1. 包装类GET方法根据业务需要自己改动
    *
    * **/

    private final T_skillData data;
    private final IBattleUnitSelector selectUnitSelector;
    private final List<SkillEffectConfig> effectConfigList;
    private final Map<ConditionType, List<SkillEffectConfig>> condition2EffectList;
    private final Map<SkillEffectType, List<SkillEffectConfig>> effect2EffectList;

    public SkillConfig(T_skillData data) {
        this.data = data;
        this.selectUnitSelector = IEnumBase.findOne(BattleUnitSelectorType.values(), data.getEffectTarget());
        this.effectConfigList = createEffectConfigs(data);
        this.condition2EffectList = CommonUtil.splitUp1Into1Group(new EnumMap<>(ConditionType.class), effectConfigList, ArrayList::new, SkillEffectConfig::getSkillConditionType);
        this.effect2EffectList = CommonUtil.splitUp1IntoNGroup(new EnumMap<>(SkillEffectType.class), effectConfigList, ArrayList::new,
                config -> config.getEffectUnitConfigs().stream().map(SkillEffectUnitConfig::getEffectType).collect(Collectors.toList()));
    }

    public IBattleUnitSelector getSelectUnitSelector() {
        return selectUnitSelector;
    }

    public List<SkillEffectConfig> getEffectConfigs() {
        return effectConfigList;
    }

    public Collection<SkillEffectConfig> getEffectConfigs(ConditionType conditionType) {
        return condition2EffectList.getOrDefault(conditionType, Collections.emptyList());
    }

    public Collection<SkillEffectConfig> getEffectConfigs(SkillEffectType effectType) {
        return effect2EffectList.getOrDefault(effectType, Collections.emptyList());
    }

    private List<SkillEffectConfig> createEffectConfigs(T_skillData data){
        List<SkillEffectConfig> effectConfigList = new ArrayList<>();
        createEffectConfig(1, data.getId(), data.getCondition1(), data.getEffect1(), data.getEffectRate1(), data.getEffectTarget1(), effectConfigList);
        createEffectConfig(2, data.getId(), data.getCondition2(), data.getEffect2(), data.getEffectRate2(), data.getEffectTarget2(), effectConfigList);
        createEffectConfig(3, data.getId(), data.getCondition3(), data.getEffect3(), data.getEffectRate3(), data.getEffectTarget3(), effectConfigList);
        createEffectConfig(4, data.getId(), data.getCondition4(), data.getEffect4(), data.getEffectRate4(), data.getEffectTarget4(), effectConfigList);
        createEffectConfig(5, data.getId(), data.getCondition4(), data.getEffect5(), data.getEffectRate5(), data.getEffectTarget5(), effectConfigList);
        return effectConfigList;
    }

    private void createEffectConfig(int index, int skillId, int conditionId, String effectString, int effectRate, int selectorId, List<SkillEffectConfig> effectConfigList) {
        if (StringUtil.isEmpty(effectString)) {
            return;
        }
        ConditionConfig conditionConfig = ConditionConfig.getConfig(conditionId);
        if (conditionConfig == null){
            return;
        }
        IBattleUnitSelector battleUnitSelector = IEnumBase.findOne(BattleUnitSelectorType.values(), selectorId);
        if (battleUnitSelector == null){
            return;
        }
        String[] effectStrings = effectString.split("\\|");
        List<SkillEffectUnitConfig> effectUnitConfigList = new ArrayList<>(effectStrings.length);
        for (String effectString0 : effectStrings) {
            String[] strings = effectString0.split("#", 2);
            int effectTypeId = Integer.parseInt(strings[0]);
            String param = strings.length > 1 ? strings[1] : null;
            SkillEffectTypeConfig typeConfig = SkillEffectTypeConfig.getConfig(effectTypeId);
            if (typeConfig == null){
                continue;
            }
            SkillEffectType effectType = typeConfig.getEffectType();
            if (effectType == null) {
                continue;
            }
            ISkillEffectHandler effectHandler = SkillEffectHandlerFactory.newInstance(effectType, skillId, param);
            if (effectHandler == null){
                continue;
            }
            SkillEffectUnitConfig effectUnitConfig = new SkillEffectUnitConfig(typeConfig, effectHandler);
            effectUnitConfigList.add(effectUnitConfig);
        }
        if (effectUnitConfigList.isEmpty()){
            return;
        }
        effectConfigList.add(new SkillEffectConfig(index, skillId, conditionConfig, effectRate, battleUnitSelector, effectUnitConfigList));
    }


    public int getPrimaryId() {
        return data.getId();
    }

    public int getId(){
        return data.getId();
    }

    public boolean isSuperSkill(){
        return data.isSuperSkill();
    }

    public boolean getIsAoe(){
        return data.getIsAoe() == 1;
    }

    public boolean isActiveSkill(){
        return data.getType() == 0;
    }

    public boolean isPassiveSkill(){
        return !isActiveSkill();
    }

    public int getInitCd(){
        return data.getInitCd();
    }

    public int getCd(){
        return data.getCd();
    }

    public boolean isIgnoreChangeCD(){
        return data.isIgnoreChangeCD();
    }

    public int getDamageRate(){
        return data.getDamageRate();
    }

    public boolean getIsAura(){
        return data.getIsAura() == 1;
    }

    public boolean isDoAction(){
        return !data.isNotDoAction();
    }

    public int getChangeSkillId(){
        return data.getChangeSkillId();
    }
}
