package com.arturjoshi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.integration.annotation.IntegrationComponentScan;

//@EnableDiscoveryClient
@IntegrationComponentScan
@SpringBootApplication
public class TrinjerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrinjerServiceApplication.class, args);
	}
}
