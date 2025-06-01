package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utils")
public class UtilityController {

    @GetMapping("/sum-upto-1000000")
    public long getSumUptoMillion() {
        final int n = 1_000_000;
        return (long) n * (n + 1) / 2;
    }
}