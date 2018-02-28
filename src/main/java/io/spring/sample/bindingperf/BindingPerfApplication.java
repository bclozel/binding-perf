package io.spring.sample.bindingperf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(InitializrProperties.class)
public class BindingPerfApplication {

	public static void main(String[] args) {
		SpringApplication.run(BindingPerfApplication.class, args);
	}

}
