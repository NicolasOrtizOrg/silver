package org.silver.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.silver.exceptions.GenericException;
import org.silver.exceptions.ResourceDuplicate;
import org.silver.exceptions.ResourceNotFound;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.dtos.books.BookResponseSimpleDTO;
import org.silver.models.dtos.books.BookSaveDTO;
import org.silver.models.entities.AuthorEntity;
import org.silver.models.entities.BookEntity;
import org.silver.repositories.IBooksRepository;
import org.silver.services.IAuthorService;
import org.springframework.dao.DataIntegrityViolationException;
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

    // ids
    private Long bookId;
    private String authorName;


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

        bookFullDTO = BookResponseFullDTO.builder()
                .id(1L)
                .title("La casa de los espíritus")
                .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                .author(author)
                .isbn("9780553383804")
                .publishedDate(LocalDate.of(1982, 4, 12))
                .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                .build();

        bookSimpleDTO = BookResponseSimpleDTO.builder()
                .id(1L)
                .title("La casa de los espíritus")
                .author(author)
                .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                .build();

        bookSaveDTO = BookSaveDTO.builder()
                .title("La casa de los espíritus")
                .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                .authorName("Isabel Allende")
                .isbn("9780553383804")
                .publishedDate(LocalDate.of(1982, 4, 12))
                .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                .build();

        bookId = book.getId();
        authorName = author.getName();
    }

    @Test
    void findByDynamicQuery() {
    }

    @Test
    void findById_BookFound() {
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDtoFull(book)).thenReturn(bookFullDTO);

        BookResponseFullDTO result = bookService.findById(bookId);

        assertNotNull(result);
        assertEquals(bookFullDTO, result);

        verify(booksRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDtoFull(book);
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

        when(booksRepository.findAllByActiveIsTrue(pageable)).thenReturn(booksPage);
        when(bookMapper.toDtoFull(book)).thenReturn(bookFullDTO);

        Page<BookResponseFullDTO> result = bookService.findAllActive(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(bookFullDTO, result.getContent().get(0));

        verify(booksRepository, times(1)).findAllByActiveIsTrue(pageable);
        verify(bookMapper, times(1)).toDtoFull(book);
    }

    @Test
    void findByAuthor() {
        String authorName = "Test";
        Pageable pageable = PageRequest.of(0, 5);
        List<BookResponseFullDTO> dtoList = List.of(bookFullDTO);
        List<BookEntity> books = List.of(book);
        Page<BookEntity> booksPage = new PageImpl<>(books);

        when(booksRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable))
                .thenReturn(booksPage);
        when(bookMapper.toDtoFull(any()))
                .thenReturn(dtoList.get(0), dtoList.get(0));

        Page<BookResponseFullDTO> result = bookService.findByAuthor(authorName, pageable);

        assertEquals(dtoList.size(), result.getTotalElements());
        assertEquals(dtoList.size(), result.getContent().size());

        verify(booksRepository, times(1)).findByAuthorNameContainingIgnoreCase(authorName, pageable);
        verify(bookMapper, times(books.size())).toDtoFull(any());
    }

    @Test
    void findByKeyword() {
        String keyword = "Test";
        Pageable pageable = PageRequest.of(0, 5);
        List<BookResponseFullDTO> bookDTOs = List.of(bookFullDTO);
        List<BookEntity> books = List.of(book);
        Page<BookEntity> booksPage = new PageImpl<>(List.of(book));

        when(booksRepository.findByTitleOrAuthorName(keyword, pageable)).thenReturn(booksPage);
        when(bookMapper.toDtoFull(any())).thenReturn(bookDTOs.get(0));

        Page<BookResponseFullDTO> result = bookService.findByKeyword(keyword, pageable);

        assertEquals(books.size(), result.getTotalElements());
        assertEquals(bookDTOs, result.getContent());

        verify(booksRepository, times(1)).findByTitleOrAuthorName(keyword, pageable);
        verify(bookMapper, times(1)).toDtoFull(any());
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
    void save_DuplicateISBN() {
        when(authorService.getOrSave(bookSaveDTO.authorName()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(ResourceDuplicate.class, () -> bookService.save(bookSaveDTO));

        verify(authorService, times(1)).getOrSave(bookSaveDTO.authorName());
        verifyNoInteractions(bookMapper);
        verifyNoInteractions(booksRepository);
    }

    @Test
    void save_GenericException() {
        when(authorService.getOrSave(bookSaveDTO.authorName()))
                .thenThrow(GenericException.class);

        assertThrows(GenericException.class, () -> bookService.save(bookSaveDTO));

        verify(authorService, times(1)).getOrSave(bookSaveDTO.authorName());
        verifyNoInteractions(bookMapper);
        verifyNoInteractions(booksRepository);
    }

    @Test
    void update() {
        Optional<BookEntity> optionalBook = Optional.of(book);

        when(booksRepository.findById(bookId)).thenReturn(optionalBook);
        when(authorService.getOrSave(author.getName())).thenReturn(author);

        assertDoesNotThrow(() -> bookService.update(bookId, bookSaveDTO));

        verify(booksRepository, times(1)).findById(bookId);
        verify(authorService, times(1)).getOrSave(book.getAuthor().getName());
        verify(booksRepository, times(1)).save(book);
    }

    @Test
    void update_GenericException(){

    }

    @Test
    void changeStatus() {
    }
}