# event-monitoring-sample-v2
 Event Monitoring Sample reading messages from IBM MQ(Webshere MQ) using Java Spring Boot

## Setup environments in Linux
```bash
$ chmod +x setup-env.sh
$ ./setup-env.sh
```

## Build and deployment
### Window
```powershell
.\mvnw clean install -DskipTests
```
### Linux
```bash
$ ./mvnw clean install -DskipTests
```