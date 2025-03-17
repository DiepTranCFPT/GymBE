#!/bin/bash

USERNAME=nhattruong075
IMAGE_NAME=webgymsystem
TAG=latest

docker build -t $USERNAME/$IMAGE_NAME:$TAG .

docker login

docker push $USERNAME/$IMAGE_NAME:$TAG

echo "Deploy thành công lên Docker Hub: $USERNAME/$IMAGE_NAME:$TAG"