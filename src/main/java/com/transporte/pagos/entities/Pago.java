package com.transporte.pagos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "pagos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Double pago;
    @NotNull
    private Double cambio;
    private Timestamp creado;
    @OneToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipos_id")
    private Tipo tipo;
}
