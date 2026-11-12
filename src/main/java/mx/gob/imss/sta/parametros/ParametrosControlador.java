package mx.gob.imss.sta.parametros;
import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import org.springframework.http.HttpStatus;import java.util.*;
@RestController @RequestMapping("/api/v1/parametros")public class ParametrosControlador {
 private final ParametrosServicio servicio;public ParametrosControlador(ParametrosServicio servicio){this.servicio=servicio;}
 @GetMapping public List<ParametroVista> listar(@RequestParam(required=false)Integer ejercicio,@RequestParam(required=false)String cveOoad,@RequestParam(required=false)String cveRegimen){return servicio.listar(ejercicio,cveOoad,cveRegimen);}
 @GetMapping("/{id}/historial")public List<Map<String,Object>> historial(@PathVariable long id){return servicio.historial(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED)public ParametroVista crear(@Valid @RequestBody ParametroSolicitud d){return servicio.guardar(null,d);}
 @PostMapping("/{id}/versiones")@ResponseStatus(HttpStatus.CREATED)public ParametroVista version(@PathVariable long id,@Valid @RequestBody ParametroSolicitud d){return servicio.guardar(id,d);}
 @PostMapping("/{id}/validacion")public ParametroVista validar(@PathVariable long id){return servicio.validar(id);}
}
