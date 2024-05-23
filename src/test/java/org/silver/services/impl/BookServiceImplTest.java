package org.silver.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.silver.exceptions.ResourceNotFound;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.dtos.books.BookSaveDTO;
import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.silver.repositories.IBooksRepository;
import org.silver.services.IAuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private IBooksRepository booksRepository;
    @Mock
    private IAuthorService authorService;
    @Mock
    private BookMapper bookMapper;

    // Entidades
    private BookEntity book;
    private AuthorEntity author;

    // DTOS
    private BookSaveDTO bookSaveDTO;
    private BookResponseFullDTO bookFullDTO;
    private BookResponseSimpleDTO bookSimpleDTO;


    @BeforeEach
    void setUp() {
        author = new AuthorEntity();
        author.setName("Isabel Allende");
        book = BookEntity.builder()
                .id(1L)
                .title("La casa de los espíritus")
                .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                .author(author)
                .isbn("9780553383804")
                .publishedDate(LocalDate.of(1982, 4, 12))
                .active(true)
                .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                .build();

        bookFullDTO = bookMapper.toDtoFull(book);
        bookSimpleDTO = bookMapper.toDtoSimple(book);

        bookSaveDTO = BookSaveDTO.builder()
                .title("La casa de los espíritus")
                .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                .authorName("Isabel Allende")
                .isbn("9780553383804")
                .publishedDate(LocalDate.of(1982, 4, 12))
                .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                .build();
    }

    @Test
    void findByDynamicQuery() {
    }

    @Test
    void findById_BookFound() {
        when(booksRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        BookResponseFullDTO bookDTO = bookService.findById(book.getId());

        assertNotNull(bookDTO);
        assertEquals(book.getTitle(), bookDTO.title());
        assertEquals(book.getAuthor(), bookDTO.author());

        verify(booksRepository, times(1)).findById(any());
    }

    @Test
    void findById_BookNotFound() {
        when(booksRepository.findById(any()))
                .thenThrow(ResourceNotFound.class);

        assertThrows(ResourceNotFound.class, () -> bookService.findById(any()));

        verify(booksRepository, times(1)).findById(any());
    }

    @Test
    void findAllActive() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<BookEntity> booksPage = new PageImpl<>(List.of(book));
        Page<BookResponseFullDTO> booksDTO = new PageImpl<>(List.of(bookFullDTO));

        when(booksRepository.findAllByActiveIsTrue(pageable)).thenReturn(booksPage);

        Page<BookResponseFullDTO> result = bookService.findAllActive(pageable);

        // Verificaciones de paginación
        assertNotNull(result);
        assertEquals(booksPage.getTotalElements(), result.getTotalElements());
        assertEquals(booksPage.getTotalPages(), result.getTotalPages());
        // Verificaciones de DTO
        assertEquals(bookFullDTO.id(), booksDTO.getContent().get(0).id());
        assertEquals(bookFullDTO.title(), booksDTO.getContent().get(0).title());
        assertEquals(bookFullDTO.author(), booksDTO.getContent().get(0).author());

        verify(booksRepository, times(1)).findAllByActiveIsTrue(pageable);
    }

    @Test
    void findByAuthor() {
        String authorName = author.getName();
        Pageable pageable = PageRequest.of(0, 5);
        Page<BookEntity> booksPage = new PageImpl<>(List.of(book));

        when(booksRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable)).thenReturn(booksPage);

        Page<BookResponseFullDTO> result = bookService.findByAuthor(authorName, pageable);

        // Verificaciones de paginación
        assertNotNull(result);
        assertEquals(booksPage.getTotalElements(), result.getTotalElements());
        assertEquals(booksPage.getTotalPages(), result.getTotalPages());
        // Verificaciones de DTO
        assertEquals(bookFullDTO.id(), result.getContent().get(0).id());
        assertEquals(bookFullDTO.title(), result.getContent().get(0).title());
        assertEquals(bookFullDTO.author(), result.getContent().get(0).author());

        verify(booksRepository, times(1)).findByAuthorNameContainingIgnoreCase(authorName, pageable);
    }

    @Test
    void findByKeyword() {
        String keyword = "Test";
        Pageable pageable = PageRequest.of(0, 5);
        Page<BookEntity> booksPage = new PageImpl<>(List.of(book));

        when(booksRepository.findByTitleOrAuthorName(keyword, pageable)).thenReturn(booksPage);

        Page<BookResponseFullDTO> result = bookService.findByKeyword(keyword, pageable);

        // Verificaciones de paginación
        assertNotNull(result);
        assertEquals(booksPage.getTotalElements(), result.getTotalElements());
        assertEquals(booksPage.getTotalPages(), result.getTotalPages());

        // Verificaciones de DTO
        assertEquals(bookFullDTO.id(), result.getContent().get(0).id());
        assertEquals(bookFullDTO.title(), result.getContent().get(0).title());
        assertEquals(bookFullDTO.author(), result.getContent().get(0).author());

        verify(booksRepository, times(1)).findByTitleOrAuthorName(keyword, pageable);
    }

    @Test
    void save_Success() {
        when(authorService.getOrSave(bookSaveDTO.authorName())).thenReturn(author);
        when(bookMapper.toEntity(bookSaveDTO)).thenReturn(book);
        when(booksRepository.save(book)).thenReturn(book);
        when(bookMapper.toDtoSimple(book)).thenReturn(bookSimpleDTO);

        BookResponseSimpleDTO result = bookService.save(bookSaveDTO);

        assertNotNull(result);
        assertEquals(bookSimpleDTO.id(), result.id());
        assertEquals(bookSimpleDTO.title(), result.title());
        assertEquals(bookSimpleDTO.author(), result.author());

        verify(authorService, times(1)).getOrSave(bookSaveDTO.authorName());
        verify(booksRepository, times(1)).save(book);
    }

    @Test
    void update() {
    }

    @Test
    void changeStatus() {
    }
}