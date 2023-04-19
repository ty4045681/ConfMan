package com.finale.ConferenceManagement;

import com.finale.ConferenceManagement.configuration.FileStorageProperties;
import com.finale.ConferenceManagement.initializer.SampleDataInitializerAndDeleter;
import com.finale.ConferenceManagement.util.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class ConferenceManagementApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ConferenceManagementApplication.class, args);

		SampleDataInitializerAndDeleter initializerAndDeleter = context.getBean(SampleDataInitializerAndDeleter.class);
		initializerAndDeleter.deleteAllData();
		initializerAndDeleter.insertData("user", "conference", "attendance");

		JwtUtils jwtUtils = context.getBean(JwtUtils.class);
		System.out.println("JWT expiration time: ");
		System.out.println(jwtUtils.getJwtExpirationInMs());
	}

}
