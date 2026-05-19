package SegundUM.Compraventas.repositorio.compraventa;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import SegundUM.Compraventas.dominio.Compraventa;

/** Adaptador de persistencia MongoDB que combina las consultas personalizadas del puerto con las de Spring Data. */
@Repository
public interface RepositorioCompraventaMongo extends RepositorioCompraventa, MongoRepository<Compraventa, String> {
	
	
}
