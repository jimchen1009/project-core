package com.project.core.config.data;

import com.project.config.IDataSource;
import com.project.config.model.T_skill;
import com.project.config.model.T_skill_buff;
import com.project.config.model.T_skill_buff_type;
import com.project.config.model.T_skill_condition;
import com.project.config.model.T_skill_effect_type;
import com.project.core.battle.buff.BuffConfig;
import com.project.core.battle.buff.BuffTypeConfig;
import com.project.core.battle.condition.ConditionConfig;
import com.project.core.battle.skill.SkillConfig;
import com.project.core.battle.skill.SkillEffectTypeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 文件创建时间:2020-08-24 18:33:03 创建者:chenjingjun

*/
public class DataMapper {

    private static final Logger logger = LoggerFactory.getLogger(DataMapper.class);

    private static final Map<String, ConfigClass> name2ConfigClass = new HashMap<>();

    static {
        addClass(T_skill.class, SkillConfig.class);
        addClass(T_skill_buff.class, BuffConfig.class);
        addClass(T_skill_buff_type.class, BuffTypeConfig.class);
        addClass(T_skill_condition.class, ConditionConfig.class);
        addClass(T_skill_effect_type.class, SkillEffectTypeConfig.class);
    }

    public static Map<String, ConfigClass> getName2ConfigClass() {
        return new HashMap<>(name2ConfigClass);
    }

    private static void addClass(Class<? extends IDataSource> dataClass, Class<?> configClass){
        try {
            IDataSource dataSource = dataClass.newInstance();
            String name = dataSource.getName();
            name2ConfigClass.put(name, new ConfigClass(name, dataClass, configClass));
        }
        catch (InstantiationException | IllegalAccessException e) {
            logger.error("dataClass:{} configClass:{}", dataClass.getName(), configClass.getName(), e);
        }
    }

    static void reloadDataSource(IDataSource dataSource)  {
        ConfigClass configClass = name2ConfigClass.get(dataSource.getName());
        if (configClass == null){
            return;
        }
        try {
            Method method = configClass.getConfigClass().getMethod("reloadConfig", dataSource.getClass(), String.class);
            method.invoke(null, dataSource, "RELOAD");
        }
        catch (Throwable e) {
            logger.error("dataClass:{} ConfigClass:{}", configClass.getName(), configClass.getName(), e);
        }
    }

    static void touchDataSource(IDataSource dataSource)  {
        ConfigClass configClass = name2ConfigClass.get(dataSource.getName());
        if (configClass == null){
            return;
        }
        try {
            Method method = configClass.getConfigClass().getMethod("touch");
            method.invoke(null);
        }
        catch (Throwable e) {
            logger.error("dataClass:{} ConfigClass:{}", configClass.getName(), configClass.getName(), e);
        }
    }

    public static class ConfigClass {

        private final String name;
        private final Class<? extends IDataSource> dataClass;
        private final Class<?> configClass;

        public ConfigClass(String name, Class<? extends IDataSource> dataClass, Class<?> configClass) {
            this.name = name;
            this.dataClass = dataClass;
            this.configClass = configClass;
        }

        public String getName() {
            return name;
        }

        public Class<? extends IDataSource> getDataClass() {
            return dataClass;
        }

        public Class<?> getConfigClass() {
            return configClass;
        }
    }
}

