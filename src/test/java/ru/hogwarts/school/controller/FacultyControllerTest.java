package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FacultyControllerTest {

    @InjectMocks
    private FacultyController facultyController;

    @Mock
    private FacultyService facultyService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Красный");
        when(facultyService.createFaculty(any(String.class), any(String.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .param("name", "Гриффиндор")
                        .param("color", "Красный")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));

        verify(facultyService, times(1)).createFaculty("Гриффиндор", "Красный");
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty("Гриффиндор", "Красный"),
                new Faculty("Слизерин", "Зеленый")
        );

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[1].name").value("Слизерин"));

        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Красный");
        when(facultyService.getFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));

        verify(facultyService, times(1)).getFaculty(1L);
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty("Гриффиндор", "Золотой");
        when(facultyService.updateFaculty(anyLong(), any(String.class), any(String.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty/{id}", 1L)
                        .param("name", "Гриффиндор")
                        .param("color", "Золотой")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Золотой"));

        verify(facultyService, times(1)).updateFaculty(1L, "Гриффиндор", "Золотой");
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(anyLong());

        mockMvc.perform(delete("/faculty/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(facultyService, times(1)).deleteFaculty(1L);
    }

    @Test
    public void testDeleteNonExistentFaculty() throws Exception {
        // Передаем сообщение в конструктор исключения
        doThrow(new FacultyNotFoundException("Faculty not found")).when(facultyService).deleteFaculty(anyLong());

        mockMvc.perform(delete("/faculty/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).deleteFaculty(999L);
    }

    @Test
    public void testFilterByColor() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty("Слизерин", "Зеленый"),
                new Faculty("Равенкло", "Синий")
        );

        when(facultyService.filterByColor(any(String.class))).thenReturn(faculties);

        mockMvc.perform(get("/faculty/filter/color/{color}", "Зеленый"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Слизерин"))
                .andExpect(jsonPath("$[0].color").value("Зеленый"));

        verify(facultyService, times(1)).filterByColor("Зеленый");
    }
}