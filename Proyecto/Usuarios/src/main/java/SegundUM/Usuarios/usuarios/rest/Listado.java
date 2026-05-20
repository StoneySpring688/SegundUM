package SegundUM.Usuarios.usuarios.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import SegundUM.Usuarios.usuarios.modelo.ResumenUsuario;

@XmlRootElement
public class Listado {

    private List<ResumenExtendido> resumenEntidad;

    public List<ResumenExtendido> getResumenEntidad() {
        return resumenEntidad;
    }

    public void setResumenEntidad(List<ResumenExtendido> resumenEntidad) {
        this.resumenEntidad = resumenEntidad;
    }

    public static class ResumenExtendido {

        private String url;
        private ResumenUsuario resumen;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public ResumenUsuario getResumen() { return resumen; }
        public void setResumen(ResumenUsuario resumen) { this.resumen = resumen; }
    }
}
