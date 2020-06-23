#!/bin/bash

set -e
EXIT_STATUS=0

start=`date +%s`

./gradlew clean test --console=plain || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

echo "Starting services"

docker run -d -p 8500:8500 consul

echo "Waiting 5 seconds for docker to start"

sleep 5

./gradlew run -parallel --console=plain & PID1=$!

echo "Waiting 5 seconds for microservices to start"

sleep 5

./gradlew :acceptancetest:test --rerun-tasks --console=plain || EXIT_STATUS=$?

killall -9 java

docker rm $(docker stop $(docker ps -a -q --filter ancestor=consul --format="{{.ID}}"))

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

end=`date +%s`
runtime=$((end-start))
echo "execution took $runtime seconds"

exit $EXIT_STATUS
