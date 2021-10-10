package co.kr.talentx.openscout.repository;

import co.kr.talentx.openscout.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}