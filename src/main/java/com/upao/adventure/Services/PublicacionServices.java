package com.upao.adventure.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upao.adventure.dto.ItinerarioDTO;
import com.upao.adventure.exception.EntidadNoEncontradaException;
import com.upao.adventure.entity.Itinerario;
import com.upao.adventure.repository.ItinerarioRepository;
import com.upao.adventure.entity.Actividad;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacionServices {

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    public List<Object> obtenerPublicaciones(String tipo) {
        switch (tipo) {
            case "todas":
                List<Object> todas = new ArrayList<>();
                todas.addAll(itinerarioRepository.findAll().stream().map(this::convertirAItinerarioDTO).collect(Collectors.toList()));
                return todas;
            case "itinerarios":
                return itinerarioRepository.findAll().stream().map(this::convertirAItinerarioDTO).collect(Collectors.toList());
            default:
                throw new EntidadNoEncontradaException("Tipo de publicación desconocido");
        }
    }

    private ItinerarioDTO convertirAItinerarioDTO(Itinerario itinerario) {
        ItinerarioDTO dto = new ItinerarioDTO();
        dto.setId(itinerario.getId());
        dto.setTitulo(itinerario.getTitulo());
        dto.setDescripcion(itinerario.getDescripcion());
        dto.setFechaInicio(itinerario.getFechainicio());
        dto.setFechaFin(itinerario.getFechaFin());
        dto.setDuracion(itinerario.getDuracion());
        dto.setLugar(itinerario.getLugar().getNombre());
        dto.setPais(itinerario.getPais().getNombre());
        // Suponiendo que actividades es una colección de entidades Actividad y quieres exponer solo las descripciones
        dto.setActividades(itinerario.getActividades().stream().map(Actividad::getDescripcion).collect(Collectors.toList()));
        return dto;
    }
}
