package SegundUM.Usuarios.usuarios.modelo;

import javax.persistence.*;

import SegundUM.Usuarios.repositorio.Identificable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Entidad JPA que representa un usuario del sistema, incluyendo datos de perfil, contadores y referencia OAuth2 de GitHub. */
@Entity
@Table(name = "usuarios")
public class Usuario implements Identificable {
    
    @Id
    private String id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = true)
    private String apellidos;
    
    // se queda nullable para permitir oauth2
    @Column(nullable = true)
    private String clave;
    
    @Column(name = "fecha_nacimiento", nullable = true)
    private LocalDate fechaNacimiento;

    private int comprasRealizadas;
    
    private int ventasRealizadas;
    
    private String telefono;
    
    @Column(nullable = false)
    private boolean administrador;

    @Column(name = "id_github", unique = true, nullable = true)
    private String idGitHub;

    @ElementCollection
    @CollectionTable(name = "productos_id")
    private List<String> productos = new ArrayList<>();
    
    // Constructor por defecto para JPA
    protected Usuario() {}
    
    public Usuario(String id, String email, String nombre, String apellidos, 
                   String clave, LocalDate fechaNacimiento, String telefono) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.clave = clave;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.administrador = false;
        this.comprasRealizadas = 0;
        this.ventasRealizadas = 0;
    }
    
    // Getters y setters (implementa Identificable)
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void addProducto(String productoId) {
        this.productos.add(productoId);
    }

    public void removeProducto(String productoId) {
        this.productos.remove(productoId);
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getClave() {
        return clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public boolean isAdministrador() {
        return administrador;
    }
    
    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
    
    public List<String> getProductosId() {
        return productos;
    }

    public void setComprasRealizadas(int comprasRealizadas) {
        this.comprasRealizadas = comprasRealizadas;
    }

    public int getComprasRealizadas() {
        return comprasRealizadas;
    }

    public void addCompraRealizada() {
       comprasRealizadas++;
    }

    public void setVentasRealizadas(int ventasRealizadas) {
        this.ventasRealizadas = ventasRealizadas;
    }

    public int getVentasRealizadas() {
        return ventasRealizadas;
    }

    public void addVenta() {
       ventasRealizadas++;
    }

    public String getIdGitHub() {
        return idGitHub;
    }

    public void setIdGitHub(String idGitHub) {
        this.idGitHub = idGitHub;
    }
    
    @Override
    public String toString() {
    			return "Usuario{" +
				"id='" + id + '\'' +
				", email='" + email + '\'' +
				", nombre='" + nombre + '\'' +
				", apellidos='" + apellidos + '\'' +
				", fechaNacimiento=" + fechaNacimiento +
				", telefono='" + telefono + '\'' +
				", administrador=" + administrador +
				'}';
    }
    
}