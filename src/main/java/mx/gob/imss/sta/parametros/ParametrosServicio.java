package mx.gob.imss.sta.parametros;
import mx.gob.imss.sta.comun.seguridad.AutorizacionServicio;import mx.gob.imss.sta.comun.auditoria.AuditoriaServicio;import mx.gob.imss.sta.comun.error.ExcepcionApi;
import org.springframework.http.HttpStatus;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.util.*;import java.math.BigDecimal;import java.time.YearMonth;
@Service public class ParametrosServicio {
 private final ParametrosMapper mapper;private final AutorizacionServicio permisos;private final AuditoriaServicio auditoria;
 public ParametrosServicio(ParametrosMapper mapper,AutorizacionServicio permisos,AuditoriaServicio auditoria){this.mapper=mapper;this.permisos=permisos;this.auditoria=auditoria;}
 @Transactional(readOnly=true)public List<ParametroVista> listar(Integer ejercicio,String ooad,String regimen){var a=permisos.alcance("PARAMETROS","CONSULTA");return mapper.listar().stream().filter(p->a.isNacional()||a.getOoads().contains(p.cveOoad)).filter(p->ejercicio==null||p.ejercicio==ejercicio).filter(p->ooad==null||ooad.equals(p.cveOoad)).filter(p->regimen==null||regimen.equals(p.regimen)).toList();}
 @Transactional(readOnly=true)public List<Map<String,Object>> historial(long id){var p=existe(mapper.obtener(id));permisos.exigir("PARAMETROS","CONSULTA",p.cveOoad,null);return mapper.historial(solicitud(p));}
 @Transactional public ParametroVista guardar(Long id,ParametroSolicitud d){
  permisos.exigirEscritura();permisos.alcance("PARAMETROS",id==null?"ALTA":"CAMBIO");normalizar(d);validarDatos(d);permisos.exigir("PARAMETROS",id==null?"ALTA":"CAMBIO",d.cveOoad,null);
  mapper.bloquearGrupo(llave(d));var ultimo=mapper.ultimo(d);var anterior=id==null?null:existe(mapper.bloquear(id));
  if(id==null&&ultimo!=null)throw conflicto("Ya existe el parámetro; cree una nueva versión.");
  if(anterior!=null){
   if(!llave(d).equals(llave(solicitud(anterior))))throw error("IDENTIDAD_INMUTABLE","La versión conserva referencia, ejercicio, mes y ámbito del parámetro.");
   if(ultimo==null||!ultimo.id.equals(anterior.id)||d.versionEsperada==null||d.versionEsperada!=anterior.version)throw conflicto("La versión cambió; recargue el parámetro.");
   if(!d.vigencia.isAfter(anterior.vigencia))throw conflicto("La nueva vigencia debe ser posterior. Las correcciones retroactivas requieren un flujo autorizado.");
   mapper.cerrar(id,d.vigencia.minusDays(1),permisos.usuarioActual());
  }
  long nuevo=mapper.crear(d,"LIMITE_PORCENTUAL".equals(d.tipo)?"PORCENTAJE":"DIAS",anterior==null?1:anterior.version+1,permisos.usuarioActual());var salida=existe(mapper.obtener(nuevo));
  auditoria.registrarVersionNoSensible("catalogo.stat_parametro",String.valueOf(nuevo),id==null?"ALTA":"CAMBIO",d.motivo,anterior,salida);return salida;
 }
 @Transactional public ParametroVista validar(long id){permisos.exigirEscritura();permisos.alcance("PARAMETROS","REVISION");var p=existe(mapper.bloquear(id));permisos.exigir("PARAMETROS","REVISION",p.cveOoad,null);if(p.validacion!=null)throw conflicto("La versión ya está validada.");mapper.validar(id,permisos.usuarioActual());var salida=existe(mapper.obtener(id));auditoria.registrarVersionNoSensible("catalogo.stat_parametro",String.valueOf(id),"REVISION","Validación funcional del parámetro",p,salida);return salida;}
 private void normalizar(ParametroSolicitud d){d.cveOoad=vacio(d.cveOoad);d.cveUnidad=vacio(d.cveUnidad);d.regimen=vacio(d.regimen);d.cveIncidencia=vacio(d.cveIncidencia);if(d.cveUnidad!=null){String o=mapper.ooadUnidad(d.cveUnidad);if(o==null)throw error("UNIDAD_INVALIDA","La unidad no existe o está inactiva.");if(d.cveOoad!=null&&!d.cveOoad.equals(o))throw error("AMBITO_INVALIDO","La unidad no pertenece al OOAD indicado.");d.cveOoad=o;}}
 void validarDatos(ParametroSolicitud d){if(d.vigencia.getYear()!=d.ejercicio)throw error("VIGENCIA_INVALIDA","La vigencia debe pertenecer al ejercicio.");if("LIMITE_PORCENTUAL".equals(d.tipo)&&d.valor.compareTo(new BigDecimal("100"))>0)throw error("LIMITE_INVALIDO","El porcentaje no puede superar 100.");if("DIAS_HABILES".equals(d.tipo)){if(d.mes<1||d.mes>12||d.valor.stripTrailingZeros().scale()>0||d.valor.signum()<=0||d.valor.compareTo(BigDecimal.valueOf(YearMonth.of(d.ejercicio,d.mes).lengthOfMonth()))>0)throw error("DIAS_INVALIDOS","Los días hábiles deben ser enteros positivos dentro del mes seleccionado.");}else if(d.mes!=0)throw error("PERIODO_INVALIDO","Metas anuales y límites del ejercicio usan mes cero.");}
 private ParametroSolicitud solicitud(ParametroVista p){var d=new ParametroSolicitud();d.tipo=p.tipo;d.ejercicio=p.ejercicio;d.mes=p.mes;d.cveOoad=p.cveOoad;d.cveUnidad=p.cveUnidad;d.regimen=p.regimen;d.cveIncidencia=p.cveIncidencia;return d;}
 private String llave(ParametroSolicitud d){return "PARAMETRO:"+Arrays.asList(d.tipo,d.ejercicio,d.mes,d.cveOoad,d.cveUnidad,d.regimen,d.cveIncidencia).toString();}
 private String vacio(String x){return x==null||x.isBlank()?null:x.trim();}
 private ParametroVista existe(ParametroVista p){if(p==null)throw new ExcepcionApi(HttpStatus.NOT_FOUND,"PARAMETRO_NO_EXISTE","El parámetro no existe.");return p;}
 private ExcepcionApi error(String c,String m){return new ExcepcionApi(HttpStatus.BAD_REQUEST,c,m);}private ExcepcionApi conflicto(String m){return new ExcepcionApi(HttpStatus.CONFLICT,"CONFLICTO_VERSION",m);}
}
