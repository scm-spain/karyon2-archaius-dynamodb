#!/usr/bin/env bash
cd config
./populate_config.sh
cd ..
aws dynamodb list-tables --endpoint-url http://192.168.59.103:8666
