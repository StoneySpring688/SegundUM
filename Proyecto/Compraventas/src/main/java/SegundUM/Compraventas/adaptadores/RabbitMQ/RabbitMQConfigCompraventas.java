package SegundUM.Compraventas.adaptadores.RabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/** Configuración de RabbitMQ: declara el exchange, la cola y los bindings que consume este microservicio. */
@Configuration
public class RabbitMQConfigCompraventas {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfigCompraventas.class);

    public static final String EXCHANGE_NAME = "bus";
    public static final String QUEUE_NAME = "compraventas";
    public static final String ROUTING_KEY = "bus.compraventas.";

    // Referencias al exchange y cola (creados por rabbitmq-setup)  
    @Bean
    public TopicExchange busExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue compraventasQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    // --- Suscripciones (bindings) ---

    @Bean
    public Binding compraventasBindingUsuariosModificado(Queue compraventasQueue, TopicExchange busExchange) {
        logger.info("Binding creado: {} -> {}", "bus.usuarios.usuario-modificado", QUEUE_NAME);
        return BindingBuilder.bind(compraventasQueue).to(busExchange).with("bus.usuarios.usuario-modificado");
    }

    @Bean
    public Binding compraventasBindingUsuariosEliminado(Queue compraventasQueue, TopicExchange busExchange) {
        logger.info("Binding creado: {} -> {}", "bus.usuarios.usuario-eliminado", QUEUE_NAME);
        return BindingBuilder.bind(compraventasQueue).to(busExchange).with("bus.usuarios.usuario-eliminado");
    }

    @Bean
    public Binding compraventasBindingProductosEliminado(Queue compraventasQueue, TopicExchange busExchange) {
        logger.info("Binding creado: {} -> {}", "bus.productos.producto-eliminado", QUEUE_NAME);
        return BindingBuilder.bind(compraventasQueue).to(busExchange).with("bus.productos.producto-eliminado");
    }

    // --- Mensajeria ---

     @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
