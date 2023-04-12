#!/bin/sh

APP_NAME=kun-blog

JAR_PATH='/usr/local/src/project/kun-cloud'

JAR_NAME=kun-blog.jar

JAR_SIMPLE_NAME=kun-blog

LOG_PATH='/usr/local/src/project/kun-cloud/logs'

usage() {
    echo "Usage: sh kun-blog.sh [start|stop|restart|status]"
    exit 1
}


is_exist(){
  pid=`ps -ef|grep $JAR_NAME | grep -v grep|awk '{print $2}' `

  if [ -z "${pid}" ];
  then
   return 1
  else
    return 0
  fi
}


start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> $APP_NAME is already running PID=${pid} <<<"
  else
  	if [ ! -d $LOG_PATH ];then
  		mkdir -p $LOG_PATH
  	fi
nohup java -Xms256m -Xmx512m -jar $JAR_PATH/$JAR_NAME --spring.profiles.active=prod  -Djava.awt.headless=false -DKUN_BLOG_SERVER_IP=112.74.169.107 -DKUN_BLOG_REDIS_PASSWORD=admin123 -DKUN_BLOG_DATASOURCE_USERNAME=root -DKUN_BLOG_DATASOURCE_PASSWORD=qazWSX123 -DKUN_BLOG_FASTDFS_GROUP=group1 -DKUN_BLOG_FASTDFS_PORT=22122 -Xms128m -Xmx128m -Xss256k -XX:ParallelGCThreads=2> $LOG_PATH/$APP_NAME.log 2>&1 &
    echo ">>> start $APP_NAME successed PID=$! <<<"
	printLog
   fi
  }


stop(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> $APP_NAME process stopped <<<"
  else
    echo ">>> $APP_NAME is not running <<<"
  fi
}


status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> $APP_NAME is running PID is ${pid} <<<"
  else
    echo ">>> $APP_NAME is not running <<<"
  fi
}


restart(){
  stop
  start
}

printLog()
{
tail -f  /usr/local/src/project/kun-cloud/logs/kun-blog.log
}

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0
