#!/bin/bash
set -e
EXIT_STATUS=0

start=`date +%s`
./gradlew --console=plain clean
./gradlew --console=plain acceptancetest:consulstatus || EXIT_STATUS=$?

if [[ $EXIT_STATUS -eq 0 ]]; then
  echo "Starting microservices"
  ./gradlew run -parallel > /dev/null 2>&1 &
  PID1=$!
  echo "Waiting for microservices to start"
  sleep 2
  echo "Executing tests"
  ./gradlew --console=plain test || EXIT_STATUS=$?
  kill -9 $PID1 > /dev/null 2>&1
  ./gradlew --console=plain -stop
fi
end=`date +%s`
runtime=$((end-start))
echo "execution took $runtime seconds"

exit $EXIT_STATUS