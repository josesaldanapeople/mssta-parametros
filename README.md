# mssta-parametros

Java 21 / Spring Boot 3.5.0 / MyBatis, conectado al esquema existente de stasdds.
Puerto 8085, contexto /mssta-parametros. No crea ni modifica estructura de base de datos.
Configurar STA_DB_URL, STA_DB_USUARIO, STA_DB_CONTRASENA y STA_JWT_SECRETO.
Las escrituras requieren STA_ESCRITURA_HABILITADA=true y autorización funcional vigente.
La auditoría se registra en la transacción de negocio. No contiene datos simulados ni credenciales.
