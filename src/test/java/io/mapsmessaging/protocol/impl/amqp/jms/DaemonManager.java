package io.mapsmessaging.protocol.impl.amqp.jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DaemonManager {

  private final String hostname;

  private String daemonName;

  public DaemonManager(String hostname){
    this.hostname = hostname;
    daemonName = null;
  }

  public void create(String name, boolean topic) throws IOException, ParseException {
    if(daemonName == null){
      daemonName  = getDaemonName();
    }
    StringBuilder url = new StringBuilder();
    url.append(hostname)
        .append("/hawtio/jolokia/exec/io.mapsmessaging:type=Broker,%20brokerName=")
        .append(daemonName)
        .append("/createDestination(java.lang.String,boolean)/")
        .append(name)
        .append("/")
        .append(topic);

    URLConnection connection = new URL(url.toString()).openConnection();
    connection.connect();
    handleResponse(connection);
  }

  public void delete(String name, boolean topic) throws IOException, ParseException {
    if(daemonName == null){
      daemonName  = getDaemonName();
    }
    String type = "Topic";
    if(!topic){
      type = "Queue";
    }
    StringBuilder url = new StringBuilder();
    url.append(hostname)
        .append("/hawtio/jolokia/exec/io.mapsmessaging:type=Broker,%20brokerName=")
        .append(daemonName)
        .append(",%20destinationType=")
        .append(type)
        .append(",%20destinationName=")
        .append(name)
        .append("/delete()");
    URLConnection connection = new URL(url.toString()).openConnection();
    connection.connect();
    handleResponse(connection);
  }

  public String getDaemonName() throws IOException, ParseException {
    URLConnection connection = new URL(hostname+"/hawtio/jolokia/read/io.mapsmessaging:type=Broker/getName").openConnection();
    connection.connect();
    InputStream is = connection.getInputStream();
    char[] buffer = new char[1024];
    StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
    int charsRead;
    while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
      out.append(buffer, 0, charsRead);
    }
    is.close();
    String result = out.toString();
    JSONParser parser = new JSONParser();
    JSONObject object = (JSONObject) parser.parse(result);
    return (String) object.get("value");
  }


  private void handleResponse(URLConnection connection) throws IOException {
    InputStream is = connection.getInputStream();
    char[] buffer = new char[1024];
    StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
    int charsRead;
    while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
      out.append(buffer, 0, charsRead);
    }
    is.close();
  }

}
