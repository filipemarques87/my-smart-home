package io.mysmarthome;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppProperties {

//    @Value("${appRoot}")
//    private String appRoot;

    @Value("${dataFolder}")
    private String dataFolder;

    @Value("${configFile}")
    private String configFile;

    @Value("${devicesFile}")
    private String devicesFile;

    @Value("${firebaseFcmFile}")
    private String firebaseFcmFile;

    @Value("${platformsFolder}")
    private String platformsFolder;

    @Value("${dbHost}")
    private String dbHost;

    @Value("${dbJdbcDriver}")
    private String dbJdbcDriver;

    @Value("${dbUserName}")
    private String dbUserName;

    @Value("${dbPassword}")
    private String dbPassword;
}


    // -DdeviceFile=/home/filipe/my-smart-home/config/devices.yaml -DconfigurationFile=/home/filipe/my-smart-home/config/configuration.properties -DdbHost=jdbc:h2:file:/home/filipe/my-smart-home/data/database -Dusername=sa -Dpassword= -DlogLevel=INFO -DlogPath=/home/filipe/my-smart-home/logs -DfirebaseConfigFile=/home/filipe/my-smart-home/config/smarthome-938ce-firebase-adminsdk-lwxdi-7302b50627.json -DplatformsFolder=/home/filipe/my-smart-home/platforms