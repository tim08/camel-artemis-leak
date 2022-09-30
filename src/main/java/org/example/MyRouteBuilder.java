package org.example;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;

import javax.jms.ConnectionFactory;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        ConnectionFactory connectionFactory = new org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory("tcp://localhost:61616", "admin", "admin");
        JmsPoolConnectionFactory pooledConnectionFactory = new JmsPoolConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        pooledConnectionFactory.setMaxSessionsPerConnection(100);

        JmsComponent jms = new org.apache.camel.component.jms.JmsComponent();
        jms.setConnectionFactory(pooledConnectionFactory);

        this.getContext().addComponent("jms", jms);

        final XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();

        from("jetty:http://0.0.0.0:8000/put")
                .marshal(xmlJsonFormat)
                .inOnly("jms:test-queue")
                .setBody(simple(""));
        from("jetty:http://0.0.0.0:8000/get")
                .pollEnrich("jms:test-queue", 0);
    }

}
