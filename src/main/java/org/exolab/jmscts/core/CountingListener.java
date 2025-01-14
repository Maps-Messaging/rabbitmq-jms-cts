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
 * $Id: CountingListener.java,v 1.3 2004/01/31 13:44:24 tanderson Exp $
 */
package org.exolab.jmscts.core;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * Helper class for counting the number of messages received.
 * Clients waiting on this will be notified when the expected number of
 * messages are received.
 *
 * @version     $Revision: 1.3 $ $Date: 2004/01/31 13:44:24 $
 * @author      <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 */
public class CountingListener implements MessageListener {

    /**
     * The number of expected messages
     */
    private final int _expected;

    /**
     * The actual number of messages received
     */
    private volatile int _count = 0;

    /**
     * Semaphore used to synchronize process completion
     */
    private Semaphore _completedLock = new Semaphore(0);


    /**
     * Construct an instance of the listener, with the number of messages
     * expected to be received. When this count is reached, the listener
     * notifies the waiting client (if any)
     *
     * @param expected the number of messages expected
     */
    public CountingListener(int expected) {
        _expected = expected;
    }

    /**
     * This method is asynchronously invoked by the message consumer when
     * a message becomes available.
     * If the number of expected messages are received, it notifies
     * the waiting client (if any)
     *
     * @param message the received message
     */
    @Override
    public void onMessage(Message message) {
        if (++_count >= _expected) {
            _completedLock.release();
        }
    }

    /**
     * Returns the no. of messages expected
     *
     * @return the no. of messages expected
     */
    public int getExpected() {
        return _expected;
    }

    /**
     * Returns the count of received messages
     *
     * @return the number of messages received
     */
    public int getReceived() {
        return _count;
    }

    /**
     * Wait for the listener to complete processing
     *
     * @param timeout the number of milleseconds to wait. An argument less
     * than or equal to zero means not to wait at all
     * @throws InterruptedException if interrupted while waiting
     * @return <code>true</code> if the listener completed in the given time
     * frame
     */
    public boolean waitForCompletion(long timeout)
        throws InterruptedException {
        boolean completed = _completedLock.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        if (completed) {
            _completedLock.release();
        }
        return completed;
    }

    /**
     * Wait indefinitely for the listener to complete processing
     *
     * @throws InterruptedException if interrupted while waiting
     */
    public void waitForCompletion() throws InterruptedException {
        _completedLock.acquire();
        _completedLock.release();
    }

}
