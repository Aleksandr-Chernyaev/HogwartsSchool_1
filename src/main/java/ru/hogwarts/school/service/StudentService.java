package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String name, int age) {
        Student student = new Student(name, age);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, String name, int age) {
        Student student = getStudent(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }
    }

    public long getCount() {
        return studentRepository.count();
    }

    public double getAverageAge() {
        Double avgAge = studentRepository.findAverageAge();
        return avgAge != null ? avgAge : 0.0;
    }

    public List<Student> getLatestStudents() {
        return studentRepository.findTop5ByOrderByIdDesc();
    }
}