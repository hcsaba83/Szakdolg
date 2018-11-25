package com.szakdolg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@EnableConfigurationProperties
@PropertySources({
    @PropertySource(value = "file:${appConf}", ignoreResourceNotFound = true)
})
@SpringBootApplication
public class SzakDolgApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SzakDolgApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	  return application.sources(SzakDolgApplication.class);
    }

	
}