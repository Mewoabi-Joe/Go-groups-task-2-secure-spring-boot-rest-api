package com.example.test1.repositories;

import com.example.test1.models.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ListRepository extends JpaRepository<List, Long> {
}
