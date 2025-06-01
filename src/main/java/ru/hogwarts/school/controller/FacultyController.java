package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestParam String name, @RequestParam String color) {
        Faculty createdFaculty = facultyService.createFaculty(name, color);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaculty);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.getAllFaculties();

        if (faculties == null || faculties.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestParam String name, @RequestParam String color) {
        return facultyService.updateFaculty(id, name, color);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.noContent().build();
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/filter/color/{color}")
    public List<Faculty> filterByColor(@PathVariable String color) {
        return facultyService.filterByColor(color);
    }

    @GetMapping("/faculties/longest-name")
    public String getLongestFacultyName() {
        return facultyService.getAllFaculties().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }
}