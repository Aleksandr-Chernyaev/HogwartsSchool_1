package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long studentId;

    @BeforeEach
    public void setUp() {
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student?name=Женька&age=20", null, Student.class);
        assertEquals(201, response.getStatusCodeValue());
        studentId = response.getBody().getId();
    }

    @Test
    public void testCreateStudent() {
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student?name=Гарри&age=22", null, Student.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Гарри", response.getBody().getName());
        assertEquals(22, response.getBody().getAge());
    }

    @Test
    public void testGetAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student", Student[].class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetStudentById() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + studentId, Student.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
    }

    @Test
    public void testUpdateStudent() {
        String newName = "Гриня";
        int newAge = 21;

        ResponseEntity<Student> response = restTemplate.exchange(
                "/student/" + studentId + "?name=" + newName + "&age=" + newAge,
                HttpMethod.PUT,
                null,
                Student.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(newName, response.getBody().getName());
        assertEquals(newAge, response.getBody().getAge());
    }

    @Test
    public void testDeleteStudent() {
        restTemplate.delete("/student/" + studentId);

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + studentId, Student.class);

        assertEquals(200, response.getStatusCodeValue());
    }
}