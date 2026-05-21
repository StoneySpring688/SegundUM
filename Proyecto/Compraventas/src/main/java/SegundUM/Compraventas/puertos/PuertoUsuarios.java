package SegundUM.Compraventas.puertos;

import SegundUM.Compraventas.rest.dto.UsuarioDTO;

public interface PuertoUsuarios {
	
	UsuarioDTO obtenerDatosUsuario(String token);
	UsuarioDTO obtenerDatosVendedor(String idUsuario, String token);
	
}
