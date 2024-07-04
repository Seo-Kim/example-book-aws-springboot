#!/usr/bin/env bash
set -eu

ABSPATH=$( readlink -f "$0" )
source "$( dirname "$ABSPATH" )/profile.sh"

REPOSITORY=~/app/step3
PROJECT_NAME=example-book-aws-springboot.jar

cd $REPOSITORY/

echo '>> Find JAR'
JAR_NAME=$( basename "$( find ./zip/ -maxdepth 1 -type f -name '*.jar' | sort -n | tail -n 1 )" )

echo '>> Copy to Root :: '"${JAR_NAME}"' > ./'${PROJECT_NAME}
cp "./zip/${JAR_NAME}" ./${PROJECT_NAME}
#chmod +x ./${PROJECT_NAME}

echo '>> backup log'
mkdir -p ./log/
if [ -z "$( find ./ -maxdepth 1 -type f -name 'nohup.out' | head -n 1 )" ]; then
  echo 'Nothing found file to backup'
else
  mv './nohup.out' "./log/nohup-$( date +%Y%m%d%H%M%S ).out"
fi

IDLE_PROFILE=$( find_idle_profile )
echo '>> Start Application :: '"${IDLE_PROFILE}"
nohup "$( cat ~/app/java_path )"/java -jar \
  -Dspring.config.location=\
classpath:application.properties,\
optional:./props/ \
  -Dspring.profiles.active="${IDLE_PROFILE}" \
  $REPOSITORY/$PROJECT_NAME > "$REPOSITORY/nohup.out" 2>&1 &