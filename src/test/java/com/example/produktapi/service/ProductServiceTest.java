package com.example.produktapi.service;

import com.example.produktapi.exception.BadRequestException;
import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService underTest;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Test
    void whenGetAllProducts_thenExactlyOneInteractionWithRepositoryMethodFindAll() {
        // when
        underTest.getAllProducts();
        //then
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void whenGetAllCategories_thenExactlyOneInteractionWithRepositoryMethodGetByCategory() {
        // when
        underTest.getAllCategories();
        //then
        verify(repository, times(1)).findAllCategories();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled
    void givenAnExistingCategory_whenGetProductsByCategory_thenReceivesANonEmptyList() {
        // Skippar för nu
    }

    @Test
    void whenAddingAProduct_thenSaveMethodShouldBeCalled() {
        // given
        Product product = new Product("Rätt objekt som sparas", 4000.0, "", "", "");
        // when
        underTest.addProduct(product);
        //then
        verify(repository).save(productCaptor.capture());
        assertEquals(product, productCaptor.getValue());
    }

    @Test
    void whenAddingProductWithDuplicateTitle_thenThrowError() {
        // given
        String title = "vår test-titel";
        Product product = new Product(title, 34.0, "", "", "");
        given(repository.findByTitle(title)).willReturn(Optional.of(product));

        //then
        BadRequestException exception = assertThrows(BadRequestException.class,
                //when
                () -> underTest.addProduct(product));
        verify(repository, times(1)).findByTitle(title);
        verify(repository, never()).save(any());
        assertEquals("En produkt med titeln: vår test-titel finns redan", exception.getMessage());
    }
}