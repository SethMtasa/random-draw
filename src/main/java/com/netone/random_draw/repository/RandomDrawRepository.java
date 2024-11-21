package com.netone.random_draw.repository;

import com.netone.random_draw.model.RandomDraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RandomDrawRepository extends JpaRepository<RandomDraw, Long> {
}