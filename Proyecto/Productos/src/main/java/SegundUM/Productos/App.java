package SegundUM.Productos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del microservicio Productos; registra la URL de la documentación al arrancar. */
@SpringBootApplication
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
		logger.info("=== Microservicio PRODUCTOS iniciado  ===");
		logger.info("Documentacion: {}", BASE_URI);
		logger.info("=========================================");
	}
}
