package com.transporte.pagos.dto;

import com.transporte.pagos.enums.PagoStatus;
import lombok.Data;

@Data
public class ViajeResponse {
    private Integer id;
    private PagoStatus estatus;
    private String creado;
}
