package com.trace.file.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
/**
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 16:10:25
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.trace.file.repository",
    entityManagerFactoryRef = "traceFileEntityManagerFactory",
    transactionManagerRef = "traceFileTransactionManager"
)
@EntityScan(basePackages = "com.trace.file.entity")
public class OracleConfig {
	
	@Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
	@Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

//    @Value("${spring.datasource.hikari.maximum-pool-size}")
//    private int maxPoolSize;
    
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("oracle.jdbc.OracleDriver")
                .url("jdbc:oracle:thin:@localhost:1521/XEPDB1")
                .username("NEXTLEVEL_FILE")
                .password("admin")
                .build();
    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("com.trace.file");
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return em;
//    }
    
    @Bean(name = "traceFileEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean traceFileEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
//            @Qualifier("traceBankDataSource") 
            DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.trace.file.entity")
                .persistenceUnit("traceFile")
                .build();
    }

    @Bean(name = "traceFileTransactionManager")
    public PlatformTransactionManager traceFileTransactionManager(
            @Qualifier("traceFileEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}
