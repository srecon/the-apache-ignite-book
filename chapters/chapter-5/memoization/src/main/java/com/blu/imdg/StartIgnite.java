package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

public class StartIgnite {
    public static void main(String[] args) {
        System.out.println("Start Ignite in external node!");
        Ignite ignite = Ignition.start("ignite-l2-config.xml");
        System.out.println("enter CRTL+x to stop the node!");
    }
}
