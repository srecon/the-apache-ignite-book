package com.blu.imdg;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

public class HelloThinClient {
    public static void main(String[] args) {
        System.out.println("Simple helloworld example of the Ignite thin client..");
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {
            final String CACHE_NAME = "thin-cache";
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
            // put a few value
            clientCache.put("Moscow", "095");
            clientCache.put("Vladimir", "033");
            // get the same value
            String val = clientCache.get("Vladimir");
            System.out.println("Value:" + val);


        } catch(IgniteException e){
            System.out.println(e.getMessage());

        } catch(Exception e){
            System.out.println(e.getMessage());

        }

    }
}
