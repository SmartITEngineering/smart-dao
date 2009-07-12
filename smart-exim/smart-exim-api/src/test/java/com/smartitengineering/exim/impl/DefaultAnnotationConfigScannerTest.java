/*
 * This is a common dao with basic CRUD operations and is not limited to any
 * persistent layer implementation
 *
 * Copyright (C) 2008  Imran M Yousuf (imyousuf@smartitengineering.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.exim.impl;

import com.smartitengineering.exim.AssociationConfig;
import com.smartitengineering.exim.EximResourceConfig;
import com.smartitengineering.exim.impl.data.IResource;
import com.smartitengineering.exim.impl.data.IResourceImpl;
import com.smartitengineering.exim.impl.data.InheritedResource;
import com.smartitengineering.exim.impl.data.MultiResourceImpl;
import com.smartitengineering.exim.impl.data.SomeResource;
import com.smartitengineering.exim.impl.data.TestClass;
import com.smartitengineering.exim.impl.data.TestResourceDomain;
import com.smartitengineering.exim.impl.data.TestResourceDomainField;
import java.util.Collection;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author imyousuf
 */
public class DefaultAnnotationConfigScannerTest
    extends TestCase {

    public DefaultAnnotationConfigScannerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
    }

    /**
     * Test of getInstance method, of class DefaultAnnotationConfigScanner.
     */
    public void testGetInstance() {
        System.out.println("getInstance");
        DefaultAnnotationConfigScanner expResult =
                                       DefaultAnnotationConfigScanner.
            getInstance();
        DefaultAnnotationConfigScanner result =
                                       DefaultAnnotationConfigScanner.
            getInstance();
        assertEquals(expResult,
                     result);
    }

    /**
     * Test of getConfigurations method, of class DefaultAnnotationConfigScanner.
     */
    public void testGetConfigurations() {
        System.out.println("getConfigurations");
        DefaultAnnotationConfigScanner instance =
                                       new DefaultAnnotationConfigScanner();
        Map<Class, EximResourceConfig> result = instance.getConfigurations();
        assertEquals(0,
                     result.size());
        instance.getResourceConfigForClass(TestClass.class);
        assertEquals(4,
                     result.size());
        assertTrue(result.keySet().contains(TestResourceDomain.class));
        assertTrue(result.keySet().contains(TestResourceDomainField.class));
        assertTrue(result.keySet().contains(IResource.class));
        assertFalse(result.keySet().contains(TestClass.class));
        assertFalse(result.keySet().contains(SomeResource.class));
        instance.getResourceConfigForClass(TestResourceDomain.class);
        assertEquals(4,
                     result.size());
        assertTrue(result.keySet().contains(TestResourceDomain.class));
        assertTrue(result.keySet().contains(IResource.class));
        assertTrue(result.keySet().contains(TestResourceDomainField.class));
        assertFalse(result.keySet().contains(TestClass.class));
        assertFalse(result.keySet().contains(SomeResource.class));
    }

    /**
     * Test of getConfiguredResourceClasses method, of class DefaultAnnotationConfigScanner.
     */
    public void testGetConfiguredResourceClasses() {
        System.out.println("getConfiguredResourceClasses");
        DefaultAnnotationConfigScanner instance =
                                       new DefaultAnnotationConfigScanner();
        Collection<Class> result = instance.getConfiguredResourceClasses();
        assertTrue(result.isEmpty());
        Package resourcePackage = Package.getPackage(getClass().getPackage().
            getName().concat(".data"));
        instance.scanPackageForResourceConfigs(resourcePackage);
        result = instance.getConfiguredResourceClasses();
        assertEquals(4,
                     result.size());
        assertTrue(result.contains(TestResourceDomain.class));
        assertTrue(result.contains(TestResourceDomainField.class));
        assertTrue(result.contains(IResource.class));
        assertFalse(result.contains(TestClass.class));
        assertFalse(result.contains(SomeResource.class));
    }

    /**
     * Test of getResourceConfigForClass method, of class DefaultAnnotationConfigScanner.
     */
    public void testGetResourceConfigForClass() {
        System.out.println("getResourceConfigForClass");
        DefaultAnnotationConfigScanner instance =
                                       new DefaultAnnotationConfigScanner();
        Class resourceClass;
        EximResourceConfig result;
        Map<String, AssociationConfig> asssocConfigs;
        AssociationConfig associationConfig;

        /**
         * Test TestResourceDomain's config
         */
        resourceClass = TestResourceDomain.class;
        result = instance.getResourceConfigForClass(resourceClass);
        assertNotNull(result);
        assertTrue(result.isAccessByPropertyEnabled());
        assertFalse(result.isAssociateExportPolicyAsUri());
        assertFalse(result.isIdentityCustomizerImplemented());
        assertFalse(result.isImporterImplemented());
        assertTrue(result.isExporterImplemented());
        assertEquals(TestResourceDomain.class, result.getDomainClass());
        assertEquals(TestResourceDomain.ID_PREFIX, result.getIdPrefix());
        assertEquals(TestResourceDomain.ID, result.getIdPropertyName());
        assertEquals(result.getDomainClass().getName(), result.getName());
        assertEquals(TestResourceDomain.PATH, result.getPathToResource());
        assertEquals(0, result.getPriority());
        asssocConfigs = result.getAssociationConfigs();
        assertNotNull(asssocConfigs);
        assertFalse(asssocConfigs.isEmpty());
        assertEquals(4,
                     asssocConfigs.size());
        //ID
        assertTrue(asssocConfigs.containsKey(TestResourceDomain.ID));
        associationConfig = asssocConfigs.get(TestResourceDomain.ID);
        assertEquals("getId", associationConfig.getAccessorName());
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        assertEquals(TestResourceDomain.ID, associationConfig.getName());
        assertFalse(associationConfig.isEagerSet());
        assertFalse(associationConfig.isItToBeExportedAsUri());
        assertFalse(associationConfig.isStringProviderImplemented());
        assertFalse(associationConfig.isTransient());
        //Some prop
        assertTrue(asssocConfigs.containsKey(TestResourceDomain.SOME_PROP));
        associationConfig = asssocConfigs.get(TestResourceDomain.SOME_PROP);
        assertEquals("getSomeProp", associationConfig.getAccessorName());
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        assertEquals(TestResourceDomain.SOME_PROP_NAME, associationConfig.
            getName());
        assertFalse(associationConfig.isEagerSet());
        assertTrue(associationConfig.isItToBeExportedAsUri());
        assertFalse(associationConfig.isStringProviderImplemented());
        assertFalse(associationConfig.isTransient());
        //Some bool prop
        assertTrue(asssocConfigs.containsKey(TestResourceDomain.SOME_BOOL_PROP));
        associationConfig = asssocConfigs.get(TestResourceDomain.SOME_BOOL_PROP);
        assertEquals("hasSomeBoolProp", associationConfig.getAccessorName());
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        assertEquals(TestResourceDomain.SOME_BOOL_PROP, associationConfig.
            getName());
        assertTrue(associationConfig.isEagerSet());
        assertFalse(associationConfig.isItToBeExportedAsUri());
        assertFalse(associationConfig.isStringProviderImplemented());
        assertFalse(associationConfig.isTransient());
        //Some transient prop
        assertTrue(asssocConfigs.containsKey(
            TestResourceDomain.SOME_TRANSIENT_PROP));
        associationConfig = asssocConfigs.get(
            TestResourceDomain.SOME_TRANSIENT_PROP);
        assertEquals("getSomeTransientProp", associationConfig.getAccessorName());
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        assertEquals(TestResourceDomain.SOME_TRANSIENT_PROP, associationConfig.
            getName());
        assertFalse(associationConfig.isEagerSet());
        assertFalse(associationConfig.isItToBeExportedAsUri());
        assertFalse(associationConfig.isStringProviderImplemented());
        assertTrue(associationConfig.isTransient());

        /**
         * Test TestResourceDomainField's config
         */
        resourceClass = TestResourceDomainField.class;
        result = instance.getResourceConfigForClass(resourceClass);
        assertNotNull(result);
        assertFalse(result.isAccessByPropertyEnabled());
        assertTrue(result.isAssociateExportPolicyAsUri());
        assertTrue(result.isIdentityCustomizerImplemented());
        assertTrue(result.isImporterImplemented());
        assertFalse(result.isExporterImplemented());
        assertEquals(TestResourceDomainField.class, result.getDomainClass());
        assertEquals(null, result.getIdPrefix());
        assertEquals(null, result.getIdPropertyName());
        assertEquals(TestResourceDomainField.NAME, result.getName());
        assertEquals("", result.getPathToResource());
        assertEquals(0, result.getPriority());
        asssocConfigs = result.getAssociationConfigs();
        assertNotNull(asssocConfigs);
        assertFalse(asssocConfigs.isEmpty());
        assertEquals(2, asssocConfigs.size());
        //someStrProp
        assertTrue(asssocConfigs.containsKey(
            TestResourceDomainField.SOME_STR_PROP));
        associationConfig = asssocConfigs.get(
            TestResourceDomainField.SOME_STR_PROP);
        assertEquals(TestResourceDomainField.SOME_STR_PROP,
                     associationConfig.getAccessorName());
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        assertEquals(TestResourceDomainField.SOME_STR_PROP, associationConfig.
            getName());
        assertFalse(associationConfig.isEagerSet());
        assertFalse(associationConfig.isItToBeExportedAsUri());
        assertFalse(associationConfig.isStringProviderImplemented());
        assertFalse(associationConfig.isTransient());
        //someTransientStrProp
        assertTrue(asssocConfigs.containsKey(
            TestResourceDomainField.SOME_TRANSIENT_STR_PROP));
        associationConfig = asssocConfigs.get(
            TestResourceDomainField.SOME_TRANSIENT_STR_PROP);
        assertEquals(TestResourceDomainField.SOME_TRANSIENT_STR_PROP,
                     associationConfig.getAccessorName());
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        assertEquals(TestResourceDomainField.SOME_TRANSIENT_STR_PROP,
                     associationConfig.getName());
        assertFalse(associationConfig.isEagerSet());
        assertFalse(associationConfig.isItToBeExportedAsUri());
        assertFalse(associationConfig.isStringProviderImplemented());
        assertTrue(associationConfig.isTransient());

        /**
         * Test IResource's config
         */
        resourceClass = IResource.class;
        result = instance.getResourceConfigForClass(resourceClass);
        assertNotNull(result);
        assertTrue(result.isAccessByPropertyEnabled());
        assertTrue(result.isAssociateExportPolicyAsUri());
        assertFalse(result.isIdentityCustomizerImplemented());
        assertFalse(result.isImporterImplemented());
        assertFalse(result.isExporterImplemented());
        assertEquals(IResource.class, result.getDomainClass());
        assertEquals("", result.getIdPrefix());
        assertEquals(IResource.RESOURCE_ID, result.getIdPropertyName());
        assertEquals(result.getDomainClass().getName(), result.getName());
        assertEquals("", result.getPathToResource());
        assertEquals(IResource.PRIORITY, result.getPriority());
        asssocConfigs = result.getAssociationConfigs();
        assertNotNull(asssocConfigs);
        assertFalse(asssocConfigs.isEmpty());
        assertEquals(7, asssocConfigs.size());
        //resourceId
        assertTrue(asssocConfigs.containsKey(IResource.RESOURCE_ID));
        associationConfig = asssocConfigs.get(IResource.RESOURCE_ID);
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());
        //list
        assertTrue(asssocConfigs.containsKey(IResource.LIST));
        associationConfig = asssocConfigs.get(IResource.LIST);
        assertEquals(AssociationConfig.AssociationType.TYPE_LIST,
                     associationConfig.getAssociationType());
        //set
        assertTrue(asssocConfigs.containsKey(IResource.SET));
        associationConfig = asssocConfigs.get(IResource.SET);
        assertEquals(AssociationConfig.AssociationType.TYPE_SET,
                     associationConfig.getAssociationType());
        assertEquals(IResource.SET_NAME, associationConfig.getName());
        //collection
        assertTrue(asssocConfigs.containsKey(IResource.COLLECTION));
        associationConfig = asssocConfigs.get(IResource.COLLECTION);
        assertEquals(AssociationConfig.AssociationType.TYPE_COLLECTION,
                     associationConfig.getAssociationType());
        //map
        assertTrue(asssocConfigs.containsKey(IResource.MAP));
        associationConfig = asssocConfigs.get(IResource.MAP);
        assertEquals(AssociationConfig.AssociationType.TYPE_MAP,
                     associationConfig.getAssociationType());
        assertTrue(associationConfig.isEagerSet());
        //array
        assertTrue(asssocConfigs.containsKey(IResource.ARRAY));
        associationConfig = asssocConfigs.get(IResource.ARRAY);
        assertEquals(AssociationConfig.AssociationType.TYPE_ARRAY,
                     associationConfig.getAssociationType());
        //bool valid
        assertTrue(asssocConfigs.containsKey(IResource.VALID));
        associationConfig = asssocConfigs.get(IResource.VALID);
        assertEquals(AssociationConfig.AssociationType.TYPE_OBJECT,
                     associationConfig.getAssociationType());

    }

    /**
     * Test of scanPackageForResourceConfigs method, of class DefaultAnnotationConfigScanner.
     */
    public void testScanPackageForResourceConfigs() {
        System.out.println("scanPackageForResourceConfigs");
        Package resourcePackage = Package.getPackage(getClass().getPackage().
            getName().concat(".data"));
        DefaultAnnotationConfigScanner instance =
                                       new DefaultAnnotationConfigScanner();
        Map<Class, EximResourceConfig> result = instance.getConfigurations();
        assertEquals(0, result.size());
        instance.scanPackageForResourceConfigs(resourcePackage);
        assertEquals(4, result.size());
        assertTrue(result.keySet().contains(TestResourceDomain.class));
        assertTrue(result.keySet().contains(TestResourceDomainField.class));
        assertTrue(result.keySet().contains(IResource.class));
        assertFalse(result.keySet().contains(TestClass.class));
        assertFalse(result.keySet().contains(SomeResource.class));
    }

    public void testGetConfigForInheritence() {
        DefaultAnnotationConfigScanner instance =
                                       new DefaultAnnotationConfigScanner();
        Package resourcePackage = Package.getPackage(getClass().getPackage().
            getName().concat(".data"));
        instance.scanPackageForResourceConfigs(resourcePackage);
        EximResourceConfig config = instance.getResourceConfigForClass(
            IResourceImpl.class);
        assertNotNull(config);
        assertEquals(IResource.class, config.getDomainClass());
        config = instance.getResourceConfigForClass(InheritedResource.class);
        assertNotNull(config);
        assertEquals(IResource.class, config.getDomainClass());
        config = instance.getResourceConfigForClass(MultiResourceImpl.class);
        assertNotNull(config);
        assertEquals(IResource.class, config.getDomainClass());
    }

    /**
     * Test of getPropertyNameFromMethodName method, of class DefaultAnnotationConfigScanner.
     */
    public void testGetPropertyNameFromMethodName() {
        System.out.println("getPropertyNameFromMethodName");
        String methodName = "getPropertyNameFromMethodName";
        DefaultAnnotationConfigScanner instance =
                                       new DefaultAnnotationConfigScanner();
        String expResult = "propertyNameFromMethodName";
        String result = instance.getPropertyNameFromMethodName(methodName);
        assertEquals(expResult, result);
        methodName = "isPropertyNameFromMethodName";
        result = instance.getPropertyNameFromMethodName(methodName);
        assertEquals(expResult, result);
        methodName = "hasPropertyNameFromMethodName";
        result = instance.getPropertyNameFromMethodName(methodName);
        assertEquals(expResult, result);
        methodName = "somePropertyNameFromMethodName";
        try {
            result = instance.getPropertyNameFromMethodName(methodName);
            fail("should not pass!");
        }
        catch (IllegalArgumentException ex) {
        }
        methodName = null;
        try {
            result = instance.getPropertyNameFromMethodName(methodName);
            fail("should not pass!");
        }
        catch (NullPointerException ex) {
        }
    }
}
