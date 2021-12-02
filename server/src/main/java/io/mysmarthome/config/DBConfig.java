package io.mysmarthome.config;

import io.mysmarthome.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(AppProperties appProperties) {
        if (StringUtils.isBlank(appProperties.getDbHost())) {
            return buildH2DataSource(appProperties);
        }

        log.info("Setting up database {}, user {}", appProperties.getDbHost(), appProperties.getDbUserName());
        return DataSourceBuilder.create()
                .driverClassName(appProperties.getDbJdbcDriver())
                .url(appProperties.getDbHost())
                .username(appProperties.getDbUserName())
                .password(appProperties.getDbPassword())
                .build();
    }

    private DataSource buildH2DataSource(AppProperties appProperties) {
        log.info("Setting up h2 database");
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:file:" + appProperties.getDataFolder() + "/database")
                .username("sa")
                .password("")
                .build();
    }
}
// -DdeviceFile=/home/filipe/my-smart-home/config/devices.yaml -DconfigurationFile=/home/filipe/my-smart-home/config/configuration.properties -DdbHost=jdbc:h2:file:/home/filipe/my-smart-home/data/database -Dusername=sa -Dpassword= -DlogLevel=INFO -DlogPath=/home/filipe/my-smart-home/logs -DfirebaseConfigFile=/home/filipe/my-smart-home/config/smarthome-938ce-firebase-adminsdk-lwxdi-7302b50627.json -DplatformsFolder=/home/filipe/my-smart-home/platforms
