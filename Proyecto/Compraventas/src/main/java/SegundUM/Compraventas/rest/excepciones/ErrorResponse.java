package SegundUM.Compraventas.rest.excepciones;

public class ErrorResponse {
    private int codigo;
    private String mensaje;

    public ErrorResponse(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() { return codigo; }
    public String getMensaje() { return mensaje; }
}
