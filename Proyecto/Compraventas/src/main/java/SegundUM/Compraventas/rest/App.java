package SegundUM.Compraventas.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/** Punto de entrada del microservicio de Compraventas; registra la URL de Swagger al arrancar. */
@SpringBootApplication(scanBasePackages = "SegundUM.Compraventas")
@EnableMongoRepositories(basePackages = "SegundUM.Compraventas.repositorio")
@EntityScan(basePackages = "SegundUM.Compraventas.dominio")
public class App implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	@Value("${api.url.swaggerui}")
	private String BASE_URI;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("=========================================");
		logger.info("=== Microservicio COMPRAVENTAS iniciado ===");
		logger.info("Documentacion: {}", BASE_URI);
		logger.info("=========================================");
	}
}
