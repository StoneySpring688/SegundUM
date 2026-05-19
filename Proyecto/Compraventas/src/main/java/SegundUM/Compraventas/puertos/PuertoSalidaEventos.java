package SegundUM.Compraventas.puertos;

import SegundUM.Compraventas.adaptadores.RabbitMQ.eventos.Evento;

public interface PuertoSalidaEventos {

    void publicar(Evento evento);
}
