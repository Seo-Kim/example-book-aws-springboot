#!/bin/bash

REPOSITORY=~/app/step2
PROJECT_NAME=example-book-aws-springboot.jar

cd $REPOSITORY/

echo '>> Find JAR'
#JAR_NAME=$( ls -t ./zip/*.jar | head -n 1 ) ## return PATH not NAME
JAR_NAME=$( ls -tr ./zip/ | grep .jar | tail -n 1 )

echo '>> Copy to Root :: '"$JAR_NAME"' > ./'"$PROJECT_NAME"
cp ./zip/$JAR_NAME ./$PROJECT_NAME
#chmod +x ./$PROJECT_NAME

echo $'\n>> Find PID on Running'
CURRENT_PID=$( pgrep -f ${PROJECT_NAME} )
if [ -z "$CURRENT_PID" ]; then
  echo '>> Nothing found PID to Stop'
else
  echo '>> Stop PID :: '"$CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 10
fi

echo $'\n>> Start Application'
mkdir -p ./log/
mv ./nohup.out ./log/nohup-$( date +%Y%m%d%H%M%S ).out
#nohup $( cat ~/app/java_path )/java -jar $REPOSITORY/$PROJECT_NAME 2>&1 &
nohup $( cat ~/app/java_path )/java -jar $REPOSITORY/$PROJECT_NAME > $REPOSITORY/nohup.out 2>&1 &