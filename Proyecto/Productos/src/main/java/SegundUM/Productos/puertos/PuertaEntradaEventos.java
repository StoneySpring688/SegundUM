package SegundUM.Productos.puertos;

import SegundUM.Productos.repositorio.EntidadNoEncontrada;

/**
 * Puerto de entrada para manejar eventos recibidos desde el bus de mensajería.
 *
 * Siguiendo la arquitectura hexagonal, este puerto define las operaciones
 * que el microservicio de Productos ofrece como reacción a eventos externos.
 */
public interface PuertaEntradaEventos {

    /**
     * Maneja el evento de compraventa creada:
     * - Marca el producto como vendido
     *
     * @param idProducto ID del producto vendido
     * @throws EntidadNoEncontrada si el producto referenciado no existe
     */
    void manejarCompraventaCreada(String idProducto) throws EntidadNoEncontrada;

    /**
     * Maneja el evento de usuario eliminado:
     * - Elimina todos los productos del usuario
     *
     * @param idUsuario ID del usuario eliminado
     */
    void manejarUsuarioEliminado(String idUsuario);
}
