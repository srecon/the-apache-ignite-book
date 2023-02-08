package com.blu.imdg.example9;

import com.blu.imdg.common.CommonConstants;

import com.blu.imdg.example9.exception.AccountNotFoundException;
import com.blu.imdg.example9.exception.LogServiceException;

import com.sun.net.httpserver.HttpServer;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;


import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * http://localhost:9988/service/withdrawlimit?accountnum=0000*1111&amount=100
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.ServiceHttpClient
 * */

public class ServiceHttpClient {
    private static BankService bankService;

    public static void main(String[] args) throws IOException {
        Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG);
        IgniteServices services = ignite.services().withAsync();

        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9988),0);
        httpServer.createContext("/service/withdrawlimit",
                (exchange -> {
                    String response = "Result";
                    String accnum = "";
                    int amount = 0;
                    boolean result = false;
                    Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

                    accnum = params.get("accountnum").get(0);
                    amount = Integer.parseInt(params.get("amount").get(0));

                    bankService = services.serviceProxy(BankService.NAME, BankService.class, /*not-sticky*/false);
                    try{
                        result = bankService.validateOperation(accnum, new BigDecimal(amount));
                    } catch (AccountNotFoundException | LogServiceException e) {
                        System.out.println(e.getMessage());
                    }
                    response = response +": "+ result;
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(response.getBytes());
                    output.flush();
                    exchange.close();
                }) );
        httpServer.start();
        System.out.println("Http Server on localhost:9988");
    }
    private static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));
    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
