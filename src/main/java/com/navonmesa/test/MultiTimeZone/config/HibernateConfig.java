//package com.realnet.MultiTimeZone.config;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.spi.MetadataImplementor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.realnet.MultiTimeZone.Component.WhoColumnInterceptor;
//
//@Configuration
//public class HibernateConfig {
//
//    private final WhoColumnInterceptor whoColumnInterceptor;
//
//    public HibernateConfig(WhoColumnInterceptor whoColumnInterceptor) {
//        this.whoColumnInterceptor = whoColumnInterceptor;
//    }
//
//    @Bean
//    public SessionFactory sessionFactory(MetadataImplementor metadata) {
//        return metadata.getSessionFactoryBuilder()
//                .applyInterceptor(whoColumnInterceptor)
//                .build();
//    }
//}
