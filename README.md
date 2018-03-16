# expenditure-recorder-gui
GUI for [Expenditure Recorder](https://github.com/YuKitAs/daily-info-hub) using [JavaFX 8](http://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm#JFXST784).

## Usage

0. Stop Apache Tomcat if it's running (see [command](https://github.com/YuKitAs/tech-note/blob/a02679c7ec2e2c412653da960fb2946d717457d0/service-config/run-and-test-tomcat-on-ubuntu.md))
1. Start MongoDB with `sudo service mongod start`
2. `java -jar path/to/daily-info-hub/target/daily-info-hub-1.0.0-SNAPSHOT.jar` (`mvn clean package` before when necessary)

## Screenshots

![](../master/screenshots/expenditure-recorder-gui-all.png)

**Filters:**

![](../master/screenshots/expenditure-recorder-gui-filter.png)

**Filtered by customized time range:**

![](../master/screenshots/expenditure-recorder-gui-custom-range.png)

**Filtered by keyword:**

![](../master/screenshots/expenditure-recorder-gui-keyword.png)
