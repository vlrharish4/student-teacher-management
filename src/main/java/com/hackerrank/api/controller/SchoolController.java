package com.hackerrank.api.controller;

import com.hackerrank.api.model.Student;
import com.hackerrank.api.model.Teacher;
import com.hackerrank.api.repository.StudentRepository;
import com.hackerrank.api.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/school")
public class SchoolController {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public SchoolController(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    //create teacher
    @PostMapping("/teacher")
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher _teacher = teacherRepository.save(teacher);
        return new ResponseEntity<>(_teacher, HttpStatus.CREATED);
    }

    //create student
    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student _student = studentRepository.save(student);
        return new ResponseEntity<>(_student, HttpStatus.CREATED);
    }

    //add student to a teacher
    @PostMapping("/teacher/{teacherId}/addStudent")
    public ResponseEntity<Teacher> addStudent(@PathVariable(value = "teacherId") Long teacherId, @RequestBody Student addStudent) {
        // Fetch teacher by ID
        Teacher teacher = teacherRepository.findById(teacherId).get();
        
        // Check if student exists
        Optional<Student> studentExists = studentRepository.findById(addStudent.getId());
        // If student doesn't exist, perists.
        if(!studentExists.isPresent()) {
            studentRepository.save(addStudent);
        }

        // Append student object to students of teacher.
        teacher.getStudents().add(addStudent);

        // Update teacher object.
        Teacher updatedTeacher = teacherRepository.save(teacher);

        // Return updated teacher entity
        return new ResponseEntity<>(updatedTeacher, HttpStatus.CREATED);
    }

    //get students of a teacher
    @GetMapping("/teacher/{teacherId}/students")
    public ResponseEntity<Set<Student>> getStudentsOfATeacher(@PathVariable(value = "teacherId") Long teacherId) {
        // Fetch teacher by ID
        Teacher teacher = teacherRepository.findById(teacherId).get();
        // Return with 200 status
        return new ResponseEntity<>(teacher.getStudents(), HttpStatus.OK);
    }

    // get teachers of a student
    @GetMapping("/student/{studentId}/teachers")
    public ResponseEntity<Set<Teacher>> getTeachersOfAStudent(@PathVariable(value = "studentId") Long studentId) {
        // Fetch student by ID
        Student student = studentRepository.findById(studentId).get();
        // Return with 200 status
        return new ResponseEntity<>(student.getTeachers(), HttpStatus.OK);
    }
}
