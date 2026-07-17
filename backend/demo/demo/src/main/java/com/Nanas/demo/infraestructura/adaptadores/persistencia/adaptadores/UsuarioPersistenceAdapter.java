package com.Nanas.demo.infraestructura.adaptadores.persistencia.adaptadores;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.Nanas.demo.dominio.modelos.Cliente;
import com.Nanas.demo.dominio.modelos.Nana;
import com.Nanas.demo.dominio.modelos.Ubicacion;
import com.Nanas.demo.dominio.modelos.Usuario;
import com.Nanas.demo.dominio.puertos.salidas.NanaRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.UbicacionRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.UsuarioRepositoryPort;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.ClienteEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.DireccionEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.NanaEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.UbicacionEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.UsuarioEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataClienteRepository;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataDireccionRepository;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataNanaRepository;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataUbicacionRepositoty;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataUsuarioRepository;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort, NanaRepositoryPort, UbicacionRepositoryPort {

    private final SpringDataUsuarioRepository usuarioRepository;
    private final SpringDataClienteRepository clienteRepository;
    private final SpringDataNanaRepository nanaRepository;
    private final SpringDataDireccionRepository direccionRepository;
    private final SpringDataUbicacionRepositoty ubicacionRepository;

    
    public UsuarioPersistenceAdapter(SpringDataUsuarioRepository usuarioRepository,
            SpringDataClienteRepository clienteRepository, SpringDataNanaRepository nanaRepository,
            SpringDataDireccionRepository direccionRepository, SpringDataUbicacionRepositoty ubicacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.nanaRepository = nanaRepository;
        this.direccionRepository = direccionRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).map(entity -> {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(entity.getIdUsuario());
            usuario.setNombre(entity.getNombre());
            usuario.setApellido(entity.getApellido());
            usuario.setCorreo(entity.getCorreo());
            usuario.setTelefono(entity.getTelefono());
            usuario.setDni(entity.getDni());
            usuario.setPasswordHash(entity.getPasswordHash());
            usuario.setFechaNacimiento(entity.getFechaNacimiento());
            usuario.setFotoPerfil(entity.getFotoPerfil());
            usuario.setTipoUsuario(entity.getTipoUsuario());
            usuario.setEstadoCuenta(entity.getEstadoCuenta());
            usuario.setFechaRegistro(entity.getFechaRegistro());
            usuario.setUltimoLogin(entity.getUltimoLogin());

            if ("NANA".equals(entity.getTipoUsuario())) {

                nanaRepository.buscarPorIdUsuario(entity.getIdUsuario())
                        .ifPresent(nana -> usuario.setIdNana(nana.getIdNana()));

            }

            if ("CLIENTE".equals(entity.getTipoUsuario())) {

                clienteRepository.findByUsuario_IdUsuario(entity.getIdUsuario())
                        .ifPresent(cliente -> usuario.setIdCliente(cliente.getIdCliente()));

            }
            return usuario;
        });

    }

    @Override
    public boolean existePorCorreo(String correo) {

        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existePorDni(String dni) {

        return usuarioRepository.existsByDni(dni);
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        // Mapear los campos generales para cliente de usuarioEntity
        usuarioEntity.setNombre(cliente.getNombre());
        usuarioEntity.setApellido(cliente.getApellido());
        usuarioEntity.setCorreo(cliente.getCorreo());
        usuarioEntity.setTelefono(cliente.getTelefono());
        usuarioEntity.setDni(cliente.getDni());
        usuarioEntity.setPasswordHash(cliente.getPasswordHash());
        usuarioEntity.setFechaNacimiento(cliente.getFechaNacimiento());
        usuarioEntity.setTipoUsuario(cliente.getTipoUsuario());
        usuarioEntity.setEstadoCuenta(cliente.getEstadoCuenta());
        usuarioEntity.setFechaRegistro(LocalDateTime.now());

        ClienteEntity clienteEntity = new ClienteEntity();
        // Mapear los campos específicos de cliente
        clienteEntity.setUsuario(usuarioEntity);
        clienteEntity.setTipoCliente(cliente.getTipoCliente());

        // Guardar el clienteEntity en la base de datos
        ClienteEntity guardado = clienteRepository.save(clienteEntity);
        cliente.setIdCliente(guardado.getIdCliente());
        cliente.setIdUsuario(guardado.getUsuario().getIdUsuario());
        cliente.setFechaRegistro(guardado.getUsuario().getFechaRegistro());
        cliente.setFechaCreacion(guardado.getFechaCreacion());
        return cliente;

    }

    @Override
    public Nana guardarNana(Nana nana) {

        // ==========================
        // Usuario
        // ==========================

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        usuarioEntity.setNombre(nana.getNombre());
        usuarioEntity.setApellido(nana.getApellido());
        usuarioEntity.setCorreo(nana.getCorreo());
        usuarioEntity.setTelefono(nana.getTelefono());
        usuarioEntity.setDni(nana.getDni());
        usuarioEntity.setPasswordHash(nana.getPasswordHash());
        usuarioEntity.setFechaNacimiento(nana.getFechaNacimiento());

        usuarioEntity.setTipoUsuario("NANA");
        usuarioEntity.setEstadoCuenta("ACTIVA");
        usuarioEntity.setFechaRegistro(LocalDateTime.now());

        // ==========================
        // Nana
        // ==========================

        NanaEntity nanaEntity = new NanaEntity();

        nanaEntity.setUsuario(usuarioEntity);
        nanaEntity.setIdUniversidad(nana.getIdUniversidad());
        nanaEntity.setCodigoUniversitario(nana.getCodigoUniversitario());
        nanaEntity.setCarrera(nana.getCarrera());
        nanaEntity.setCiclo(nana.getCiclo());
        nanaEntity.setDescripcion(nana.getDescripcion());
        nanaEntity.setExperiencia(nana.getExperiencia());
        nanaEntity.setTarifaHora(nana.getTarifaHora());

        nanaEntity.setDisponibilidad("DISPONIBLE");
        nanaEntity.setVerificado(false);
        nanaEntity.setRatingPromedio(BigDecimal.ZERO);
        nanaEntity.setCantidadReviews(0);

        // ==========================
        // Guardar Nana
        // ==========================

        NanaEntity guardada = nanaRepository.save(nanaEntity);

        // ==========================
        // Dirección
        // ==========================

        DireccionEntity direccion = new DireccionEntity();

        direccion.setIdUsuario(guardada.getUsuario().getIdUsuario());

        direccion.setDepartamento(nana.getDepartamento());
        direccion.setProvincia(nana.getProvincia());
        direccion.setDistrito(nana.getDistrito());
        direccion.setDireccion(nana.getDireccion());
        direccion.setReferencia(nana.getReferencia());

        direccion.setLatitud(0.0);
        direccion.setLongitud(0.0);

        direccionRepository.save(direccion);

        // ==========================
        // Retorno
        // ==========================

        nana.setIdNana(guardada.getIdNana());
        nana.setIdUsuario(guardada.getUsuario().getIdUsuario());
        nana.setFechaRegistro(guardada.getUsuario().getFechaRegistro());
        nana.setFechaCreacion(guardada.getFechaCreacion());

        return nana;
    }

    @Override
    public List<Nana> obtenerTodas() {

        // obtiene todas las nanas de la base de datos en formato de persistencia
        List<NanaEntity> nanaEntities = nanaRepository.findAll();
        // prepara el contenedor basio para transformar la entidades a objetos del
        // dominio(objetos de negocio)
        List<Nana> dominioNanas = new ArrayList<>();
        for (NanaEntity entity : nanaEntities) {
            Nana nana = new Nana();
            nana.setIdNana(entity.getIdNana());
            nana.setIdUsuario(entity.getUsuario().getIdUsuario());
            nana.setIdUniversidad(entity.getIdUniversidad());
            nana.setNombre(entity.getUsuario().getNombre());
            nana.setApellido(entity.getUsuario().getApellido());
            nana.setCorreo(entity.getUsuario().getCorreo());
            nana.setTelefono(entity.getUsuario().getTelefono());
            nana.setTarifaHora(entity.getTarifaHora());
            nana.setCarrera(entity.getCarrera());
            nana.setCiclo(entity.getCiclo());

            nana.setDescripcion(entity.getDescripcion());
            nana.setExperiencia(entity.getExperiencia());
            nana.setDisponibilidad(entity.getDisponibilidad());
            nana.setRatingPromedio(entity.getRatingPromedio());
            nana.setCantidadReviews(entity.getCantidadReviews());
            // buscar coordenadas de la nana en la tabla direccion
            // Busca la primera dirección del usuario vinculado a esta entidad.
            Optional<DireccionEntity> dir = direccionRepository.findByIdUsuario(entity.getUsuario().getIdUsuario());
            // si encuentra la direccion, asigna las coordenadas a la nana
            if (dir.isPresent()) {

                nana.setDepartamento(dir.get().getDepartamento());
                nana.setProvincia(dir.get().getProvincia());
                nana.setDistrito(dir.get().getDistrito());
                nana.setDireccion(dir.get().getDireccion());
                nana.setReferencia(dir.get().getReferencia());

                nana.setLatitud(dir.get().getLatitud());
                nana.setLongitud(dir.get().getLongitud());

            } else {

                nana.setLatitud(0.0);
                nana.setLongitud(0.0);

            }
            dominioNanas.add(nana);
        }

        return dominioNanas;
    }

    @Override
    public Nana buscarNanaPorId(Integer idNana) {
        // 1. Buscamos la entidad individual en la base de datos
        Optional<NanaEntity> entityOpt = nanaRepository.findById(idNana);

        if (entityOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró ninguna nana registrada con el ID: " + idNana);
        }

        NanaEntity entity = entityOpt.get();

        Nana nana = new Nana();
        nana.setIdNana(entity.getIdNana());
        nana.setIdUsuario(entity.getUsuario().getIdUsuario());
        nana.setNombre(entity.getUsuario().getNombre());
        nana.setApellido(entity.getUsuario().getApellido());
        nana.setCorreo(entity.getUsuario().getCorreo());
        nana.setTelefono(entity.getUsuario().getTelefono());
        nana.setTarifaHora(entity.getTarifaHora());
        nana.setCarrera(entity.getCarrera());
        nana.setCiclo(entity.getCiclo());

        // Mapeamos también los campos del módulo de calificación
        nana.setRatingPromedio(entity.getRatingPromedio());
        nana.setCantidadReviews(entity.getCantidadReviews());

        // Agregamos los campos de descripcion, experiencia, disponibilidad y verificado
        nana.setDescripcion(entity.getDescripcion());
        nana.setExperiencia(entity.getExperiencia());
        nana.setDisponibilidad(entity.getDisponibilidad());
        nana.setVerificado(entity.getVerificado());

        return nana;
    }

    @Override
    public Usuario guardarUsuarioGenerico(Usuario usuario) {
        // buscamos por el id al usuario
        UsuarioEntity entity = usuarioRepository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el usuario a actualizar con ID: " + usuario.getIdUsuario()));

        // seteamos los campos que cambian al hacer el login
        entity.setNombre(usuario.getNombre());
        entity.setApellido(usuario.getApellido());
        entity.setTelefono(usuario.getTelefono());
        entity.setFotoPerfil(usuario.getFotoPerfil());
        entity.setEstadoCuenta(usuario.getEstadoCuenta());
        entity.setUltimoLogin(usuario.getUltimoLogin());

        // guardamos los datos actualizados cno el repositorio inyectado
        UsuarioEntity guardado = usuarioRepository.save(entity);

        // retornamos los valores para que el dominio los consuma
        Usuario usuarioD = new Usuario();
        usuarioD.setIdUsuario(guardado.getIdUsuario());
        usuarioD.setNombre(guardado.getNombre());
        usuarioD.setApellido(guardado.getApellido());
        usuarioD.setCorreo(guardado.getCorreo());
        usuarioD.setTelefono(guardado.getTelefono());
        usuarioD.setDni(guardado.getDni());
        usuarioD.setPasswordHash(guardado.getPasswordHash());
        usuarioD.setFechaNacimiento(guardado.getFechaNacimiento());
        usuarioD.setFotoPerfil(guardado.getFotoPerfil());
        usuarioD.setTipoUsuario(guardado.getTipoUsuario());
        usuarioD.setEstadoCuenta(guardado.getEstadoCuenta());
        usuarioD.setFechaRegistro(guardado.getFechaRegistro());
        usuarioD.setUltimoLogin(guardado.getUltimoLogin());

        if ("NANA".equals(guardado.getTipoUsuario())) {

            nanaRepository.buscarPorIdUsuario(guardado.getIdUsuario())
                    .ifPresent(nana -> usuarioD.setIdNana(nana.getIdNana()));

        }

        if ("CLIENTE".equals(guardado.getTipoUsuario())) {

            clienteRepository.findByUsuario_IdUsuario(guardado.getIdUsuario())
                    .ifPresent(cliente -> usuarioD.setIdCliente(cliente.getIdCliente()));

        }

        return usuarioD;
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer idUsuario) {

        return usuarioRepository.findById(idUsuario)
                .map(entity -> {

                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(entity.getIdUsuario());
                    usuario.setNombre(entity.getNombre());
                    usuario.setApellido(entity.getApellido());
                    usuario.setCorreo(entity.getCorreo());
                    usuario.setTelefono(entity.getTelefono());
                    usuario.setDni(entity.getDni());
                    usuario.setFechaNacimiento(entity.getFechaNacimiento());
                    usuario.setFotoPerfil(entity.getFotoPerfil());
                    usuario.setTipoUsuario(entity.getTipoUsuario());
                    usuario.setEstadoCuenta(entity.getEstadoCuenta());

                    return usuario;
                });
    }

    @Override
    public Cliente actualizarCliente(Integer idUsuario, Cliente cliente) {

        UsuarioEntity entity = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        entity.setNombre(cliente.getNombre());
        entity.setApellido(cliente.getApellido());
        entity.setTelefono(cliente.getTelefono());
        entity.setFotoPerfil(cliente.getFotoPerfil());

        UsuarioEntity guardado = usuarioRepository.save(entity);

        Cliente actualizado = new Cliente();

        actualizado.setIdUsuario(guardado.getIdUsuario());
        actualizado.setNombre(guardado.getNombre());
        actualizado.setApellido(guardado.getApellido());
        actualizado.setCorreo(guardado.getCorreo());
        actualizado.setTelefono(guardado.getTelefono());
        actualizado.setDni(guardado.getDni());
        actualizado.setFotoPerfil(guardado.getFotoPerfil());
        actualizado.setTipoUsuario(guardado.getTipoUsuario());
        actualizado.setEstadoCuenta(guardado.getEstadoCuenta());

        return actualizado;
    }

    @Override
    public Integer obtenerIdUsuarioCliente(Integer idCliente) {
        ClienteEntity cliente = clienteRepository
            .findById(idCliente)
            .orElseThrow(() ->
                new RuntimeException("Cliente no encontrado."));

    return cliente.getUsuario().getIdUsuario();
    }

    @Override
    public Integer obtenerIdUsuarioNana(Integer idNana) {
        NanaEntity nana = nanaRepository
            .findById(idNana)
            .orElseThrow(() ->
                new RuntimeException("Nana no encontrada."));

    return nana.getUsuario().getIdUsuario();
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        UbicacionEntity entity = new UbicacionEntity();

    entity.setIdUsuario(ubicacion.getIdUsuario());

    entity.setLatitud(ubicacion.getLatitud());

    entity.setLongitud(ubicacion.getLongitud());

    UbicacionEntity guardada = ubicacionRepository.save(entity);

    ubicacion.setIdUbicacion(guardada.getIdUbicacion());

    ubicacion.setFechaRegistro(guardada.getFechaRegistro());

    return ubicacion;
    }

    @Override
    public Ubicacion obtenerUltimaUbicacion(Integer idUsuario) {
        UbicacionEntity entity = ubicacionRepository
            .findFirstByIdUsuarioOrderByFechaRegistroDesc(idUsuario)
            .orElseThrow(() ->
                    new IllegalArgumentException("No existe ubicación para el usuario."));

    Ubicacion ubicacion = new Ubicacion();

    ubicacion.setIdUbicacion(entity.getIdUbicacion());

    ubicacion.setIdUsuario(entity.getIdUsuario());

    ubicacion.setLatitud(entity.getLatitud());

    ubicacion.setLongitud(entity.getLongitud());

    ubicacion.setFechaRegistro(entity.getFechaRegistro());

    return ubicacion;
    }
}
