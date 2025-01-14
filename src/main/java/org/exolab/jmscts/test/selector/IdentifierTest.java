/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact tma@netspace.net.au.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (https://castor.exolab.org).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2003-2004 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id: IdentifierTest.java,v 1.6 2004/02/03 21:52:11 tanderson Exp $
 */
package org.exolab.jmscts.test.selector;

import java.util.HashMap;

import junit.framework.Test;

import org.exolab.jmscts.core.TestCreator;


/**
 * This class tests selector identifiers.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.6 $
 * @see AbstractSelectorTestCase
 * @jmscts.message TextMessage
 */
public class IdentifierTest extends AbstractSelectorTestCase {

    /**
     * Message properties
     */
    private static final HashMap<String, String> PROPERTIES = new HashMap<String, String>();


    /**
     * Create an instance of this class for a specific test case
     *
     * @param name the name of test case
     */
    public IdentifierTest(String name) {
        super(name);
    }

    /**
     * Sets up the test suite
     *
     * @return an instance of this class that may be run by
     * {@link org.exolab.jmscts.core.JMSTestRunner}
     */
    public static Test suite() {
        return TestCreator.createSendReceiveTest(IdentifierTest.class);
    }

    /**
     * Verifies that identifiers may contain '$' characters, using the selector
     * <code>$State = 'VIC'</code> and property '$State'
     * with value 'VIC'. This should select all messages.
     *
     * @jmscts.requirement selector.identifier.name
     * @jmscts.requirement selector.expression
     * @throws Exception for any error
     */
    public void testDollars() throws Exception {
        checkSelector("$State = 'VIC'", true, PROPERTIES);
    }

    /**
     * Verifies that identifiers may contain '_' characters, using the selector
     * <code>_postcode_ = '3001'</code> and property '_postcode_'
     * with value '3001'. This should select all messages.
     *
     * @jmscts.requirement selector.identifier.name
     * @jmscts.requirement selector.expression
     * @throws Exception for any error
     */
    public void testUnderscores() throws Exception {
        checkSelector("_postcode_ = '3001'", true, PROPERTIES);
    }

    /**
     * Verifies that user identifier names are case sensitive, using the
     * selector <code>country = 'Australia'</code>, and property 'Country'
     * with value 'Australia'. This shouldn't select any messages.
     *
     * @jmscts.requirement selector.identifier.case
     * @jmscts.requirement selector.expression
     * @throws Exception for any error
     */
    public void testUserIdentifierCase() throws Exception {
        checkSelector("country = 'Australia'", false, PROPERTIES);
    }

    /**
     * Verifies that the selector <code>~ABC = 'foo'</code> throws
     * InvalidSelectorException
     *
     * @jmscts.requirement selector.identifier.name
     * @jmscts.requirement selector.validation
     * @throws Exception for any error
     */
    public void testInvalid1() throws Exception {
        checkInvalidSelector("~ABC = 'foo'");
    }

    /**
     * (Extension) Verifies that identifiers may contain periods,
     * using the selector <code>Country.name = 'New Zealand'</code> and
     * property 'Country.name' with value 'New Zealand'. This should
     * select all messages.
     *
     * @jmscts.requirement selector.identifier.name
     * @jmscts.requirement selector.expression
     * @throws Exception for any error
     */
    public void testPeriods() throws Exception {
        checkSelector("Country_name = 'New Zealand'", true, PROPERTIES);
    }

    static {
        PROPERTIES.put("Country_name", "New Zealand");
        PROPERTIES.put("Country", "Australia");
        PROPERTIES.put("$State", "VIC");
        PROPERTIES.put("_postcode_", "3001");
    }

}
