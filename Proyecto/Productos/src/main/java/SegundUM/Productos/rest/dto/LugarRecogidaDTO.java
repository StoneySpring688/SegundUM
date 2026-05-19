package SegundUM.Productos.rest.dto;


import org.springframework.hateoas.RepresentationModel;

import SegundUM.Productos.dominio.LugarRecogida;

public class LugarRecogidaDTO extends RepresentationModel<LugarRecogidaDTO> {
    
	public String descripcion;

    public Double longitud;
    
    public Double latitud;

    public LugarRecogidaDTO() {}

    public static LugarRecogidaDTO fromEntity(LugarRecogida entidad) {
        if (entidad == null) return null;
        LugarRecogidaDTO dto = new LugarRecogidaDTO();
        dto.descripcion = entidad.getDescripcion();
        dto.longitud = entidad.getLongitud();
        dto.latitud = entidad.getLatitud();
        return dto;
    }

    public LugarRecogida toEntity() {
        return new LugarRecogida(this.descripcion, this.longitud, this.latitud);
    }
    
    @Override
    public String toString() {
    			return "LugarRecogidaDTO{" +
				"descripcion='" + descripcion + '\'' +
				", longitud=" + longitud +
				", latitud=" + latitud +
				'}';
    }
}