package mx.gob.imss.sta.parametros;
import org.junit.jupiter.api.Test;import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.transaction.annotation.Transactional;
@EnabledIfEnvironmentVariable(named="STA_PRUEBAS_LECTURA",matches="true")
@SpringBootTest(properties={"sta.escritura-habilitada=false","sta.jwt.secreto=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="})
@Transactional(readOnly=true)
class ParametrosLecturaTest {
 @Autowired ParametrosMapper mapper;
 @Test void consultaParametrosRealesSinEscritura(){var lista=mapper.listar();assertNotNull(lista);if(!lista.isEmpty())assertNotNull(mapper.obtener(Long.parseLong(lista.getFirst().id)));}
}
