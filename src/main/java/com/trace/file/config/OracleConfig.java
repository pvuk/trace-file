package com.trace.file.config;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;

/**
 * Oracle R2DBC configuration
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 25-August-2026 16:10:25
 */
@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "com.trace.file.repository")
public class OracleConfig {

    @Value("${spring.r2dbc.url}")
    private String url;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(DRIVER, "oracle")
                .option(HOST, "localhost")
                .option(PORT, 1521)
                .option(DATABASE, "XEPDB1")
                .option(USER, username)
                .option(PASSWORD, password)
                .build()
        );
    }


    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
    
    /**
     * CodeReference: Interview:
     * 
     * Switch to R2dbcEntityTemplate 
     * 
     * JdbcTemplate belongs to Spring JDBC (blocking, synchronous).
	 * You are using Spring Data R2DBC (reactive, non-blocking).
     * @author PULIPATI VENKATA UDAYKIRAN
     * @since Tuesday 15-September-2026 19:27:29
     * @param connectionFactory
     * @return
     */
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

}
