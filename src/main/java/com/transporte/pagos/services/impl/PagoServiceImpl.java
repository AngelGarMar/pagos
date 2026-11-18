package com.transporte.pagos.services.impl;

import com.transporte.pagos.constants.Constants;
import com.transporte.pagos.dto.PagoRequest;
import com.transporte.pagos.dto.PagoResponse;
import com.transporte.pagos.dto.ViajeResponse;
import com.transporte.pagos.entities.Pago;
import com.transporte.pagos.entities.Tipo;
import com.transporte.pagos.entities.Viaje;
import com.transporte.pagos.enums.PagoStatus;
import com.transporte.pagos.models.ResponseService;
import com.transporte.pagos.repositories.PagoRepository;
import com.transporte.pagos.repositories.TipoRepository;
import com.transporte.pagos.repositories.ViajeRepository;
import com.transporte.pagos.services.PagoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class PagoServiceImpl implements PagoService {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private ViajeRepository viajeRepository;
    @Autowired
    private TipoRepository tipoRepository;

    @Value("${app.maintenance.createPago}")
    private boolean createPago;

    @Override
    @Transactional
    public ResponseService createPago(PagoRequest pagoRequest) {
        log.info("se revisa si por configuración se puede crear el pago");
        if (!createPago) {
            log.info("no se puede crear el pago");
            return new ResponseService(Constants.RESPONSE_TYPE_WARNING, Constants.CREATE_PAGO_DISABLED, null);
        }
        log.info("se revisa que exista el id de viaje");
        Optional<Viaje> viajeOptional = viajeRepository.findById(pagoRequest.getIdViaje());
        if (viajeOptional.isEmpty()) {
            log.info("no existe el viaje");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.TRAVEL_UNKNOWN, null);
        }
        Viaje viaje = viajeOptional.get();
        log.info("se revisa que el estatus del viaje sea creado");
        if (!viaje.getEstatus().equals(PagoStatus.CREADO)) {
            log.info("el estatus del viaje no está en {}", viaje.getEstatus());
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.TRAVEL_ESTATUS, null);
        }
        log.info("se revisa que el tipo de pago exista");
        Optional<Tipo> tipoPagoOptional = tipoRepository.findById(pagoRequest.getIdTipoPago());
        if (tipoPagoOptional.isEmpty()) {
            log.info("no existe el tipo de pago");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.PAY_UNKNOWN, null);
        }
        Tipo tipoPago = tipoPagoOptional.get();
        if (tipoPago.getId() == 5) { // 5 : Cancelar el pago
            log.info("se procede a cancelar el pago");
            viaje.setEstatus(PagoStatus.CANCELADO);
            viaje.setModificado(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Mexico_City"))));
            Viaje viajeCancelled = viajeRepository.save(viaje);
            ViajeResponse response = new ViajeResponse();
            response.setId(viajeCancelled.getId());
            response.setEstatus(PagoStatus.CANCELADO);
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(viajeCancelled.getModificado().getTime()), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            response.setCreado(dateTime.format(formatter));
            log.info("se lanza el mensaje de que el viaje se cancelo al microservicio de viajes"); ////////////////////////////////////////////////////////
            log.info("se regresa el viaje cancelado");
            return new ResponseService(Constants.RESPONSE_TYPE_SUCCESS, Constants.TRAVEL_CANCELLED, response);
        }
        log.info("se revisa que el pago a realizar sea mayor o igual al costo del viaje");
        if (pagoRequest.getPago() < viaje.getCosto()) {
            log.info("es insuficiente el pago a realizar");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.PAY_NOT_SUFICIENT, null);
        }
        log.info("se acepta el pago y se crea el registro en la base");
        Pago pago = Pago.builder()
                .pago(pagoRequest.getPago())
                .cambio(pagoRequest.getPago() - viaje.getCosto())
                .creado(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Mexico_City"))))
                .viaje(viaje)
                .tipo(tipoPago)
                .build();
        Pago pagoSaved = pagoRepository.save(pago);
        log.info("se actualiza el estatus del viaje");
        viaje.setEstatus(PagoStatus.PAGO_CONFIRMADO);
        viaje.setModificado(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Mexico_City"))));
        Viaje viajeSaved = viajeRepository.save(viaje);
        PagoResponse response = new PagoResponse(viajeSaved, pagoSaved);
        log.info("se lanza el mensaje de que el pago se hizo al microservicio de viajes"); ////////////////////////////////////////////////////////////////
        log.info("se regresa el viaje con el pago actualizado");
        return new ResponseService(Constants.RESPONSE_TYPE_SUCCESS, Constants.PAY_CREATED, response);
    }
}
