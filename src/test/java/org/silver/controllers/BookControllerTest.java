package org.silver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.silver.integration.BookSearchFacade;
import org.silver.mappers.BookMapper;
import org.silver.models.dtos.books.BookResponseFullDTO;
import org.silver.models.entities.AuthorEntity;
import org.silver.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private IBookService bookService;

    @MockBean
    private BookSearchFacade bookSearchFacade;

    @MockBean
    private BookMapper bookMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String API_PATH = "http://localhost:8080/books";


    // Entidades
    private AuthorEntity author;

    // DTOS
    private BookResponseFullDTO bookFullDTO;


    @BeforeEach
    void setUp() {
        author = new AuthorEntity();
        author.setName("Isabel Allende");
        bookFullDTO = BookResponseFullDTO.builder()
                .id(1L)
                .title("La casa de los espíritus")
                .description("La saga de la familia Trueba a lo largo de varias generaciones.")
                .author(author)
                .isbn("9780553383804")
                .publishedDate(LocalDate.of(1982, 4, 12))
                .image("https://www.tematika.com/media/catalog/Ilhsa/Imagenes/706758.jpg")
                .build();
    }

    @Test
    void getAllActive() throws Exception {
        List<BookResponseFullDTO> mockList = List.of(bookFullDTO);
        Page<BookResponseFullDTO> mockPage = new PageImpl<>(mockList);

        when(bookService.findAllActive(any())).thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get(API_PATH)
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(bookFullDTO.title()));
    }

    @Test
    void getByBookId() {
    }

    @Test
    void getByKeyword() {
    }

    @Test
    void getByAuthorName() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }
}