package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestParam String name, @RequestParam int age) {
        Student createdStudent = studentService.createStudent(name, age);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestParam String name, @RequestParam int age) {
        return studentService.updateStudent(id, name, age);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/count")
    public long getCount() {
        return studentService.getCount();
    }

    @GetMapping("/students/average-age")
    public Double getAverageAge() {
        List<Student> students = studentService.getAllStudents();
        return students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @GetMapping("/latest")
    public List<Student> getLatestStudents() {
        return studentService.getLatestStudents();
    }

    @GetMapping("/students/names/start-with-a")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getAllStudents().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("А") || name.startsWith("а"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/print-parallel")
    public void printStudentsParallel() {
        List<Student> students = studentService.getAllStudents();

        if (students.size() < 6) {
            logger.warn("Недостаточно студентов для выполнения задачи");
            return;
        }

        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании потоков", e);
            Thread.currentThread().interrupt();
        }
    }

    @GetMapping("/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> students = studentService.getAllStudents();

        if (students.size() < 6) {
            logger.warn("Недостаточно студентов для выполнения задачи");
            return;
        }

        class Printer {
            synchronized void printStudentName(Student student) {
                System.out.println(student.getName());
            }
        }

        Printer printer = new Printer();

        printer.printStudentName(students.get(0));
        printer.printStudentName(students.get(1));

        Thread thread1 = new Thread(() -> {
            try {
                printer.printStudentName(students.get(2));
                printer.printStudentName(students.get(3));
            } catch (Exception e) {
                logger.error("Ошибка в потоке 1 при выводе имен", e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                printer.printStudentName(students.get(4));
                printer.printStudentName(students.get(5));
            } catch (Exception e) {
                logger.error("Ошибка в потоке 2 при выводе имен", e);
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании потоков", e);
            Thread.currentThread().interrupt();
        }
    }
}