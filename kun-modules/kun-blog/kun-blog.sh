#!/bin/sh

APP_NAME=kun-blog

JAR_PATH='/root/kun-blog'

JAR_NAME=kun-blog.jar

JAR_SIMPLE_NAME=kun-blog

LOG_PATH='/root/kun-blog/logs'

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
nohup java -Xms256m -Xmx512m -jar $JAR_PATH/$JAR_NAME --spring.profiles.active=prod > $LOG_PATH/$APP_NAME.log 2>&1 &
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
tail -f  /root/kun-blog/logs/kun-blog.log
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
