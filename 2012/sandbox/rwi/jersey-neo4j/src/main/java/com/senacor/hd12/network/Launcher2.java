package com.senacor.hd12.network;

import com.senacor.hd12.neo.model.Person;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Ralph Winzinger, Senacor
 */
public class Launcher2 {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("neoContext-standalone.xml");
        TheSenacorNetwork tsn = applicationContext.getBean(TheSenacorNetwork.class);

        tsn.startNetworkBuilding();
        tsn.dumpNetwork();
    }
}
