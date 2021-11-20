package io.mysmarthome.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(
            @Value("${dbHost}") String dbHost,
            @Value("${username}") String username,
            @Value("${password}") String password
    ) {
        log.info("Setting up database {}, user {}", dbHost, username);
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url(dbHost)
                .username(username)
                .password(password)
                .build();
    }
}
