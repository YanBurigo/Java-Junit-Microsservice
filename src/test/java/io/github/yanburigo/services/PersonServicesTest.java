package io.github.yanburigo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.yanburigo.exceptions.ResourceNotFoundException;
import io.github.yanburigo.model.Person;
import io.github.yanburigo.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    @Mock
    private PersonRepository repository;
    
    @InjectMocks
    private PersonServices services;

    private Person person0;
    
    @BeforeEach
    public void setup() {
        // Given / Arrange
    	person0 = new Person(
                "Yan",
                "Burigo",
                "yan@gmail.com",
                "Sao Paulo - Sao Paulo - Brasil",
                "Male");
    }
    
    @DisplayName("JUnit test for Given Person Object when Save Person then Return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
        
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person0)).willReturn(person0);
        
        // When / Act
        Person savedPerson = services.create(person0);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Yan", savedPerson.getFirstName());
    }   
    
    @DisplayName("JUnit test for Given Existing Email when Save Person then throws Exception")
    @Test
    void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {
        
        // Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));
        
        // When / Act
        assertThrows(ResourceNotFoundException.class, () -> {
            services.create(person0);
        });
        
        // Then / Assert
        verify(repository, never()).save(any(Person.class));
    }   
    
    @DisplayName("JUnit test for Given Persons List when findAll Persons then Return Persons List")
    @Test
    void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList() {
        
        // Given / Arrange
        Person person1 = new Person("Yan2",
                "Burigo",
                "yan2@gmail.com",
                "Sao Paulo - Sao Paulo - Brasil",
                "Male");
        
        given(repository.findAll()).willReturn(List.of(person0, person1));
        
        // When / Act
        List<Person> personsList = services.findAll();
        
        // Then / Assert
        assertNotNull(personsList);
        assertEquals(2, personsList.size());
    }   
    
    @DisplayName("JUnit test for Given Empty Persons List when findAll Persons then Return Empty Persons List")
    @Test
    void testGivenEmptyPersonsList_WhenFindAllPersons_thenReturnEmptyPersonsList() {
        
        // Given / Arrange
        given(repository.findAll()).willReturn(Collections.emptyList());
        
        // When / Act
        List<Person> personsList = services.findAll();
        
        // Then / Assert
        assertTrue(personsList.isEmpty());
        assertEquals(0, personsList.size());
    }   
    
    @DisplayName("JUnit test for Given PersonId when findById then Return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() {
        
        // Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        
        // When / Act
        Person savedPerson = services.findById(1L);
        
        // Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Yan", savedPerson.getFirstName());
    }  
    
    @DisplayName("JUnit test for Given Person Object when Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatedPersonObject() {
        
        // Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        
        person0.setFirstName("Yan");
        person0.setEmail("yan2@gmail.com");
        
        given(repository.save(person0)).willReturn(person0);
        
        // When / Act
        Person updatedPerson = services.update(person0);
        
        // Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Yan", updatedPerson.getFirstName());
        assertEquals("yan2@gmail.com", updatedPerson.getEmail());
    }  
    
    @DisplayName("JUnit test for Given PersonID when Delete Person then do Nothing")
    @Test
    void testGivenPersonID_WhenDeletePerson_thenDoNothing() {
        
        // Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        willDoNothing().given(repository).delete(person0);
        
        // When / Act
        services.delete(person0.getId());
        
        // Then / Assert
        verify(repository, times(1)).delete(person0);
    }  
}
