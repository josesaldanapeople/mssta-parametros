package mx.gob.imss.sta.parametros;
import jakarta.validation.constraints.*;import java.time.LocalDate;import java.math.BigDecimal;
public class ParametroSolicitud {
 @NotBlank @Pattern(regexp="LIMITE_PORCENTUAL|META_ANUAL_DIAS|DIAS_HABILES") public String tipo;
 @NotNull @DecimalMin("0") @Digits(integer=12,fraction=6) public BigDecimal valor;
 @Min(1900) @Max(9999) public int ejercicio;
 @Min(0) @Max(12) public int mes;
 @Size(max=2) public String cveOoad;
 @Pattern(regexp="[A-Za-z0-9]{6}") public String cveUnidad;
 @Size(max=20) public String regimen,cveIncidencia;
 @NotNull public LocalDate vigencia;
 @NotBlank @Size(max=2000) public String motivo;
 public Integer versionEsperada;
}
