package com.Nanas.demo.aplicacion.servicios;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Nanas.demo.aplicacion.puertos.entradas.NanaService;
import com.Nanas.demo.dominio.modelos.Nana;
import com.Nanas.demo.dominio.puertos.salidas.NanaRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.UsuarioRepositoryPort;

@Service
public class NanaServiceImpl implements NanaService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final NanaRepositoryPort nanaRepositoryPort;

    public NanaServiceImpl(UsuarioRepositoryPort usuarioRepositoryPort, NanaRepositoryPort nanaRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.nanaRepositoryPort = nanaRepositoryPort;
    }

    @Override
    public Nana registrarNana(Nana nana) {
        if (usuarioRepositoryPort.existePorCorreo(nana.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (usuarioRepositoryPort.existePorDni(nana.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }
        if (nana.getCodigoUniversitario() == null || nana.getCodigoUniversitario().isEmpty()) {
            throw new IllegalArgumentException("El código universitario es obligatorio");
        }
        // Validar que la tarifa por hora sea mayor a cero
        if (nana.getTarifaHora() == null || nana.getTarifaHora().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La tarifa por hora debe ser mayor a cero");
        }
        // Asignación de estados
        nana.setTipoUsuario("NANA");
        if (nana.getDisponibilidad() == null) {
            nana.setDisponibilidad("DISPONIBLE"); // por defecto, la nana está disponible
        }

        nana.setVerificado(false); // Requiere aprobación administrativa posterior
        nana.setRatingPromedio(BigDecimal.ZERO);
        nana.setCantidadReviews(0);
        return nanaRepositoryPort.guardarNana(nana);

    }

    @Override
    public List<Nana> listarNanasDisponibles() {
        List<Nana> todasLasNanas = nanaRepositoryPort.obtenerTodas();

        // solo disponibles
        return todasLasNanas.stream()
                .filter(nana -> "DISPONIBLE".equals(nana.getDisponibilidad()))
                .collect(Collectors.toList());
    }

    @Override
    public Nana obtenerNanaPorId(Integer idNana) {

        return nanaRepositoryPort.buscarNanaPorId(idNana);

    }

}
