package com.upao.adventure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.upao.adventure.entity.Itinerario;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {
    // Métodos de consulta específicos para Itinerario
}