package com.upao.adventure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.upao.adventure.entity.Pais;
import java.util.Optional;

@Repository
public interface PaisRepository extends JpaRepository<Pais, String> {
    Optional<Pais> findBycodigo(String codigo);
    Optional<Pais> findByNombreIgnoreCase(String nombre);

}