package com.projeton1.repository;

import com.projeton1.model.Ferramenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FerramentaRepository extends JpaRepository<Ferramenta, Long> {
    List<Ferramenta> findByAtivaTrue();
}
