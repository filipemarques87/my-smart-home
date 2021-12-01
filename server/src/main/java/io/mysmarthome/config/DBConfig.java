package io.mysmarthome.config;

import io.mysmarthome.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Objects;

@Slf4j
@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(
            @Value("${dbHost:#{null}}") String dbHost,
            @Value("${username:#{null}}") String username,
            @Value("${password:#{null}}") String password,
            @Value("${jdbcDriver:#{null}}") String jdbcDriver
    ) {
        if (StringUtils.isBlank(dbHost)) {
            return buildH2DataSource();
        }

        log.info("Setting up database {}, user {}", dbHost, username);
        return DataSourceBuilder.create()
                .driverClassName(jdbcDriver)
                .url(dbHost)
                .username(username)
                .password(password)
                .build();
    }

    private DataSource buildH2DataSource() {
        log.info("Setting up h2 database");
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:file:" + AppConstants.DATA_FOLDER + "/database")
                .username("sa")
                .password("")
                .build();
    }
}
// -DdeviceFile=/home/filipe/my-smart-home/config/devices.yaml -DconfigurationFile=/home/filipe/my-smart-home/config/configuration.properties -DdbHost=jdbc:h2:file:/home/filipe/my-smart-home/data/database -Dusername=sa -Dpassword= -DlogLevel=INFO -DlogPath=/home/filipe/my-smart-home/logs -DfirebaseConfigFile=/home/filipe/my-smart-home/config/smarthome-938ce-firebase-adminsdk-lwxdi-7302b50627.json -DplatformsFolder=/home/filipe/my-smart-home/platforms
