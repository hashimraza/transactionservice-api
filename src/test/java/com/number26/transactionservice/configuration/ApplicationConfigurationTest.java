package com.number26.transactionservice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = { "com.number26.transactionservice.business", "com.number26.transactionservice.data" })
@Configuration
public class ApplicationConfigurationTest {
}
