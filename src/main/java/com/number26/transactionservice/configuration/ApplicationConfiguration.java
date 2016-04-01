/**
 * 
 */
package com.number26.transactionservice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author HashimR
 *
 */
@EnableWebMvc
@ComponentScan(basePackages = "com.number26")
@Configuration
public class ApplicationConfiguration {

}
