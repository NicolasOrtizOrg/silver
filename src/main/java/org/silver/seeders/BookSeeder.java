package org.silver.seeders;

import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.silver.models.entities.CategoryEntity;
import org.silver.repositories.IBooksRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookSeeder {

    private final IBooksRepository booksRepository;

    public BookSeeder(IBooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public void loadData(){
        if (booksRepository.count() == 0){
            BookEntity bookEntity = BookEntity.builder()
                    .title("titulo")
                    .description("lorem lorem")
                    .isbn("asfbbsa24")
                    .publishedDate(LocalDate.now())
                    .isActive(true)
                    .category(new CategoryEntity(1L, ""))
                    .author(new AuthorEntity(1L, ""))
                    .build();
            booksRepository.save(bookEntity);
        }
    }

}
