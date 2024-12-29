# UDP Sensor Simulation (JAVA)

## Description

Project contains two Java applications (Warehouse and CentralService). 
Warehouse application monitors multiple sensors which send data using UDP protocol. 
In current example there are two sensors heat and humidity. 
Each sensor has a threshold. If threshold is exceeded, RabbitMQ message will be sent.
CentralService is subscribed to RabbitMQ and shows messages when threshold is exceeded.

## How To Run Application

### Docker 

There is a docker-compose.yml file in the project root directory. 
It contains only RabbitMQ node. Exposed ports for RabbitMQ are (16672 and 6672). 
Ports are different in order to avoid name clashes.

Run following command to start dockerized RabbitMQ:
```
docker-compose up 
```

### Warehouse

Open Warehouse project in IntelliJ and run application.

### CentralService

Open CentralService project in IntelliJ and run application.

## Sending Messages to Sensors

Install NetCat tool which is used for sending messages using UDP protocol. 

### Heat sensor

Connect to the heat sensor using following command:
```
ncat -u 127.0.0.1 3344
```

Once connected send message in following format:
```
sensor_id=t1; value=34
```

Threshold of heat sensor is 35 so if value is more than 35 RabbitMQ message will be broadcasted
and centrall service will react.


### Humidity sensor

Connect to the heat sensor using following command:
```
ncat -u 127.0.0.1 3355
```

Once connected send message in following format:
```
sensor_id=h1; value=45
```

Threshold for hummidity is 50.

