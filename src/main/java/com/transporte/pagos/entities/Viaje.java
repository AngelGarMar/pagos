package com.transporte.pagos.entities;

import com.transporte.pagos.enums.PagoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "viajes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer viajeId;
    @NotNull
    private Integer clienteId;
    @NotNull
    private Integer usuarioId;
    @NotNull
    private double costo;
    @Enumerated(EnumType.STRING)
    private PagoStatus estatus;
    private Timestamp creado;
    private Timestamp modificado;
    @OneToOne(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pago pago;
}
