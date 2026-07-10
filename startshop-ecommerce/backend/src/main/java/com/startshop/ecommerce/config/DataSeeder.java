package com.startshop.ecommerce.config;

import com.startshop.ecommerce.entity.Product;
import com.startshop.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * CommandLineRunner: Spring ejecuta el metodo run() UNA vez, justo al arrancar la app.
 * Lo usamos para tener productos de ejemplo desde el primer momento, sin tener
 * que insertarlos manualmente en la base de datos.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return; // Ya hay productos, no duplicamos
        }

        productRepository.save(new Product(
                "Auriculares Bluetooth",
                "Auriculares inalambricos con cancelacion de ruido y 20 horas de bateria.",
                new BigDecimal("39.90"), 25,
                "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=400",
                "Accesorios"
        ));
        productRepository.save(new Product(
                "Reloj inteligente",
                "Monitorea tu ritmo cardiaco, pasos y notificaciones del celular.",
                new BigDecimal("59.90"), 15,
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400",
                "Accesorios"
        ));
        productRepository.save(new Product(
                "Set de skincare facial",
                "Rutina completa de limpieza e hidratacion para todo tipo de piel.",
                new BigDecimal("24.50"), 40,
                "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                "Cuidado Personal"
        ));
        productRepository.save(new Product(
                "Organizador modular de escritorio",
                "Compartimentos apilables para mantener tu espacio de trabajo ordenado.",
                new BigDecimal("18.00"), 30,
                "https://images.unsplash.com/photo-1587145820266-a5951ee6f620?w=400",
                "Hogar"
        ));
        productRepository.save(new Product(
                "Lampara LED de escritorio",
                "Luz regulable en 3 tonalidades, ideal para estudiar o trabajar.",
                new BigDecimal("22.90"), 20,
                "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=400",
                "Hogar"
        ));
        productRepository.save(new Product(
                "Mochila anti-robo",
                "Con puerto USB integrado y compartimento acolchado para laptop.",
                new BigDecimal("45.00"), 18,
                "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400",
                "Accesorios"
        ));
        productRepository.save(new Product(
                "Botella termica 750ml",
                "Mantiene bebidas frias 24h o calientes 12h. Acero inoxidable.",
                new BigDecimal("15.90"), 50,
                "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400",
                "Hogar"
        ));
        productRepository.save(new Product(
                "Kit de bandas de resistencia",
                "5 niveles de intensidad para entrenar en casa.",
                new BigDecimal("19.90"), 35,
                "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400",
                "Deporte"
        ));

        System.out.println("Productos de ejemplo insertados correctamente.");
    }
}
