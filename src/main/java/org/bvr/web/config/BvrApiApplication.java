package org.bvr.web.config;

import com.bvr.core.configuration.CoreConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Log4j2
@SpringBootApplication(exclude={
		MongoAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
})
@ComponentScan(basePackages={"org.bvr.web","com.bvr.web","com.api.*"})
@Import({CoreConfiguration.class})
public class BvrApiApplication {

	public static void main(String[] args) {
		log.info("Application starting....");
		SpringApplication.run(BvrApiApplication.class, args);
	}

}
