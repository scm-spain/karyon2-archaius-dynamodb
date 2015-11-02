#!/usr/bin/env bash
echo "populate config"
aws_server=
#comment next line to connect to amazon
aws_server="--endpoint-url http://localhost:8666"

echo "config"

aws dynamodb delete-table --table-name archaiusProperties $aws_server

aws dynamodb create-table --table-name archaiusProperties --attribute-definitions AttributeName=key,AttributeType=S --key-schema AttributeName=key,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 $aws_server

aws dynamodb put-item --table-name archaiusProperties --item '{"key":{"S":"amazing.property"}, "value":{"S":"amazing_value"}}' $aws_server
aws dynamodb put-item --table-name archaiusProperties --item '{"key":{"S":"property.to.override"}, "value":{"S":"value_overrided"}}' $aws_server
