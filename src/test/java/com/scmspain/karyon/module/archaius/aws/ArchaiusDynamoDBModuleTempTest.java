package com.scmspain.karyon.module.archaius.aws;

import com.netflix.config.DynamicPropertyFactory;
import com.scmspain.karyon.module.archaius.aws.common.AbstractControllerTest;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class ArchaiusDynamoDBModuleTempTest extends AbstractControllerTest {

    @Test
    public void shouldTakeNewValuesFromDynamoDb() throws Exception {
        DynamicPropertyFactory properties = DynamicPropertyFactory.getInstance();

        final String actual = properties.getStringProperty("amazing.property", "failing_value").get();

        Assert.assertEquals("amazing_value", actual);
    }

    @Test
    public void shouldTakeDefaultValuesFromPropertiesFile() throws Exception {
        DynamicPropertyFactory properties = DynamicPropertyFactory.getInstance();

        final String actual = properties.getStringProperty("property.only.in.file", "failing_value").get();

        Assert.assertEquals("got_it_from_file", actual);
    }

    @Test
    public void shouldOverride() throws Exception {
        DynamicPropertyFactory properties = DynamicPropertyFactory.getInstance();

        final String actual = properties.getStringProperty("property.to.override", "failing_value").get();

        Assert.assertEquals("value_overrided", actual);
    }
}
