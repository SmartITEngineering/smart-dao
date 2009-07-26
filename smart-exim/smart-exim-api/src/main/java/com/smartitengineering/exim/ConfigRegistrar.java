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
package com.smartitengineering.exim;

import com.smartitengineering.util.simple.IOFactory;
import com.smartitengineering.util.simple.reflection.ClassInstanceVisitorImpl;
import com.smartitengineering.util.simple.reflection.ClassScanner;
import com.smartitengineering.util.simple.reflection.Config;
import com.smartitengineering.util.simple.reflection.VisitCallback;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * A central registrar for configuration scanners. It will register scanners
 * according their priority first and then in lexical order by class name. And
 * it will also iterate in the same order.
 * @author imyousuf
 * @since 0.4
 */
public final class ConfigRegistrar {

    private static final Set<ConfigScannerRegistryItem<ClassConfigScanner>> CLASS_CONFIG_SCANNER =
        Collections.synchronizedSortedSet(
        new TreeSet<ConfigScannerRegistryItem<ClassConfigScanner>>());
    private static final Set<ConfigScannerRegistryItem<PackageConfigScanner>> PACKAGE_CONFIG_SCANNER =
        Collections.synchronizedSortedSet(
        new TreeSet<ConfigScannerRegistryItem<PackageConfigScanner>>());

    static Set<ConfigScannerRegistryItem<ClassConfigScanner>> getClassConfigScannerRegistryItems() {
        return CLASS_CONFIG_SCANNER;
    }

    static Set<ConfigScannerRegistryItem<PackageConfigScanner>> getPackageConfigScannerRegistryItems() {
        return PACKAGE_CONFIG_SCANNER;
    }

    /**
     * The following static block loads the default config scanner impl classes,
     * so that if they have any static initializers then they can invoked
     */
    static {
        String packageName =
            ConfigRegistrar.class.getPackage().getName().concat(".impl");
        ClassScanner scanner = IOFactory.getDefaultClassScanner();
        scanner.scan(new String[]{packageName},
            new ClassInstanceVisitorImpl(IOFactory.getClassNameForVisitor(
            ClassConfigScanner.class), new VisitCallback<Config>() {

            public void handle(Config config) {
                try {
                    IOFactory.getClassFromVisitorName(config.getClassName());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }));
        scanner.scan(new String[]{packageName},
            new ClassInstanceVisitorImpl(IOFactory.getClassNameForVisitor(
            PackageConfigScanner.class), new VisitCallback<Config>() {

            public void handle(Config config) {
                try {
                    IOFactory.getClassFromVisitorName(config.getClassName());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }));
    }

    private ConfigRegistrar() {
        throw new AssertionError();
    }

    /**
     * Register the class scanner with its priority
     * @param classScannerClass Class scanner
     * @param priority Priority of the scanner
     * @throws IllegalArgumentException If class scanner is null
     */
    public static void registerClassScanner(
        final Class<? extends ClassConfigScanner> classScannerClass,
        int priority) {
        CLASS_CONFIG_SCANNER.add(new ConfigScannerRegistryItem<ClassConfigScanner>(
            classScannerClass, priority));
    }

    /**
     * Register the package scanner with its priority
     * @param packageScannerClass Package scanner
     * @param priority Priority of the scanner
     * @throws IllegalArgumentException If class scanner is null
     */
    public static void registerPackageScanner(
        final Class<? extends PackageConfigScanner> packageScannerClass,
        int priority) {
        PACKAGE_CONFIG_SCANNER.add(new ConfigScannerRegistryItem<PackageConfigScanner>(
            packageScannerClass, priority));
    }

    /**
     * Scans (if required) and return the configuration for the resource class
     * @param resourceClass Resource class
     * @return config for the resource class, null if not config found for the
     *          class.
     */
    public static EximResourceConfig getConfigForClass(final Class resourceClass) {
        for (ConfigScannerRegistryItem<ClassConfigScanner> scanner :
            CLASS_CONFIG_SCANNER) {
            EximResourceConfig config = scanner.getInstance().
                getResourceConfigForClass(resourceClass);
            if (config != null) {
                return config;
            }
        }
        return null;
    }

    /**
     * Simply scan a package to gather all its configurations. This would be
     * helpful if an only if the scanner caches the config result.
     * @param resourcePackage Package to scan for resources
     */
    public static void scanPackage(final Package resourcePackage) {
        for (ConfigScannerRegistryItem<PackageConfigScanner> scanner :
            PACKAGE_CONFIG_SCANNER) {
            scanner.getInstance().scanPackageForResourceConfigs(resourcePackage);
        }
    }

    /**
     * Return all configurations from a package and in process if requried also
     * initiate a scan process
     * @param resourcePackage Package to scan
     * @return All configurations from the resource package
     */
    public static Collection<EximResourceConfig> getConfigForPackage(
        final Package resourcePackage) {
        Set<EximResourceConfig> configs = new HashSet<EximResourceConfig>();
        for (ConfigScannerRegistryItem<PackageConfigScanner> scanner :
            PACKAGE_CONFIG_SCANNER) {
            PackageConfigScanner configScanner = scanner.getInstance();
            configScanner.scanPackageForResourceConfigs(resourcePackage);
            Collection<Class> configClasses = configScanner.
                getConfiguredResourceClasses();
            for (Class configClass : configClasses) {
                if (resourcePackage.equals(configClass.getPackage())) {
                    configs.add(configScanner.getConfigurations().get(
                        configClass));
                }
            }
        }
        return configs;
    }

    private static class ConfigScannerRegistryItem<T extends ConfigScanner>
        implements Comparable<ConfigScannerRegistryItem<T>> {

        private Class<? extends T> configClass;
        private int priority;

        public ConfigScannerRegistryItem(Class<? extends T> configClass,
                                         int priority) {
            if (configClass == null) {
                throw new IllegalArgumentException();
            }
            this.configClass = configClass;
            this.priority = priority;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ConfigScannerRegistryItem<T> other =
                (ConfigScannerRegistryItem<T>) obj;
            if (this.configClass != other.configClass &&
                (this.configClass == null ||
                !this.configClass.equals(other.configClass))) {
                return false;
            }
            if (this.priority != other.priority) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash =
                71 * hash +
                (this.configClass != null ? this.configClass.hashCode() : 0);
            hash = 71 * hash + this.priority;
            return hash;
        }

        public int compareTo(ConfigScannerRegistryItem<T> obj) {
            if (obj == null) {
                return 1;
            }
            Integer myPriority = Integer.valueOf(priority);
            Integer otherPriority = Integer.valueOf(obj.priority);
            int myComp = myPriority.compareTo(otherPriority);

            if (myComp == 0) {
                int classComp = configClass.getName().compareTo(obj.configClass.
                    getName());
                return classComp;
            }
            else {
                return myComp;
            }
        }

        /**
         * Returns the instance of the scanner.
         * @return If static getInstance() method is implemented then it will
         *          invoke that first else will try to use no-args constructor.
         *          Null if both fails.
         */
        public T getInstance() {
            T instance = null;
            try {
                Method getInstanceMethod = configClass.getMethod("getInstance",
                    new Class[0]);
                if (getInstanceMethod != null && getInstanceMethod.isSynthetic()) {
                    instance = (T) getInstanceMethod.invoke(null, new Object[0]);
                }
            }
            catch(NoSuchMethodException exception) {
            }
            catch(InvocationTargetException exception) {
            }
            catch(IllegalAccessException exception) {
            }
            catch(IllegalArgumentException exception) {
            }
            if (instance == null) {
                try {
                    instance = configClass.newInstance();
                }
                catch (InstantiationException ex) {
                }
                catch (IllegalAccessException ex) {
                }
                catch (RuntimeException ex) {
                }
            }
            return instance;
        }
    }
}
