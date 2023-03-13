package com.example.produktapi.repository;

import com.example.produktapi.model.Product;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @Test
    void simpleTest() {
        List<Product> products = underTest.findAll();
        Assertions.assertFalse(products.isEmpty());
    }

    @Test
    void whenSearchingForAnExistingTitle_thenReturnThatProduct() {
        //given
        String title = "En dator";
        Product product = new Product(title,
                25000.0,
                "Elekronik",
                "bra o ha",
                "urlTillBild");
        underTest.save(product);

        // when
        Optional<Product> optionalProduct = underTest.findByTitle(title);

        //then
        // Ett sätt att skriva 3 tester
        assertTrue(optionalProduct.isPresent());
        assertFalse(optionalProduct.isEmpty());
        assertEquals(title, optionalProduct.get().getTitle());


        // Ett annat sätt att skriva samma 3 tester
        Assertions.assertAll(
                () -> assertTrue(optionalProduct.isPresent()),
                () -> assertFalse(optionalProduct.isEmpty()),
                () -> assertEquals(title, optionalProduct.get().getTitle())
        );
    }
    @Test
    void whenSearchingForNonExistingTitle_thenReturnEmptyOptional(){
        // given
        String title = "En titel som absolut inte finns";
        // when
        Optional<Product> optionalProduct = underTest.findByTitle(title);

        // then
        Assertions.assertAll(
                ()-> assertFalse(optionalProduct.isPresent()),
                () -> assertTrue(optionalProduct.isEmpty()),
                () -> assertThrows(NoSuchElementException.class, ()-> optionalProduct.get())
        );
    }

}