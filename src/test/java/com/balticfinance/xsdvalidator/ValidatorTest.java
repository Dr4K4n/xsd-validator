package com.balticfinance.xsdvalidator;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {

    @Test
    public void testMain() {
        String[] args = { "src/test/resources/test.xsd", "src/test/resources/test.xml" };
        Validator.main(args);
        Assert.assertTrue(true);
    }

}
