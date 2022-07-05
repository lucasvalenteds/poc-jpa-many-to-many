package com.example.document;

import com.example.person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "DOCUMENT")
@Data
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_ID_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "DOCUMENT_ID_SEQUENCE_GENERATOR", sequenceName = "DOCUMENT_ID_SEQUENCE", allocationSize = 1)
    @Column(name = "DOCUMENT_ID")
    private Long id;

    @Column(name = "DOCUMENT_CODE")
    private String code;

    @ManyToMany(mappedBy = "documents")
    private Set<Person> people;
}
