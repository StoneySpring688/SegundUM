package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.Evento;
import SegundUM.Usuarios.usuarios.puertos.PuertoSalidaEventos;

/** Adaptador de salida RabbitMQ: serializa y publica eventos de Usuarios en el exchange del bus. */
public class ProductorEventos implements PuertoSalidaEventos {

    private static final Logger logger = LoggerFactory.getLogger(ProductorEventos.class);
    private static final Gson gson = new Gson();

    private final ConnectionFactory connectionFactory;

    public ProductorEventos(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void publicar(Evento evento) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            
            channel.basicPublish(
                    RabbitMQConfigUsuarios.EXCHANGE_NAME,
                    RabbitMQConfigUsuarios.ROUTING_KEY + evento.getTipo(),
                    null,
                    gson.toJson(evento).getBytes("UTF-8"));
            
            logger.info("Evento publicado: {} - {}", evento.getTipo(), evento);

        } catch (Exception e) {
            logger.error("Error al publicar evento {}: {}", evento.getTipo(), e.getMessage(), e);
        }
    }
}
