package SegundUM.Productos.adaptadores.Retrofit;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import SegundUM.Productos.puertos.PuertoUsuarios;

/** Adaptador de salida HTTP que implementa PuertoUsuarios usando Retrofit para llamar al microservicio Usuarios. */
@Component
public class AdaptadorUsuariosRetrofit implements PuertoUsuarios {

    private static final Logger logger = LoggerFactory.getLogger(AdaptadorUsuariosRetrofit.class);

    private final ApiUsuariosRetrofit api;
    
    @Autowired
    public AdaptadorUsuariosRetrofit(ApiUsuariosRetrofit api) {
        this.api = api;
    }

    @Override
    public String getIdByEmail(String email) throws IOException {
        logger.info("Consultando ID de usuario por email {} via Retrofit", email);
       
            IdUsuarioDTO dto = api.getIdByEmail(email).execute().body();
            if (dto == null || dto.id == null || dto.id.isEmpty()) {
                throw new RuntimeException("Usuario con email " + email + " no encontrado");
            }
            return dto.id;
      
    }
}
