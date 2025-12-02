package com.multipledb.componentFileDisableUATProcess;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@Profile("!dev & !qa & !prod") //assume spring.profiles.active=uat in app.properties file gets 
@Profile("uat")
public class UatConfig {
	
//spring.profiles.active=uat,prod  can have 2 values
	
	
//	A bean with @Profile("!uat") is active only if none of the active profiles is uat
//	        | Active Profiles | @Profile("!uat") Active? |
//			| --------------- | ------------------------ |
//			| dev             | ✔ Yes                    |
//			| prod            | ✔ Yes                    |
//			| test            | ✔ Yes                    |
//			| qa              | ✔ Yes                    |
//			| uat             | ❌ No                     |
//			| uat,prod        | ❌ No                     |  with & condition within env
//			| dev,uat         | ❌ No                     |
//			| local,uat,prod  | ❌ No                     |
	

	
	
	//@Profile("!prod & !uat")

//	        | Active Profiles | !prod | !uat  | !prod & !uat | Bean Loaded? |
//			| --------------- | ----- | ----- | ------------ | ------------ |
//			| dev             | true  | true  | true         | ✔ YES        |
//			| qa              | true  | true  | true         | ✔ YES        |
//			| prod            | false | true  | false        | ❌ NO         |
//			| uat             | true  | false | false        | ❌ NO         |
//			| prod, uat       | false | false | false        | ❌ NO         |
//			| dev, prod       | false | true  | false        | ❌ NO         |
//			| dev, uat        | true  | false | false        | ❌ NO         |
//			| dev, qa         | true  | true  | true         | ✔ YES        |


}
