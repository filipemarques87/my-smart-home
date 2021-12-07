# my-smart-home-server
My smart home is a plugin based application to provide support for interaction between user and the devices.

## How to install
The server runs in a service. Change the application root path in `start.sh` and in `mysmarthome.service` file to your directory and then copy the `mysmarthome.service` file to `/etc/systemd/system`.

Run the following commands: `systemctl start mysmarthome` and then `systemctl start mysmarthome`

## File structure
```
--+ app-root
    +-- config
    +-- data
    +-- logs
    +-- platforms
    \-- <application.jar>
```

### Config folder
The `config` folder stores all the configuration needed to the application.

#### Configuration file
The configuration file is a property file that holds the base configurations and as well all configurations need to initialize the platforms.

#### Device file
Device file is a yaml file that holds all configuration for the devices. A device configuration consist into 3 types:
* mandatory - configurations that are required to initialize the device
* optional - configurations that are not required for the initialization
* platform specific - configurations that are dependent of the platform used

Device configuration:

| Property      | Required | Description |
| ----------- | ----------- | ---- |
| platform | yes | The platform used by the device |
| group      | yes       | Indicates the group which the device belongs |
| name   | yes      |  The device name |
| deviceId   | not        |  A unique id that identifies the device. If it is not present, the device name is used |
| type   | yes        |  The device type. This field is used for the MySmartHome app to determine which component should be shown for the device |
| schedulers   | no        |  A list of schedulers. For each scheduler, the application will create a new cron task and when the task is triggered, will send to the device the payload defined in the file. A device can have 0 or more schedulers  |
| notifications   | no        |  A list of notifications to send in case a given condition is meet. In that case, a notification will be sent to the MySmartHome app. A device can have 0 or more notifications  |
| actions   | no        |  A list of actions to perform in case a given condition is meet. In that case, the application will send data to other device according to the configuration. A device can have 0 or more actions  |


Scheduler configuration:

| Property      | Required | Description |
| ----------- | ----------- | ---- |
| trigger | yes | A cron expression to define the frequency of the scheduler |
|  payload      | yes       | The data to send to the device when the application executes the task |

Notification configuration:

| Property      | Required | Description |
| ----------- | ----------- | ---- |
| trigger | yes | A js expression that will be evaluate |
|  payload      | yes       | A js expression that represents a message to send to MySmartHome app |


Action configuration:

| Property      | Required | Description |
| ----------- | ----------- | ---- |
| trigger | yes | A js expression that will be evaluate |
| deviceId | yes | The device that will be called if the trigger returns true |
|  payload      | yes       | The data to send to the device |

For the notification and for the action objects, the trigger property is a js expression, `<valid js expression>`. However, can also be a file path, `file:/path/to/file.js`.

#### FCM Firebase file
This file is a json file that is downloaded from the firebase console. 

### Data folder
This folder stores the data for the application.

If any database configuration is given, an H2 database is created otherwise is used the database configured in `start.sh` file.

### Logs folder
This folder will store all logs from the application.

### Platforms folder
It is in this folder where all platforms plugins should be put. At start up, the application reads this folder, and it will load all platforms present in this folder. If any error occur during the initialization, the platform will be not loaded and all devices that use that platform will be not loaded either.  