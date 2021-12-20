# Remove previous images
docker rmi $(docker images | grep 'dsm-project') -f

# Build images
sbt "docker; project sign-service; docker"

docker images | grep 'dsm-project'
