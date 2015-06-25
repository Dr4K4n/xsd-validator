/*
 * XSD Validator.
 * 
 * Copyright 2013 Adrian Mouat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.balticfinance.xsdvalidator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Validator {

    private static final Config applicationConfig = new Config();

    private static final int VALIDATION_FAIL = 1;
    private static final int ERROR_READING_SCHEMA = 2;
    private static final int ERROR_READING_XML = 3;

    private static String mXSDFileName;
    private static String[] mXMLFileNames;

    private Validator() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        parseArgs(args);
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        File xsdFile = new File(mXSDFileName);
        File[] xmlFiles = new File[mXMLFileNames.length];
        for (int i = 0; i < mXMLFileNames.length; i++) {
            xmlFiles[i] = new File(mXMLFileNames[i]);
        }

        try {
            Schema schema = factory.newSchema(xsdFile);
            javax.xml.validation.Validator validator = schema.newValidator();

            for (File xmlFile : xmlFiles) {
                Source source = new StreamSource(xmlFile);

                try {
                    validator.validate(source);
                    System.out.println(xmlFile.getName() + " validates");
                } catch (SAXParseException ex) {
                    System.out.println(xmlFile.getName() + " fails to validate because: \n");
                    System.out.println(ex.getMessage());
                    System.out.println("At: " + ex.getLineNumber() + ":" + ex.getColumnNumber());
                    System.out.println();
                    System.exit(VALIDATION_FAIL);
                } catch (SAXException ex) {
                    System.out.println(xmlFile.getName() + " fails to validate because: \n");
                    System.out.println(ex.getMessage());
                    System.out.println();
                    System.exit(VALIDATION_FAIL);
                } catch (IOException io) {
                    System.err.println("Error reading XML source: " + xmlFile.getName());
                    System.err.println(io.getMessage());
                    System.exit(ERROR_READING_XML);
                }
            }
        } catch (SAXException sch) {
            System.err.println("Error reading XML Schema: " + mXSDFileName);
            System.err.println(sch.getMessage());
            System.exit(ERROR_READING_SCHEMA);
        }

    }

    /**
     * Checks and interprets the command line arguments.
     *
     * Code is based on Sun standard code for handling arguments.
     *
     * @param args
     *            An array of the command line arguments
     */
    private static void parseArgs(final String[] args) {

        int argNo = 0;
        String currentArg;
        char flag;

        while (argNo < args.length && args[argNo].startsWith("-")) {
            currentArg = args[argNo++];

            // "wordy" arguments

            if ("--version".equals(currentArg)) {
                printVersionAndExit();
            } else if ("--help".equals(currentArg)) {
                printHelpAndExit();
            } else {

                // (series of) flag arguments
                for (int charNo = 1; charNo < currentArg.length(); charNo++) {
                    flag = currentArg.charAt(charNo);
                    switch (flag) {
                    case 'V':
                        printVersionAndExit();
                        break;
                    case 'h':
                        printHelpAndExit();
                        break;

                    default:
                        System.err.println("Illegal option " + flag);
                        printUsageAndExit();
                        break;
                    }
                }
            }
        }

        if ((argNo + 2) > args.length) {
            // Not given at least 2 files on input
            printUsageAndExit();
        }

        mXSDFileName = args[argNo];
        mXMLFileNames = Arrays.copyOfRange(args, ++argNo, args.length);
    }

    /**
     * Outputs usage message to standard error.
     */
    public static void printUsageAndExit() {

        System.err.println("Usage: " + applicationConfig.getApplicationName()
                + " [OPTION]... XSDFILE XMLFILE [XMLFILE...]");
        System.exit(2); // 2 indicates incorrect usage
    }

    public static void printHelpAndExit() {

        System.out.print("\nUsage: " + applicationConfig.getApplicationName()
                + " [OPTION]... XSDFILE XMLFILE [XMLFILE...]\n\n "
                + "Validates the XML document(s) at XMLFILE against the XML Schema at" + " XSDFILE.\n\n"
                + "--version  -V  Output version number.\n" + "--help  -h  Output this help.\n");

        System.exit(0);
    }

    /**
     * Outputs the current version of diffxml to standard out.
     */
    public static void printVersionAndExit() {

        System.out
                .println(applicationConfig.getApplicationName() + " Version " + applicationConfig.getVersion() + "\n");
        System.exit(0);
    }

}
