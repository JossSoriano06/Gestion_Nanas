package com.Nanas.demo.dominio.puertos.salidas;

import java.util.Optional;

import com.Nanas.demo.dominio.modelos.Cliente;
import com.Nanas.demo.dominio.modelos.Nana;
import com.Nanas.demo.dominio.modelos.Usuario;

public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorCorreo(String correo);
    boolean existePorCorreo(String correo);
    boolean existePorDni(String dni);
    Cliente guardarCliente(Cliente cliente);
    Nana guardarNana(Nana nana);
    Nana buscarNanaPorId(Integer idNana);

    Usuario guardarUsuarioGenerico (Usuario usuario);

    Optional<Usuario> buscarPorId(Integer idUsuario);

    Cliente actualizarCliente(Integer idUsuario, Cliente cliente);
    
    Integer obtenerIdUsuarioCliente(Integer idCliente);

    Integer obtenerIdUsuarioNana(Integer idNana);
}
