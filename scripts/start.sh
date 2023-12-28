#!/bin/bash
echo Running start build ..........
echo Total Number of Argument Passed: "$#"
Build_Profile=${1?Error: Please provide profile name}
echo Active profile: $Build_Profile
pid=`ps -eaf | grep  bvr.war | grep -v "grep" | awk '{print $2}'`
CODE_DIRECTORY=`pwd`
if test -z "$pid"
then
 echo "Bvr web is not running"
else
 echo "Bvr web previous Process ID: "$pid
 kill -9 $pid
fi
nohup java -jar -Dserver.port=80 -Dspring.profiles.active=$Build_Profile $CODE_DIRECTORY/gps.war </dev/null &>/dev/null &
echo "Starting bvr web................."
pid=`ps -eaf | grep  bvr.war | grep -v "grep" | awk '{print $2}'`
echo "Gps web current Process ID: "$pid