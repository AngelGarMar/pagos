package com.transporte.pagos.services;

import com.transporte.pagos.dto.PagoRequest;
import com.transporte.pagos.models.ResponseService;

public interface PagoService {
    ResponseService createPago(PagoRequest pago);
}
