package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String name, int age) {
        logger.info("Вызван метод для создания студента");
        Student student = new Student(name, age);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Вызван метод для получения студента с id={}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент с id=" + id + " не найден"));
    }

    public List<Student> getAllStudents() {
        logger.info("Вызван метод для получения всех студентов");
        return (List<Student>) studentRepository.findAll();
    }

    public Student updateStudent(Long id, String name, int age) {
        logger.info("Вызван метод для обновления студента с id={}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент с id=" + id + " не найден для обновления"));
        student.setName(name);
        student.setAge(age);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Вызван метод для удаления студента с id={}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Студент с id={} не найден для удаления", id);
            return;
        }
        studentRepository.deleteById(id);
    }

    public long getCount() {
        logger.info("Вызван метод для получения количества студентов");
        return studentRepository.count();
    }

    public double getAverageAge() {
        logger.info("Вызван метод для получения среднего возраста студентов");
        List<Student> students = (List<Student>) studentRepository.findAll();
        if (students.isEmpty()) {
            return 0.0;
        }
        double totalAge = students.stream().mapToInt(Student::getAge).sum();
        return totalAge / students.size();
    }

    public List<Student> getLatestStudents() {
        logger.info("Вызван метод для получения последних добавленных студентов");

        List<Student> allStudents = (List<Student>) studentRepository.findAll();

        return allStudents.stream()
                .sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId()))
                .limit(5)
                .toList();
    }
}