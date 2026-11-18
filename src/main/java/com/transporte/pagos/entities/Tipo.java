package com.transporte.pagos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tipos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "El campo descripcion no debe estar vacío.")
    @Size(min = 1, max = 45)
    private String descripcion;
    @NotNull
    private Integer estatus;
    private Timestamp creado;
    private Timestamp modificado;
    @OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;
}
