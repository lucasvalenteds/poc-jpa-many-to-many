package com.example;

import com.example.document.Document;
import com.example.document.DocumentRepository;
import com.example.person.Person;
import com.example.person.PersonRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationTest {

    @Container
    private static final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres"));

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
    }

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Order(1)
    void creatingDocument() {
        final var document = new Document();
        document.setCode("XD892342");

        final var documentSaved = documentRepository.save(document);

        assertNotNull(documentSaved.getId());
        assertEquals(document.getCode(), documentSaved.getCode());
    }

    @Test
    @Order(2)
    void creatingPerson() {
        final var person = new Person();
        person.setName("John Smith");

        final var personSaved = personRepository.save(person);

        assertNotNull(person.getId());
        assertEquals(person.getName(), personSaved.getName());
        assertEquals(person.getDocuments(), personSaved.getDocuments());
    }

    @Test
    @Order(3)
    void assigningDocumentsToPerson() {
        // Creating two documents with different codes
        final var document1 = new Document();
        document1.setCode("XYZ00001");

        final var document2 = new Document();
        document2.setCode("XYZ00002");

        final var documents = new HashSet<Document>();
        for (var document : documentRepository.saveAll(Set.of(document1, document2))) {
            documents.add(document);
        }

        // Creating person and assigning to the documents
        final var person = new Person();
        person.setName("John Smith");
        person.setDocuments(documents);

        // Asserting the assignment works
        final var personSaved = personRepository.save(person);

        assertNotNull(personSaved.getId());
        assertThat(personSaved.getDocuments())
                .containsExactlyElementsOf(documents);
    }

    @Test
    @Order(4)
    void assigningPeopleToDocument() {
        // Creating two people without documents
        final var person1 = new Person();
        person1.setName("John Smith");

        final var person2 = new Person();
        person2.setName("Mary Jane");

        final var peopleSaved = new HashSet<Person>();
        for (var person : personRepository.saveAll(Set.of(person1, person2))) {
            peopleSaved.add(person);
        }

        // Creating one document already assigned with two people
        final var document = new Document();
        document.setCode("PAX66712");
        document.setPeople(peopleSaved);

        // Asserting the assignment works
        final var documentSaved = documentRepository.save(document);

        assertNotNull(documentSaved.getId());
        assertEquals(document.getCode(), documentSaved.getCode());
        assertThat(documentSaved.getPeople())
                .containsExactlyElementsOf(peopleSaved);
    }
}
