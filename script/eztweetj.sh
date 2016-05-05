#!/bin/bash

JAR_FILE_NAME=eztweetj.jar


if [ -e ${JAR_FILE_NAME} ]; then
  java -jar ${JAR_FILE_NAME} $@

else
  echo "${JAR_FILE_NAME}が見つかりません。"

fi
