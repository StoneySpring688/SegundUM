package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** Centraliza la configuración de RabbitMQ del microservicio Usuarios: credenciales, exchange, cola y bindings. */
public class RabbitMQConfigUsuarios {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfigUsuarios.class);

    public static final String EXCHANGE_NAME = "bus";
    public static final String QUEUE_NAME = "usuarios";
    public static final String ROUTING_KEY = "bus.usuarios.";

    // --- Suscripciones (patrones de routing key) ---
    private static final String[] BINDINGS = {
            "bus.compraventas.compraventa-creada",
            "bus.productos.producto-creado",
            "bus.productos.producto-eliminado"
    };

    private static final String HOST = env("RABBITMQ_HOST", "rat.rmq2.cloudamqp.com");
    private static final int PORT = Integer.parseInt(env("RABBITMQ_PORT", "5671"));
    private static final String USERNAME = env("RABBITMQ_USERNAME", "cfrvyzor");
    private static final String PASSWORD = env("RABBITMQ_PASSWORD", "Y2mLAqiR1mOFZBBnupB6UDZ6o9E778iX");
    private static final String VIRTUAL_HOST = env("RABBITMQ_VHOST", "cfrvyzor");
    private static final boolean USE_SSL = Boolean.parseBoolean(env("RABBITMQ_SSL", "true"));

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value != null ? value : defaultValue;
    }

    public static ConnectionFactory crearConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        logger.info("Creando ConnectionFactory RabbitMQ para Usuarios (host: {})", HOST);
        if (USE_SSL) {
            try {
                factory.useSslProtocol();
            } catch (Exception e) {
                throw new RuntimeException("Error al configurar SSL para RabbitMQ", e);
            }
        }
        return factory;
    }

    public static void configurarBindings(Channel channel) throws IOException {
        for (String pattern : BINDINGS) {
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, pattern);
        }
        logger.info("Bindings configurados para la cola de Usuarios: {} suscripciones", BINDINGS.length);
    }
}
