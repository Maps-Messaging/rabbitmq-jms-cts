package com.rabbitmq.integration.jmscts;

import java.io.File;

import org.exolab.jmscts.test.ComplianceTestSuite;
import org.junit.Before;
import org.junit.Test;

/**
 * Runs the jmscts test suite as a single (integration) test (using maven/failsafe).
 */
public class MainComplianceIT {

    static {
        if (System.getProperty("basedir")==null) {
            System.setProperty("basedir",".");
        }
        System.setProperty("jmscts.home", System.getProperty("basedir"));
    }

    @Before
    public void setup(){
        if (System.getProperty("rabbit.jms.terminationTimeout") == null) {
            System.setProperty("rabbit.jms.terminationTimeout", "1000");
        }
    }

    @Test
    public void testSelector() throws Exception {
        runTests(System.getProperty("basedir"), "target/jmscts-selector-report", "config/selector-filter.xml");
    }

    @Test
    public void testBrowse() throws Exception {
        runTests(System.getProperty("basedir"), "target/jmscts-browse-report", "config/browse-filter.xml");
    }

    @Test
    public void testTopics() throws Exception {
        runTests(System.getProperty("basedir"), "target/jmscts-topic-report", "config/topic-filter.xml");
    }

    @Test
    public void testQueues() throws Exception {
        runTests(System.getProperty("basedir"), "target/jmscts-queue-report", "config/queue-filter.xml");
    }

    private void runTests(String basedir, String target, String config){
        ComplianceTestSuite.main(new String[] { "-output",
            new File(basedir, target).getAbsolutePath(),
            "-filter",
            new File(basedir, config).getAbsolutePath() });
    }
}
