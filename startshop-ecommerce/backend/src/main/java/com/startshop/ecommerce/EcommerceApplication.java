package com.startshop.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal. El metodo main() es la puerta de entrada de CUALQUIER
 * programa Java. @SpringBootApplication le dice a Spring: "escanea este
 * paquete y todos sus subpaquetes, arma el contexto de la aplicacion,
 * configura el servidor web embebido, etc."
 */
@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
