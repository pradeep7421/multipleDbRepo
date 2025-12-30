package com.multipledb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.multipledb.componentFileDisableUATProcess.UATDisableFile;

@SpringBootApplication
//@ComponentScan(basePackages = "com.myproject", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UATDisableFile.class))
//@ComponentScan(basePackages = "com.myproject", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.myproject\\.util\\.UATDisableFile"))
@EnableFeignClients
@EnableCaching
//@EnableTransactionManagement // This is not needed if we have already defined TransactionManager in config class if this is added it means to enable twice which is ambigous might confuse spring which one to implement
public class MultipleDbExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleDbExampleApplication.class, args);
	}
	

}
