package com.ericarodrigs.sacola.repository;

import com.ericarodrigs.sacola.model.Restaurante;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository <Restaurante, Long> {
}
