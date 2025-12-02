package com.multipledb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.multipledb.componentFileDisableUATProcess.UATDisableFile;

@SpringBootApplication
//@ComponentScan(basePackages = "com.myproject", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UATDisableFile.class))
//@ComponentScan(basePackages = "com.myproject", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.myproject\\.util\\.UATDisableFile"))
@EnableFeignClients
public class MultipleDbExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleDbExampleApplication.class, args);
	}
	

}
