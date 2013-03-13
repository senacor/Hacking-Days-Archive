package com.senacor.hd12.network.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {
    private static SpringApplicationContext ourInstance = new SpringApplicationContext();

    private static ApplicationContext applicationContext;

    public static SpringApplicationContext getInstance() {
        return ourInstance;
    }

    private SpringApplicationContext() {
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
    
    public static Object getBean(Class clazz) {
        return applicationContext.getBean(clazz);
    }
}
