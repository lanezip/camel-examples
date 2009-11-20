/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.gae;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class WeatherProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        // convert XML body to DOM tree
        Document doc = exchange.getIn().getBody(Document.class);

        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xpath = xpfactory.newXPath();

        // Extract result values via XPath
        String city = xpath.evaluate("//forecast_information/city/@data", doc);
        String cond = xpath.evaluate("//current_conditions/condition/@data", doc);
        String temp = xpath.evaluate("//current_conditions/temp_c/@data", doc);

        String msg = null;
        if (city != null && city.length() > 0) {
            msg = new StringBuffer()
                .append("\n").append("Weather report for:  ").append(city)
                .append("\n").append("Current condition:   ").append(cond)
                .append("\n").append("Current temperature: ").append(temp).append(" (Celsius)").toString();
        } else {
            // create an error message
            msg = "Error getting weather report for " + exchange.getIn().getHeader("city", String.class);
        }
        exchange.getIn().setBody(msg);
    }

}