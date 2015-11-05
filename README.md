# Archaius Aws module for Karyon2

This module for the Netflix framework Karyon helps configuring Archaius to poll from a DynamoDB.

## Documentation

Simply add this module into Modules specification for your AppServer.

```
@KaryonBootstrap(name = "AppServer", healthcheck = AlwaysHealthyHealthCheck.class)
@Modules(include = {
    ...
    ArchaiusDynamoDBModule.class
})
public interface AppServerForTesting {
    ...
}
```

Please see AppServerForTesting for a functional example.

## Gradle

Add dependency as follows:

```
    compile 'com.scmspain.karyon:karyon2-archaius-dynamodb:0.1.2'
```

## AppServer.properties

Update your properties with dynamodb configurations:

```
com.netflix.config.dynamo.tableName=archaiusProperties
com.netflix.config.dynamo.keyAttributeName=key
com.netflix.config.dynamo.valueAttributeName=value
com.netflix.config.dynamo.region=us-east-1
com.netflix.config.dynamo.endpoint=dynamodb.us-east-1.amazonaws.com
```
