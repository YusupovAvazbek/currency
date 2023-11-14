package fido.currency.repository;

import fido.currency.model.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    List<Currency> findAllByOrderByDateDesc(Pageable pageable);
}
