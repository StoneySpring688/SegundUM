package SegundUM.Productos.puertos;

import java.io.IOException;

public interface PuertoUsuarios {

    String getIdByEmail(String email) throws IOException;
}
