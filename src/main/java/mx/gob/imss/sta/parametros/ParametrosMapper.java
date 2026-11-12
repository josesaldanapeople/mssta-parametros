package mx.gob.imss.sta.parametros;
import org.apache.ibatis.annotations.*;import java.util.*;import java.time.LocalDate;
@Mapper public interface ParametrosMapper {
 String V="SELECT id_parametro::text id,ref_parametro tipo,num_ejercicio ejercicio,num_mes mes,cve_ooad,cve_unidad,cve_regimen regimen,cve_incidencia,num_valor valor,tip_medida medida,num_version version,fec_inicio vigencia,fec_fin fin,ref_motivo motivo,cve_validador validador,stp_validacion validacion FROM catalogo.stat_parametro ";
 String GRUPO="ref_parametro=#{d.tipo} AND num_ejercicio=#{d.ejercicio} AND num_mes=#{d.mes} AND cve_ooad IS NOT DISTINCT FROM #{d.cveOoad} AND cve_unidad IS NOT DISTINCT FROM #{d.cveUnidad} AND cve_regimen IS NOT DISTINCT FROM #{d.regimen} AND cve_incidencia IS NOT DISTINCT FROM #{d.cveIncidencia}";
 @Select(V+"WHERE stp_baja IS NULL ORDER BY num_ejercicio DESC,ref_parametro,num_version DESC")List<ParametroVista> listar();
 @Select(V+"WHERE id_parametro=#{id}")ParametroVista obtener(long id);
 @Select(V+"WHERE id_parametro=#{id} FOR UPDATE")ParametroVista bloquear(long id);
 @Select("SELECT 1 FROM pg_advisory_xact_lock(hashtextextended(#{llave},0))")Integer bloquearGrupo(String llave);
 @Select(V+"WHERE "+GRUPO+" ORDER BY num_version DESC LIMIT 1")ParametroVista ultimo(@Param("d")ParametroSolicitud d);
 @Select("SELECT cve_ooad FROM catalogo.stac_unidad WHERE cve_unidad=#{unidad} AND stp_baja IS NULL")String ooadUnidad(String unidad);
 @Update("UPDATE catalogo.stat_parametro SET fec_fin=#{fin},stp_modifica=CURRENT_TIMESTAMP,cve_usuario_modifica=#{actor} WHERE id_parametro=#{id}")int cerrar(@Param("id")long id,@Param("fin")LocalDate fin,@Param("actor")String actor);
 @Select(value="INSERT INTO catalogo.stat_parametro(ref_parametro,num_ejercicio,num_mes,cve_ooad,cve_unidad,cve_regimen,cve_incidencia,num_valor,tip_medida,num_version,fec_inicio,ref_motivo,cve_usuario_alta) VALUES(#{d.tipo},#{d.ejercicio},#{d.mes},#{d.cveOoad},#{d.cveUnidad},#{d.regimen},#{d.cveIncidencia},#{d.valor},#{medida},#{version},#{d.vigencia},#{d.motivo},#{actor}) RETURNING id_parametro",affectData=true)long crear(@Param("d")ParametroSolicitud d,@Param("medida")String medida,@Param("version")int version,@Param("actor")String actor);
 @Update("UPDATE catalogo.stat_parametro SET cve_validador=#{actor},stp_validacion=CURRENT_TIMESTAMP,stp_modifica=CURRENT_TIMESTAMP,cve_usuario_modifica=#{actor} WHERE id_parametro=#{id}")int validar(@Param("id")long id,@Param("actor")String actor);
 @Select("SELECT e.id_evento::text id,e.stp_evento fecha,e.cve_usuario actor,e.ref_cambio motivo,e.ref_anterior anterior,e.ref_posterior posterior FROM auditoria.stat_evento e WHERE e.ref_objeto='catalogo.stat_parametro' AND e.ref_registro IN (SELECT id_parametro::text FROM catalogo.stat_parametro WHERE "+GRUPO+") ORDER BY e.stp_evento DESC,e.id_evento DESC")List<Map<String,Object>> historial(@Param("d")ParametroSolicitud d);
}
