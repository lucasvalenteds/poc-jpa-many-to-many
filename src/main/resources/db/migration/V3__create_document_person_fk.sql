CREATE TABLE PERSON_DOCUMENT
(
    PERSON_ID   BIGINT,
    DOCUMENT_ID BIGINT,

    PRIMARY KEY (PERSON_ID, DOCUMENT_ID),
    FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID),
    FOREIGN KEY (DOCUMENT_ID) REFERENCES DOCUMENT (DOCUMENT_ID)
);