#!/bin/bash
set -eu
  ## -e: -o errexit 오류 발생 시 종료
  ## -u: -o unused 정의되지 않은 변수 사용 제한
  ## -x: 디버깅 (명령 실행 전 "+[명령문]" 출력)

REPOSITORY=~/app/step2
PROJECT_NAME=example-book-aws-springboot.jar

#cd $REPOSITORY/ || exit
  ## 이동 실패 시 종료 > set -e 설정으로 불필요
cd $REPOSITORY/

echo '>> Find JAR'
#JAR_NAME=$( ls -t ./zip/*.jar | head -n 1 ) ## return PATH not NAME
#JAR_NAME=$( ls -tr ./zip/ | grep .jar | tail -n 1 )
#JAR_NAME=$( basename "$( ls -tr ./zip/*.jar | tail -n 1 )" )
JAR_NAME=$( basename "$( find ./zip/ -maxdepth 1 -type f -name '*.jar' | sort -n | tail -n 1 )" )
  ## ShellCheck 권장 사항을 지켜보려고 하니 참 복잡하다 (결국 영문자, 숫자가 아닌 이름의 처리 때문에 변경을 권장하는 것)
  ## ./zip부터 깊이 1 이하에서 .jar 파일을 찾는다
  ## 파일 수정?생성? 시간을 기준으로 정렬하고 (sort) 최하위를 선택 (tail)
  ## 경로에서 파일 이름만 (정확히는 최종 경로를) 추출 (basename)

echo '>> Copy to Root :: '"$JAR_NAME"' > ./'$PROJECT_NAME
cp ./zip/"$JAR_NAME" ./$PROJECT_NAME
#chmod +x ./$PROJECT_NAME

set +e
  ## +e: -e 옵션 제거
echo $'\n>> Find PID on Running'
CURRENT_PID=$( pgrep -f ${PROJECT_NAME} )
  ## PID 못 찾은 경우 오류가 발생하여 아무 값도 반환되지 않는 것
  ## 오류 발생 없이 PID 있는지 찾거나 기본 값 설정할 수 있는 손쉬운 방법을 못 찾아서 errexit 옵션 제거 후 다시 추가
set -e
if [ -z "$CURRENT_PID" ]; then
  ## -z: zero length
  echo 'Nothing found PID to Stop'
else
  echo 'Stop PID :: '"$CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 10
fi

echo $'\n>> backup log'
mkdir -p ./log/
if [ -z "$( find ./ -maxdepth 1 -type f -name 'nohup.out' | head -n 1 )" ]; then
  echo 'Nothing found file to backup'
else
  mv ./nohup.out ./log/nohup-"$( date +%Y%m%d%H%M%S )".out
fi

echo '>> Start Application'
#nohup $( cat ~/app/java_path )/java -jar $REPOSITORY/$PROJECT_NAME 2>&1 &
#nohup $( cat ~/app/java_path )/java -jar $REPOSITORY/$PROJECT_NAME > $REPOSITORY/nohup.out 2>&1 &
  ## nohup 출력이 CI 서비스가 아니라 서버에 되도록 함으로 프로그램 실행 후 CI 서비스는 정상 종료되게 함 (무한 대기 방지)
nohup "$( cat ~/app/java_path )"/java -jar \
  -Dspring.config.location=\
classpath:application.properties,\
./props/ \
  -Dspring.profiles.active=real \
  $REPOSITORY/$PROJECT_NAME > $REPOSITORY/nohup.out 2>&1 &
  ## 실행 옵션 추가 (외부 설정 파일 연결, 실행 환경 지정)
  ## 줄바꿈과 공백은 별개 문자 (줄바꿈 앞 또는 뒤에 공백 배치 여부 매우 중요)
