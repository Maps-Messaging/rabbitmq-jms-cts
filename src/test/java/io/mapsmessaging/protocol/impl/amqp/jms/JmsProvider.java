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


import org.exolab.jmscts.provider.Administrator;
import org.exolab.jmscts.provider.Provider;

public class JmsProvider  implements Provider {

  private Administrator _admin = new JmsAdministrator();

  public JmsProvider() {
    System.err.println("JMS Provider starting");
  }

  public void initialise(boolean start) {
  }

  public Administrator getAdministrator() {
    return _admin;
  }

  public void cleanup(boolean stop) {
  }
}
