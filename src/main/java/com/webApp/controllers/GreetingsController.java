package com.webApp.controllers;

import com.webApp.json.Greetings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/greetings")
public class GreetingsController {
    private static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/show")
    public Greetings getGreetings(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Greetings(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

    @GetMapping
    public String index() {
        return "redirect:/swagger-ui";
    }
}
