package mx.gob.imss.sta.parametros;
import java.math.BigDecimal;import java.time.LocalDate;import java.time.OffsetDateTime;
public class ParametroVista {
 public String id,tipo,cveOoad,cveUnidad,regimen,cveIncidencia,medida,motivo,validador;
 public BigDecimal valor;public int ejercicio,mes,version;
 public LocalDate vigencia,fin;public OffsetDateTime validacion;
}
