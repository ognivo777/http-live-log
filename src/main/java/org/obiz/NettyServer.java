package org.obiz;


import io.quarkus.runtime.Startup;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class NettyServer {

    @PostConstruct
    void init() {

    }

}
