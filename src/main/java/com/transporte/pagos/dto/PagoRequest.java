package com.transporte.pagos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class PagoRequest implements Serializable {
    @NotNull(message = "El campo idViaje no debe estar vacío.")
    private Integer idViaje;
    @NotNull(message = "El campo pago no debe estar vacío.")
    @Min(value = 1, message = "El valor debe ser mayor a cero.")
    private Double pago;
    @NotNull(message = "El campo idTipoPago no debe estar vacío.")
    private Integer idTipoPago;
}
