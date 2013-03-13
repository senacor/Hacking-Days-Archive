package com.senacor.hd12.network;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Ralph Winzinger, Senacor
 */
public class Launcher {
    // static ConfigurableApplicationContext applicationContext = null;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("neoContext-standalone.xml");
        TheSenacorNetwork tsn = applicationContext.getBean(TheSenacorNetwork.class);

        tsn.startNetworkBuilding();
    }


    /*
    @Override
    protected void finalize() throws Throwable {
        System.out.println("shutting down server");
        applicationContext.close();
        super.finalize();
    }
    */
}
