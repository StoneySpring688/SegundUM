package SegundUM.pasarela;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/** Punto de entrada del microservicio pasarela; activa el proxy Zuul para enrutar tráfico externo. */
@SpringBootApplication
@EnableZuulProxy
public class App implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	@Value("${server.port:8090}")
	private String port;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("=========================================");
		logger.info("=== Microservicio PASARELA iniciado  ===");
		logger.info("Documentacion: http://localhost:{}/swagger-ui.html", port);
		logger.info("=========================================");
	}
}
