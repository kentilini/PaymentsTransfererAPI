package com.kentilini.server.jetty;

import com.kentilini.server.entity.EntityManagerService;
import com.kentilini.server.service.UserService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserService.class);
        bind(EntityManagerService.class).to(EntityManagerService.class);
    }
}
