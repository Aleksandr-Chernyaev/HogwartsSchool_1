package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacultyControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty createdFaculty;

    private static final String BASE_URL = "/faculty";

    @BeforeEach
    public void setUp() {
        // Создаем факультет для использования в тестах
        createdFaculty = restTemplate.postForObject(BASE_URL + "?name=Кря&color=Красный", null, Faculty.class);
    }

    @Test
    public void testCreateFaculty() {
        ResponseEntity<Faculty> response = restTemplate.postForEntity(BASE_URL + "?name=Спорт&color=Синий", null, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Спорт");
        assertThat(response.getBody().getColor()).isEqualTo("Синий");
    }

    @Test
    public void testGetAllFaculties() {
        ResponseEntity<List> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, List.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testGetFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(BASE_URL + "/" + createdFaculty.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdFaculty.getId());
    }

    @Test
    public void testUpdateFaculty() {
        Long facultyId = createdFaculty.getId();
        HttpEntity<Faculty> request = new HttpEntity<>(new Faculty("Обновил", "Малиновый"));

        ResponseEntity<Faculty> response = restTemplate.exchange(BASE_URL + "/" + facultyId + "?name=Обновил&color=Малиновый", HttpMethod.PUT, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Обновил");
    }

    @Test
    public void testDeleteNonExistentFaculty() {
        Long nonExistentId = 999L;

        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URL + "/" + nonExistentId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFilterByColor() {
        ResponseEntity<List> response = restTemplate.exchange(BASE_URL + "/filter/color/Green", HttpMethod.GET, null, List.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}