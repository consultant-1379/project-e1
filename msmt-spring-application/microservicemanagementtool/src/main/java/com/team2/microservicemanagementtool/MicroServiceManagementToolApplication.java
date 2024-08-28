package com.team2.microservicemanagementtool;

import com.team2.microservicemanagementtool.persistence.MsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MicroServiceManagementToolApplication {

	public static void main(String[] args) {
		//SpringApplication.run(MicroServiceManagementToolApplication.class, args);
		ApplicationContext context = SpringApplication.run(MicroServiceManagementToolApplication.class, args);
		MsService service = context.getBean(MsService.class);
		service.test();
	}

}
