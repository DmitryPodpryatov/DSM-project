
# DSM-project

Distributed Systems and Middleware: Patterns and Frameworks Project

## How to Run

Execute bash script to build images

```shell
chmod +x ./build-images.sh
./build-images.sh
```

Run in docker 

```shell
docker network create  -d bridge app_network
docker-compose up
```

To access peers, go to ports http://0.0.0.0:8091, http://0.0.0.0:8092.

To access sign service, go to http://0.0.0.0:8082. 
