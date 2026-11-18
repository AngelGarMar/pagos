package com.transporte.pagos.dto;

import com.transporte.pagos.entities.Pago;
import com.transporte.pagos.entities.Viaje;
import com.transporte.pagos.enums.PagoStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class PagoResponse implements Serializable {
    private Integer id;
    private PagoStatus estatus;
    private String creado;

    public PagoResponse(Viaje viaje, Pago pago) {
        this.id = viaje.getId();
        this.estatus = viaje.getEstatus();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(pago.getCreado().getTime()), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.creado = dateTime.format(formatter);
    }
}
