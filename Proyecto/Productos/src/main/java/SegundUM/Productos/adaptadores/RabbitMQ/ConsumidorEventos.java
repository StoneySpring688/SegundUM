package SegundUM.Productos.adaptadores.RabbitMQ;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import SegundUM.Productos.adaptadores.RabbitMQ.eventos.EventoCreacionCompraventa;
import SegundUM.Productos.adaptadores.RabbitMQ.eventos.EventoEliminacionUsuario;
import SegundUM.Productos.puertos.PuertoEntradaEventos;

/** Adaptador de entrada RabbitMQ; despacha eventos recibidos al puerto de entrada del dominio. */
@Component
public class ConsumidorEventos {

    private static final Logger logger = LoggerFactory.getLogger(ConsumidorEventos.class);

    private final PuertoEntradaEventos puertoEntradaEventos;
    private final ObjectMapper objectMapper;

    public ConsumidorEventos(PuertoEntradaEventos puertoEntradaEventos, ObjectMapper objectMapper) {
        this.puertoEntradaEventos = puertoEntradaEventos;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfigProductos.QUEUE_NAME)
    public void recibirEvento(Message message, @Header("amqp_receivedRoutingKey") String routingKey) {
        
        // Decodificar el cuerpo del mensaje como texto UTF-8 antes de deserializar
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        logger.info("Evento recibido en cola productos - routingKey: {}", routingKey);

        try {
            switch (routingKey) {
                case "bus.compraventas.compraventa-creada":
                    EventoCreacionCompraventa evtCompraventa = objectMapper.readValue(json, EventoCreacionCompraventa.class);
                    puertoEntradaEventos.manejarCompraventaCreada(evtCompraventa.getIdProducto());
                    break;
                case "bus.usuarios.usuario-eliminado":
                    EventoEliminacionUsuario evtUsuario = objectMapper.readValue(json, EventoEliminacionUsuario.class);
                    puertoEntradaEventos.manejarUsuarioEliminado(evtUsuario.getIdUsuario());
                    break;
                default:
                    logger.warn("Evento no reconocido: {}", routingKey);
            }
        } catch (Exception e) {
            logger.error("Error al procesar evento {}: {}", routingKey, e.getMessage(), e);
        }
    }
}
