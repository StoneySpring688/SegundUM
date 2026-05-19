package SegundUM.Compraventas.adaptadores.RabbitMQ.eventos;

public abstract class Evento {
    private String tipo;
    private String fechaHora;

    protected Evento() {}

    protected Evento(String tipo, String fechaHora) {
        this.tipo = tipo;
        this.fechaHora = fechaHora;
    }

    public String getTipo() { return tipo; }
    public String getFechaHora() { return fechaHora; }
}
