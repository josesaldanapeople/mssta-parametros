package mx.gob.imss.sta.parametros;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.apache.ibatis.annotations.Mapper;
@SpringBootApplication(scanBasePackages="mx.gob.imss.sta",exclude=UserDetailsServiceAutoConfiguration.class)
@MapperScan(basePackages="mx.gob.imss.sta",annotationClass=Mapper.class)
public class ParametrosAplicacion { public static void main(String[] args) {SpringApplication.run(ParametrosAplicacion.class,args);} }
