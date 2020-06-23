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

if [[ -z "${TRAVIS}" ]]; then
  echo "Waiting 5 seconds for consul to start"
  sleep 5
else
  echo "Waiting 15 seconds for consul to start"
  sleep 15
fi

./gradlew run -parallel --console=plain & PID1=$!

if [[ -z "${TRAVIS}" ]]; then
  echo "Waiting 5 seconds for microservices to start"
  sleep 5
else
  echo "Waiting 15 seconds for microservices to start"
  sleep 15
fi


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
