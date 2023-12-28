#!/bin/bash
echo Running pre build ..........
Build_Dir=$(date +%Y%m%d);
Build_Path=/data/deploy/web/$Build_Dir;
Temp_Build=/data/deploy/web/bvr-web.tar.gz;
echo Current Build directory $Build_Dir
if [ -d "$Build_Path" ];
then
  echo Build directory already exists $Build_Path;
else
  echo Build directory creating $Build_Path;
  mkdir $Build_Path
fi
echo "Build directory identified"
mv $Temp_Build $Build_Path
cd $Build_Path
ls
tar -xf bvr-web.tar.gz
ls


