package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student("Гарри", 22);
        when(studentService.createStudent(any(String.class), anyInt())).thenReturn(student);

        mockMvc.perform(post("/student")
                        .param("name", "Гарри")
                        .param("age", "22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Гарри"))
                .andExpect(jsonPath("$.age").value(22));

        verify(studentService, times(1)).createStudent("Гарри", 22);
    }

    @Test
    public void testGetAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
                new Student("Гарри", 22),
                new Student("Рон", 21)
        );

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Гарри"))
                .andExpect(jsonPath("$[1].name").value("Рон"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testGetStudent() throws Exception {
        Student student = new Student("Гарри", 22);
        when(studentService.getStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гарри"))
                .andExpect(jsonPath("$.age").value(22));

        verify(studentService, times(1)).getStudent(1L);
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student("Гарри Поттер", 23);
        when(studentService.updateStudent(anyLong(), any(String.class), anyInt())).thenReturn(updatedStudent);

        mockMvc.perform(put("/student/{id}", 1L)
                        .param("name", "Гарри Поттер")
                        .param("age", "23")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(23));

        verify(studentService, times(1)).updateStudent(1L, "Гарри Поттер", 23);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(anyLong());

        mockMvc.perform(delete("/student/{id}", 1L))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}