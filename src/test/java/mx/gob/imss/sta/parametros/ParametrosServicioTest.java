package mx.gob.imss.sta.parametros;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
import mx.gob.imss.sta.comun.seguridad.AutorizacionServicio;import mx.gob.imss.sta.comun.auditoria.AuditoriaServicio;import mx.gob.imss.sta.comun.error.ExcepcionApi;
import java.math.BigDecimal;import java.time.LocalDate;
class ParametrosServicioTest {
 private final ParametrosMapper mapper=mock(ParametrosMapper.class);
 private final ParametrosServicio servicio=new ParametrosServicio(mapper,mock(AutorizacionServicio.class),mock(AuditoriaServicio.class));
 private ParametroSolicitud dias(String valor){var d=new ParametroSolicitud();d.tipo="DIAS_HABILES";d.valor=new BigDecimal(valor);d.ejercicio=2028;d.mes=2;d.vigencia=LocalDate.of(2028,2,1);return d;}
 @Test void aceptaLimiteRealDeFebreroBisiesto(){assertDoesNotThrow(()->servicio.validarDatos(dias("29")));verifyNoInteractions(mapper);}
 @Test void rechazaDiasFueraDelMes(){assertThrows(ExcepcionApi.class,()->servicio.validarDatos(dias("30")));}
 @Test void rechazaDiasFraccionariosYCero(){assertThrows(ExcepcionApi.class,()->servicio.validarDatos(dias("20.5")));assertThrows(ExcepcionApi.class,()->servicio.validarDatos(dias("0")));}
 @Test void rechazaVigenciaDeOtroEjercicio(){var d=dias("20");d.vigencia=LocalDate.of(2027,2,1);assertThrows(ExcepcionApi.class,()->servicio.validarDatos(d));}
 @Test void rechazaPorcentajeMayorACien(){var d=dias("100.000001");d.tipo="LIMITE_PORCENTUAL";d.mes=0;assertThrows(ExcepcionApi.class,()->servicio.validarDatos(d));}
 @Test void solicitudAdmiteUnidadOriginalAlfanumerica(){try(var fabrica=jakarta.validation.Validation.buildDefaultValidatorFactory()){var v=fabrica.getValidator();var d=dias("20");d.motivo="Prueba RN";d.cveUnidad="01CP01";assertTrue(v.validate(d).isEmpty());d.cveUnidad="01-P01";assertFalse(v.validate(d).isEmpty());}}
}
