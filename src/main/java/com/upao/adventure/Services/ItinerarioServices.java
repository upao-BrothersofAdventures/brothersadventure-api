package com.upao.adventure.Services;

import org.springframework.stereotype.Service;
import com.upao.adventure.dto.PublicacionDTO;
import com.upao.adventure.entity.*;
import com.upao.adventure.repository.ItinerarioRepository;
import com.upao.adventure.repository.LugarRepository;
import com.upao.adventure.repository.PaisRepository;
import com.upao.adventure.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItinerarioServices {

    private final ItinerarioRepository itinerarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final LugarRepository lugarRepository;

    private final PaisRepository paisRepository;

    public ItinerarioServices(ItinerarioRepository itinerarioRepository, UsuarioRepository usuarioRepository, LugarRepository lugarRepository, PaisRepository paisRepository){
        this.itinerarioRepository = itinerarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.lugarRepository = lugarRepository;
        this.paisRepository = paisRepository;
    }

    public Itinerario crearDesdeDTO(PublicacionDTO publicacionDTO){
        Itinerario itinerario = new Itinerario();
        itinerario.setTitulo(publicacionDTO.getTitulo());
        itinerario.setDescripcion(publicacionDTO.getDescripcion());
        itinerario.setTipo(publicacionDTO.getTipo());

        Usuario usuario = usuarioRepository.findById(publicacionDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        itinerario.setUsuario(usuario);

        Lugar lugar = lugarRepository.findByNombre(publicacionDTO.getLugar())
                .orElseGet(() -> {
                    Lugar nuevoLugar = new Lugar();
                    nuevoLugar.setNombre(publicacionDTO.getLugar());
                    return lugarRepository.save(nuevoLugar);
                });
        itinerario.setLugar(lugar);

        Pais pais = paisRepository.findByNombreIgnoreCase(publicacionDTO.getNombrePais())
                .orElseThrow(() -> new RuntimeException("Pais no encontrado"));
        itinerario.setPais(pais);

        itinerario.setDuracion(publicacionDTO.getDuracion());
        itinerario.setFechainicio(publicacionDTO.getFechaInicio());
        itinerario.setFechaFin(publicacionDTO.getFechaFin());

        List<Actividad> actividades = publicacionDTO.getActividades().stream()
                .map(descripcion -> new Actividad(descripcion, itinerario))
                .collect(Collectors.toList());
        itinerario.setActividades(actividades);
        itinerario.setFecha(LocalDate.from(LocalDateTime.now()));
        itinerario.setHora(LocalTime.from(LocalDateTime.now()));

        return itinerarioRepository.save(itinerario);
    }
}
