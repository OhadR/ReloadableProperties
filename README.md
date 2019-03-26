# ReloadableProperties

This project is based on https://github.com/jamesmorgan/ReloadablePropertiesAnnotation.

## How to run

mvn clean tomcat7:run -Dohadr.project.port=8093


## Debug within Eclipse

See how in this README: https://gitlab.com/OhadR/activemq-spring-sandbox#debug-within-eclipse

Use the browser to make a GET call: http://localhost:8093/reloadable-properties/properties. see the result value. Then change the value in the properties file, and make this call again. See that the value has changed - reloaded without restart.