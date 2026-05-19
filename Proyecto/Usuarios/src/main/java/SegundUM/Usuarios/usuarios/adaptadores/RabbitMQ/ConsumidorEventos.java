package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.EventoCreacionCompraventa;
import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.EventoCreacionProducto;
import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.EventoEliminacionProducto;
import SegundUM.Usuarios.usuarios.puertos.PuertoEntradaEventos;

/** Adaptador de entrada RabbitMQ: suscribe la cola de Usuarios y delega cada evento en PuertoEntradaEventos. */
public class ConsumidorEventos {

    private static final Logger logger = LoggerFactory.getLogger(ConsumidorEventos.class);
    private static final Gson gson = new Gson();

    private final ConnectionFactory connectionFactory;
    private final PuertoEntradaEventos puertoEntradaEventos;

    public ConsumidorEventos(ConnectionFactory connectionFactory, PuertoEntradaEventos puertoEntradaEventos) {
        this.connectionFactory = connectionFactory;
        this.puertoEntradaEventos = puertoEntradaEventos;
    }

    public void iniciar() {
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            RabbitMQConfigUsuarios.configurarBindings(channel);

            logger.info("Consumidor RabbitMQ de Usuarios iniciado. Cola: {}", RabbitMQConfigUsuarios.QUEUE_NAME);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String routingKey = delivery.getEnvelope().getRoutingKey();
                String cuerpo = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("Evento recibido en cola usuarios - routingKey: {}", routingKey);

                try {
                    recibirEvento(routingKey, cuerpo);
                } catch (Exception e) {
                    logger.error("Error al procesar evento {}: {}", routingKey, e.getMessage(), e);
                }
            };

            // autoAck=true: los mensajes se confirman automáticamente al recibirlos
            channel.basicConsume(RabbitMQConfigUsuarios.QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            logger.error("Error al iniciar consumidor RabbitMQ de Usuarios: {}", e.getMessage(), e);
        }
    }

    private void recibirEvento(String routingKey, String cuerpo) {
        try {
            switch (routingKey) {
                case "bus.compraventas.compraventa-creada":
                    EventoCreacionCompraventa evtCompraventa = gson.fromJson(cuerpo, EventoCreacionCompraventa.class);
                    puertoEntradaEventos.manejarCompraventaCreada(
                            evtCompraventa.getIdComprador(),
                            evtCompraventa.getIdVendedor()
                    );
                    break;
                case "bus.productos.producto-creado":
                    EventoCreacionProducto evtProductoCreado = gson.fromJson(cuerpo, EventoCreacionProducto.class);
                    puertoEntradaEventos.manejarProductoCreado(
                            evtProductoCreado.getIdProducto(),
                            evtProductoCreado.getVendedorId()
                    );
                    break;
                case "bus.productos.producto-eliminado":
                    EventoEliminacionProducto evtProductoEliminado = gson.fromJson(cuerpo, EventoEliminacionProducto.class);
                    puertoEntradaEventos.manejarProductoEliminado(
                            evtProductoEliminado.getIdProducto(),
                            evtProductoEliminado.getVendedorId()
                    );
                    break;
                default:
                    logger.warn("Evento no reconocido: {}", routingKey);
            }
        } catch (Exception e) {
            logger.error("Error al despachar evento {}: {}", routingKey, e.getMessage(), e);
        }
    }
}
