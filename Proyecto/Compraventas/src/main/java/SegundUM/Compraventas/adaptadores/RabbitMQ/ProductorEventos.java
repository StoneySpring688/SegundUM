package SegundUM.Compraventas.adaptadores.RabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import SegundUM.Compraventas.adaptadores.RabbitMQ.eventos.Evento;
import SegundUM.Compraventas.puertos.PuertoSalidaEventos;

/** Adaptador de salida RabbitMQ que publica eventos de dominio en el exchange del bus. */
@Component
public class ProductorEventos implements PuertoSalidaEventos {

    private static final Logger logger = LoggerFactory.getLogger(ProductorEventos.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductorEventos(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(Evento evento) {

        // La routing key se forma concatenando el prefijo fijo con el tipo del evento
        rabbitTemplate.convertAndSend(
                RabbitMQConfigCompraventas.EXCHANGE_NAME,
                RabbitMQConfigCompraventas.ROUTING_KEY + evento.getTipo(),
                evento);
        
        logger.info("Publicando evento: {} - {}", evento.getTipo());

    }
}
