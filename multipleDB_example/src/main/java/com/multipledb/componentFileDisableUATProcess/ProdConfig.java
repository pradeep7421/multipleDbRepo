package com.multipledb.componentFileDisableUATProcess;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@Profile("!dev & !qa & !uat")  //assume spring.profiles.active=prod in app.properties file gets 
@Profile("prod")
public class ProdConfig {

}
