package SegundUM.Productos.adaptadores.RabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import SegundUM.Productos.adaptadores.RabbitMQ.eventos.Evento;
import SegundUM.Productos.puertos.PuertoSalidaEventos;

/** Adaptador de salida RabbitMQ; publica eventos del dominio en el exchange del bus. */
@Component
public class ProductorEventos implements PuertoSalidaEventos {

    private static final Logger logger = LoggerFactory.getLogger(ProductorEventos.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductorEventos(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(Evento evento) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfigProductos.EXCHANGE_NAME,
                RabbitMQConfigProductos.ROUTING_KEY + evento.getTipo(),
                evento);

        logger.info("Evento publicado: {} - {}", evento.getTipo(), evento);
    }
}
