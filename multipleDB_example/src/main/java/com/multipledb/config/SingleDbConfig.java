//package com.multipledb.config;
//
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.zaxxer.hikari.HikariDataSource;
//
///*
// *USE THIS CLASS IF WE ARE USING SINGLE DATABASE AND WANT TO CONFIGURE DATASOURCE
// *FOR SINGLE DATABASE WE CAN DELETE THIS CLASS HIBBERNATE WILL MAKE ITS OWN DATASOURCE & ENTITY MANAGER
//*/
//@Configuration
//@EnableConfigurationProperties(DataSourceProperties.class)
//@EnableJpaRepositories(
//        basePackages = "com.multipledb.bookRepo",
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "transactionManager"
//)
//public class SingleDbConfig {
//
//    @Bean
//    @Primary
//    public DataSource dataSource(DataSourceProperties properties) {
//        return properties
//                .initializeDataSourceBuilder()
//                .type(HikariDataSource.class)
//                .build();
//    }
//
//    @Bean(name = "entityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//           DataSource dataSource
//    ) {
//
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource);
//        emf.setPackagesToScan("com.multipledb.bookModel");
//        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        Map<String,Object> map = new HashMap<>();
//        map.put("hibernate.hbm2ddl.auto", "update");
////      map.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect"); //hibernate auto detects MySQLDialect dialect if url points to mysql
//        map.put("hibernate.show-sql", true);
//        map.put("hibernate.format_sql", true); // optional, makes it readable
//        map.put("hibernate.use_sql_comments", true); // optional, adds comments
//        
//       
////        map.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
////        map.put("jakarta.persistence.jdbc.url", "jdbc:mysql://localhost:3306/book_db");
////        map.put("jakarta.persistence.jdbc.user", "root");
////        map.put("jakarta.persistence.jdbc.password", "root");
//        emf.setJpaPropertyMap(map);
//
//
//        emf.setJpaPropertyMap(map);
//        
//        return emf;
//    }
//
//    @Bean(name = "transactionManager")
//    @Primary
//    public PlatformTransactionManager transactionManager(
//    		@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean emf
//    ) {
//        return new JpaTransactionManager(emf.getObject());
//    }
//}
