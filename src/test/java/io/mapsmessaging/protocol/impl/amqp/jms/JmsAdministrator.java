/*
 *  Copyright [2020] [Matthew Buckton]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.mapsmessaging.protocol.impl.amqp.jms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.jms.JMSException;
import javax.jms.XATopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.JmsTopic;
import org.exolab.jmscts.provider.Administrator;


public class JmsAdministrator implements Administrator {

  private final DaemonManager daemonManager;
  private final NamingException ex;
  private Context context;
  private  Properties properties = new Properties();

  public JmsAdministrator(){
    System.err.println("JMS Admin starting");
    daemonManager = new DaemonManager("http://localhost:8080");
    NamingException error = null;
    Context con = null;
    try {
      Collection<String> knownProperties = getResources(Pattern.compile(".*simpleConnectionTest.props"));
      for (String config : knownProperties) {
        try (FileInputStream fis = new FileInputStream(config)) {
          properties.load(fis);
        }
      }
      con = new InitialContext(properties);
    } catch (Exception ex) {
      ex.printStackTrace();
      error = new NamingException(ex.getMessage());
    }
    context = con;
    ex = error;
  }

  /**
   * Returns the name of the QueueConnectionFactory bound in JNDI
   *
   * @return the default QueueConnectionFactory name
   */
  public String getQueueConnectionFactory() {
    return "QueueConnectionFactory";
  }

  /**
   * Returns the name of the TopicConnectionFactory bound in JNDI
   *
   * @return the default TopicConnectionFactory name
   */
  public String getTopicConnectionFactory() {
    return "TopicConnectionFactory";
  }

  public String getXAQueueConnectionFactory() {
    return "XAQueueConnectionFactory";
  }

  public String getXATopicConnectionFactory() {
    XATopicConnectionFactory factory;
    return "XAQTopicConnectionFactory";
  }

  public Object lookup(String s) throws NamingException {
    return getContext().lookup(s);
  }

  /**
   * Returns the JNDI context used to look up administered objects
   *
   * @return the JNDI context under which administered objects are bound
   * @throws NamingException if the context cannot be obtained
   */
  public Context getContext() throws NamingException {
    if(context != null){
      return context;
    }
    throw ex;
  }

  /**
   * Create an administered destination
   *
   * @param name the destination name
   * @param queue if true, create a queue, else create a topic
   * @throws JMSException if the destination cannot be created
   */
  public void createDestination(String name, boolean queue) throws JMSException {
    try {
      if(queue){
        properties.put("queue."+name, name);
      }
      else{
        properties.put("topic."+name, name);
      }
      context = new InitialContext(properties);
      try {
        daemonManager.create(name, !queue);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (NamingException e) {
      e.printStackTrace();
      JMSException jmsEx = new JMSException(e.getMessage());
      jmsEx.initCause(e);
      throw jmsEx;
    }
  }

  /**
   * Destroy an administered destination
   *
   * @param name the destination name
   * @throws JMSException if the destination cannot be destroyed
   */
  public void destroyDestination(String name) throws JMSException {
    try {
      Object lookup = lookup(name);
      if(lookup instanceof JmsQueue){
        properties.remove("queue."+name);
        daemonManager.delete(name, false);
      }
      else if(lookup instanceof JmsTopic){
        properties.remove("topic."+name);
        daemonManager.delete(name, true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns true if an administered destination exists
   *
   * @param name the destination name
   * @throws JMSException for any internal JMS provider error
   */
  public boolean destinationExists(String name)
      throws JMSException {

    boolean exists = false;
    try {
      getContext().lookup(name);
      exists = true;
    } catch (NameNotFoundException ignore) {
    } catch (Exception exception) {
      JMSException error = new JMSException(exception.getMessage());
      error.setLinkedException(exception);
      throw error;
    }
    return exists;
  }

  private static Collection<String> getResources(final Pattern pattern) throws IOException {
    final ArrayList<String> returnValue = new ArrayList<>();
    final String classPath = System.getProperty("basedir", ".");
    final String[] classPathElements = classPath.split(System.getProperty("path.separator"));
    for (final String element : classPathElements) {
      returnValue.addAll(getResources(element, pattern));
    }
    return returnValue;
  }

  private static Collection<String> getResources(final String element, final Pattern pattern)
      throws IOException {
    final ArrayList<String> returnValue = new ArrayList<>();
    final File file = new File(element);
    if (file.isDirectory()) {
      returnValue.addAll(getResourcesFromDirectory(file, pattern));
    } else {
      returnValue.addAll(getResourcesFromJarFile(file, pattern));
    }
    return returnValue;
  }

  private static Collection<String> getResourcesFromJarFile(final File file, final Pattern pattern)
      throws IOException {
    final ArrayList<String> returnValue = new ArrayList<>();
    ZipFile zf = new ZipFile(file);

    final Enumeration<? extends ZipEntry> e = zf.entries();
    while (e.hasMoreElements()) {
      final ZipEntry ze = e.nextElement();
      final String fileName = ze.getName();
      final boolean accept = pattern.matcher(fileName).matches();
      if (accept) {
        returnValue.add(fileName);
      }
    }
    zf.close();
    return returnValue;
  }

  private static Collection<String> getResourcesFromDirectory(
      final File directory, final Pattern pattern) throws IOException {
    final ArrayList<String> returnValue = new ArrayList<>();
    final File[] fileList = directory.listFiles();
    if (fileList != null && fileList.length > 0) {
      for (final File file : fileList) {
        if (file.isDirectory()) {
          returnValue.addAll(getResourcesFromDirectory(file, pattern));
        } else {
          final String fileName = file.getCanonicalPath();
          final boolean accept = pattern.matcher(fileName).matches();
          if (accept) {
            returnValue.add(fileName);
          }
        }
      }
    }
    return returnValue;
  }
}