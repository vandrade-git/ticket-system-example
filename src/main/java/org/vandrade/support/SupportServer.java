package org.vandrade.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 12:42 PM
 */

@SpringBootApplication
public class SupportServer {
    public static void main(String[] args) {
        System.setProperty("org.jooq.no-logo", "true");
        SpringApplication.run(SupportServer.class, args);
    }
}