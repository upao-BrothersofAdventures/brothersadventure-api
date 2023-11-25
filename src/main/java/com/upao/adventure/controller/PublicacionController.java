package com.upao.adventure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upao.adventure.dto.PublicacionDTO;
import com.upao.adventure.entity.Itinerario;
import com.upao.adventure.entity.Publicacion;
import com.upao.adventure.Services.ItinerarioServices;
import com.upao.adventure.Services.PublicacionServices;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")

public class PublicacionController {

    @Autowired
    private ItinerarioServices itinerarioService;
    @Autowired
    private PublicacionServices publicacionServices;

    @PostMapping("/crearPublicacion")
    public ResponseEntity<?> crearPublicacion(@RequestBody PublicacionDTO publicacionDTO) {
        switch (publicacionDTO.getTipo().toLowerCase()) {
            case "itinerario":
                Itinerario itinerario = itinerarioService.crearDesdeDTO(publicacionDTO);
                return ResponseEntity.ok("Publicacion creada");
            default:
                return new ResponseEntity<>("Tipo de publicación desconocido", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<?>> obtenerPublicaciones(@RequestParam(name = "tipo", defaultValue = "todas") String tipo) {
        List<?> publicaciones = publicacionServices.obtenerPublicaciones(tipo);
        return ResponseEntity.ok(publicaciones);
    }
}