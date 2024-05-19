package org.silver.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final BookSeeder bookSeeder;

    public DataLoader(BookSeeder bookSeeder) {
        this.bookSeeder = bookSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        bookSeeder.loadData();
    }
}
