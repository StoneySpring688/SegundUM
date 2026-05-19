package SegundUM.Usuarios;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.ConnectionFactory;

import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.ProductorEventos;
import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.ConsumidorEventos;
import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.RabbitMQConfigUsuarios;
import SegundUM.Usuarios.usuarios.puertos.PuertoEntradaEventos;
import SegundUM.Usuarios.servicio.FactoriaServicios;
import SegundUM.Usuarios.usuarios.servicio.ServicioUsuarios;
import SegundUM.Usuarios.usuarios.servicio.ServicioUsuariosImpl;

/** Punto de entrada del microservicio Usuarios: arranca Grizzly/JAX-RS en el puerto 8081 y conecta el bus de eventos RabbitMQ. */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String BASE_URI = "http://0.0.0.0:8081/api/";

    public static void main(String[] args) {
        try {
            ResourceConfig config = new ResourceConfig()
                    .packages("SegundUM.Usuarios.usuarios.rest",
                              "SegundUM.Usuarios.rest.excepciones",
                              "SegundUM.Usuarios.util",
                              "SegundUM.Usuarios.auth")
                    .register(JacksonFeature.class);

            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(BASE_URI), config);

            logger.info("=========================================");
            logger.info("=== Microservicio USUARIOS iniciado   ===");
            logger.info("API: {}", BASE_URI);
            logger.info("=========================================");

            // --- Configurar bus de eventos RabbitMQ ---
            ConnectionFactory connectionFactory = RabbitMQConfigUsuarios.crearConnectionFactory();

            ServicioUsuariosImpl servicio = (ServicioUsuariosImpl) FactoriaServicios.getServicio(ServicioUsuarios.class);
            servicio.setPuertoSalidaEventos(new ProductorEventos(connectionFactory));

            new ConsumidorEventos(connectionFactory, (PuertoEntradaEventos) servicio).iniciar();


            logger.info("Bus de eventos RabbitMQ configurado correctamente");


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Cerrando microservicio Usuarios...");
                server.shutdownNow();
            }));

        } catch (Exception e) {
            logger.error("Error al arrancar el microservicio de Usuarios", e);
        }
    }
}
