package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(String name, String color) {
        logger.info("Вызван метод для создания факультета");
        Faculty faculty = new Faculty(name, color);
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Вызван метод для получения факультета с id={}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Факультет с id=" + id + " не найден"));
    }

    public List<Faculty> getAllFaculties() {
        logger.info("Вызван метод для получения всех факультетов");
        return (List<Faculty>) facultyRepository.findAll();
    }

    public Faculty updateFaculty(Long id, String name, String color) {
        logger.info("Вызван метод для обновления факультета с id={}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Факультет с id=" + id + " не найден для обновления"));
        faculty.setName(name);
        faculty.setColor(color);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Вызван метод для удаления факультета с id={}", id);
        if (!facultyRepository.existsById(id)) {
            logger.warn("Факультет с id={} не найден для удаления", id);
            return;
        }
        facultyRepository.deleteById(id);
    }

    public List<Faculty> filterByColor(String color) {
        logger.info("Вызван метод для фильтрации факультетов по цвету: {}", color);
        return facultyRepository.findByColorIgnoreCase(color);
    }
}