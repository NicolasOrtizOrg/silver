package org.silver.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Cargar todos los datos al iniciar el proyecto
 * */
@Component
public class DataLoader implements CommandLineRunner {

    private final BookSeeder bookSeeder;
    private final UserSeeder userSeeder;

    public DataLoader(BookSeeder bookSeeder, UserSeeder userSeeder) {
        this.bookSeeder = bookSeeder;
        this.userSeeder = userSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        bookSeeder.loadData();
        userSeeder.loadData();
    }
}
