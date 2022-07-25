#!/bin/bash

echo '==> Remove old containers and clean old data'
docker rm ibm-mq esb-db -f

echo '==> Start docker container: ibm-mq and esb-db'
cd ./setup || exit
docker-compose up -d

sleep 10s

echo '==> Disable authentication for IBM MQ...'
id=$(docker ps -aqf "name=ibm-mq")

docker cp ./ibm-mq/disable-auth.sh "$id":/opt/mqm/bin/disable-auth.sh
docker exec -it ibm-mq /bin/bash -c '
cd /opt/mqm/bin/ || exit
chmod +x disable-auth.sh
./disable-auth.sh
rm disable-auth.sh
exit'

cd ../
echo '==> Setup environments done!'