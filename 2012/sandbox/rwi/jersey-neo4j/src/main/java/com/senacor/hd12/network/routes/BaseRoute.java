package com.senacor.hd12.network.routes;

import com.senacor.hd12.neo.engine.NetworkRepositoryImpl;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public abstract class BaseRoute /*extends Route*/ {
    protected ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired
    protected NetworkRepositoryImpl senacor;

    public BaseRoute(String path) {
        //super(path);
    }
}
