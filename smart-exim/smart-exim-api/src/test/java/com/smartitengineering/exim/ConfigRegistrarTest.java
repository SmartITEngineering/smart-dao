package com.smartitengineering.exim;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ConfigRegistrarTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConfigRegistrarTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ConfigRegistrarTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( ConfigRegistrar.getClassConfigScannerRegistryItems().size() == 1 );
        assertTrue( ConfigRegistrar.getPackageConfigScannerRegistryItems().size() == 1 );
    }
}
