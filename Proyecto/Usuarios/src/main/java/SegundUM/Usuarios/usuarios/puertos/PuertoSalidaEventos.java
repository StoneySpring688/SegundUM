package SegundUM.Usuarios.usuarios.puertos;

import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.Evento;

public interface PuertoSalidaEventos {

    void publicar(Evento evento);
}
