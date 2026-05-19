package SegundUM.Compraventas.adaptadores.RabbitMQ;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import SegundUM.Compraventas.adaptadores.RabbitMQ.eventos.EventoEliminacionProducto;
import SegundUM.Compraventas.adaptadores.RabbitMQ.eventos.EventoEliminacionUsuario;
import SegundUM.Compraventas.adaptadores.RabbitMQ.eventos.EventoModificacionUsuario;
import SegundUM.Compraventas.puertos.PuertoEntradaEventos;

/** Adaptador de entrada RabbitMQ que recibe eventos del bus y los delega al puerto de entrada. */
@Component
public class ConsumidorEventos {

    private static final Logger logger = LoggerFactory.getLogger(ConsumidorEventos.class);

    private final PuertoEntradaEventos puertoEntradaEventos;
    private final ObjectMapper objectMapper;

    public ConsumidorEventos(PuertoEntradaEventos puertoEntradaEventos, ObjectMapper objectMapper) {
        this.puertoEntradaEventos = puertoEntradaEventos;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfigCompraventas.QUEUE_NAME)
    public void recibirEvento(Message message, @Header("amqp_receivedRoutingKey") String routingKey) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        logger.info("Evento recibido en cola compraventas - routingKey: {}", routingKey);

        try {
            // Despachar al handler correcto según la routing key del evento recibido
            switch (routingKey) {
                case "bus.usuarios.usuario-modificado":
                    EventoModificacionUsuario evtUsuarioMod = objectMapper.readValue(json, EventoModificacionUsuario.class);
                    puertoEntradaEventos.manejarUsuarioModificado(
                            evtUsuarioMod.getIdUsuario(),
                            evtUsuarioMod.getNombre()
                    );
                    break;
                case "bus.usuarios.usuario-eliminado":
                    EventoEliminacionUsuario evtUsuarioElim = objectMapper.readValue(json, EventoEliminacionUsuario.class);
                    puertoEntradaEventos.manejarUsuarioEliminado(evtUsuarioElim.getIdUsuario());
                    break;
                case "bus.productos.producto-eliminado":
                    EventoEliminacionProducto evtProductoElim = objectMapper.readValue(json, EventoEliminacionProducto.class);
                    puertoEntradaEventos.manejarProductoEliminado(evtProductoElim.getIdProducto());
                    break;
                default:
                    logger.warn("Evento no reconocido: {}", routingKey);
            }
        } catch (Exception e) {
            logger.error("Error al procesar evento {}: {}", routingKey, e.getMessage(), e);
        }
    }
}
