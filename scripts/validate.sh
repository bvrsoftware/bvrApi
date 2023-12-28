#!/bin/bash
echo Running validate build ..........
sleep 60s
if lsof -Pi :80 -sTCP:LISTEN -t >/dev/null ; then
    echo "Validation succeed!"
else
    echo "Validation failed!"
    exit 1
fi
echo Validaion done!