package com.managementSystem;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

/**
 * @author ZYOOO
 * @date 2021-09-16 17:33
 */
public class ComDemo {
    public static void main(String[] args) {
        CommPortIdentifier portId;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        // iterate through the ports.
        while (en.hasMoreElements()) {
            portId = (CommPortIdentifier) en.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(portId.getName());
            }
        }
    }
    public void listPortChoices() {

    }
}
