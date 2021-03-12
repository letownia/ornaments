package com.mapdecorator.config;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

@Component
public class PropertyLogger  {

    private static final boolean FILTER_CREDENTIALS = false;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @EventListener
    public static void handleContextRefresh(ContextRefreshedEvent event) {
        logEnv( event.getApplicationContext().getEnvironment());
    }
    public static void logEnv(Environment env){
        logger.info("====== Environment and configuration ======");
        logger.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
        final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(sources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .filter( prop -> !FILTER_CREDENTIALS || !(prop.toLowerCase().contains("credentials") || prop.toLowerCase().contains("password")))
                .forEach(prop -> logger.info("{}: {}", prop, env.getProperty(prop)));
        logger.info("===========================================");
    }
}
