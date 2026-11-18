package com.transporte.pagos.repositories;

import com.transporte.pagos.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
