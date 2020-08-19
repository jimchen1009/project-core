package com.project.core.config.data;

import com.project.config.IDataSource;
import com.project.config.model.T_demo;
import com.project.config.model.T_demo_main;
    import com.project.config.model.ext.DemoConfig;
    import com.project.config.model.ext.DemoMainConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
* 版本:1.0  MODIFICATION IS NOT ALLOWED
*
* 文件创建时间:2020-08-19 16:18:43 创建者:chenjingjun

*/
public class DataMapper {

    private static final Logger logger = LoggerFactory.getLogger(DataMapper.class);

    private static final Map<String, ConfigClass> name2ConfigClass = new HashMap<>();

    static {
        addClass(T_demo.class, DemoConfig.class);
        addClass(T_demo_main.class, DemoMainConfig.class);
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

    static void onDataSourceReload(IDataSource dataSource) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConfigClass configClass = name2ConfigClass.get(dataSource.getName());
        if (configClass == null){
            return;
        }
        Method method = configClass.getConfigClass().getMethod("reloadConfig", dataSource.getClass());
        method.invoke(null, dataSource);
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

