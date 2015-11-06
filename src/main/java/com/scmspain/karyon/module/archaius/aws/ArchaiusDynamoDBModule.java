package com.scmspain.karyon.module.archaius.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.inject.AbstractModule;
import com.netflix.config.*;
import com.netflix.config.sources.DynamoDbConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ArchaiusDynamoDBModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusDynamoDBModule.class);
    private DynamicPropertyFactory properties = DynamicPropertyFactory.getInstance();

    public static final String PROPERTY_DYNAMODB_ENDPOINT = "com.netflix.config.dynamo.endpoint";
    public static final String PROPERTY_DYNAMODB_REGION = "com.netflix.config.dynamo.region";

    @Override
    protected void configure() {
        LOGGER.info("Setting up dynamodb connection");
        loadDynamoDBConfiguration();
        LOGGER.info("Finished setting up dynamodb connection");
    }

    private void loadDynamoDBConfiguration() {
        DynamicConfiguration dynamicConfig = setupConfiguration();
        installConfiguration(dynamicConfig);
    }

    private DynamicConfiguration setupConfiguration() {
        AmazonDynamoDB dbClient = buildDynamoDBClient();
        updateRegion(dbClient);
        updateEndpoint(dbClient);
        return buildDynamoDBConfiguration(dbClient);
    }

    private AmazonDynamoDB buildDynamoDBClient() {
        AWSCredentials credentials = new DefaultAWSCredentialsProviderChain().getCredentials();
        return new AmazonDynamoDBClient(credentials);
    }

    private void updateRegion(AmazonDynamoDB dbClient) {
        final String region = properties.getStringProperty(PROPERTY_DYNAMODB_REGION, "").get();
        if (!region.isEmpty()) {
            dbClient.setRegion(Region.getRegion(Regions.fromName(region)));
            LOGGER.info("Set Dynamo region:" + region);
        }
    }

    private void updateEndpoint(AmazonDynamoDB dbClient) {
        final String endpoint = properties.getStringProperty(PROPERTY_DYNAMODB_ENDPOINT, "").get();
        if (!endpoint.isEmpty()) {
            dbClient.setEndpoint(endpoint);
            LOGGER.info("Set Dynamo endpoint:" + endpoint);
        }
    }

    private DynamicConfiguration buildDynamoDBConfiguration(AmazonDynamoDB dbClient) {
        DynamoDbConfigurationSource source = new DynamoDbConfigurationSource(dbClient);
        FixedDelayPollingScheduler scheduler = new FixedDelayPollingScheduler();

        return new DynamicConfiguration(source, scheduler);
    }

    private void installConfiguration(DynamicConfiguration dynamicConfig) {
        ConcurrentCompositeConfiguration config = prepareCompositeConfiguration(dynamicConfig);
        installCompositeConfiguration(config);
    }

    private void installCompositeConfiguration(ConcurrentCompositeConfiguration config) {
        if (ConfigurationManager.isConfigurationInstalled()) {
            ConfigurationManager.loadPropertiesFromConfiguration(config);
            LOGGER.info("ConfigurationManager updated via 'loadPropertiesFromConfiguration()' method");
        } else {
            ConfigurationManager.install(config);
            LOGGER.info("ConfigurationManager updated via 'install()' method");
        }
    }

    private ConcurrentCompositeConfiguration prepareCompositeConfiguration(DynamicConfiguration dynamicConfig) {
        ConcurrentCompositeConfiguration config = new ConcurrentCompositeConfiguration();
        config.addConfiguration(dynamicConfig, "dynamodb");
        return config;
    }
}
