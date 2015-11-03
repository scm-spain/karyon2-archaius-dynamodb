#!/usr/bin/env bash
sh fixtures/config/populate_config.sh
aws dynamodb list-tables --endpoint-url http://localhost:8666
