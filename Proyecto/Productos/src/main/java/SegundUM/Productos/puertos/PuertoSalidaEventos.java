package SegundUM.Productos.puertos;

import SegundUM.Productos.adaptadores.RabbitMQ.eventos.Evento;

public interface PuertoSalidaEventos {

    void publicar(Evento evento);
}
