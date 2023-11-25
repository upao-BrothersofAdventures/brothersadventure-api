package com.upao.adventure.repository;

import org.springframework.stereotype.Repository;
import com.upao.adventure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findById(Long id);
}
