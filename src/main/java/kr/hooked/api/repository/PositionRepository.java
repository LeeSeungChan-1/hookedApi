package kr.hooked.api.repository;

import kr.hooked.api.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Page<Position> findAll(Pageable pageable);

    boolean existsByNumber(String number);

    boolean existsByName(String name);
}
