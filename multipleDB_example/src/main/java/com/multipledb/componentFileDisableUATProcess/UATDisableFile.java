package com.multipledb.componentFileDisableUATProcess;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!uat") // It will load in every env except in UAT env
//@Profile("!uat & !prod") //And condition is activated
//@Profile("uat | prod")  // OR condition activated
public class UATDisableFile {

}
