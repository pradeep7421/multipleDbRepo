package com.multipledb.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.multipledb.bookRepo", 
entityManagerFactoryRef = "bookEntityManagerFactory", 
transactionManagerRef = "bookTransactionManager")
public class BookDbConfig {

	@Primary
	@Bean(name = "bookDataSource")
	@ConfigurationProperties(prefix = "spring.book.datasource")
	public DataSource bookDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "bookEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean bookEntityManagerFactory(
			@Qualifier("bookDataSource") DataSource dataSource) {

		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPackagesToScan("com.multipledb.bookModel");
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Map<String,Object> map = new HashMap<>();
		map.put("hibernate.hbm2ddl.auto", "update");
      	map.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
      	map.put("hibernate.show-sql", true);
      	emf.setJpaPropertyMap(map);
		return emf;
	}

	@Primary
	@Bean(name = "bookTransactionManager")
	public PlatformTransactionManager bookTransactionManager(
			@Qualifier("bookEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
		return new JpaTransactionManager(emf.getObject());
	}

}
