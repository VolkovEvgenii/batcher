package ru.volkov.batch.sotialuko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.volkov.batch.sotialuko.exporter.SotialUkoExporter;

@RestController
public class SotialUkoController {

    private SotialUkoExporter exporter;

    @Autowired
    public SotialUkoController(SotialUkoExporter sotialUkoExporter) {
        this.exporter = sotialUkoExporter;
    }

    @GetMapping("/greeting")
    public String greeting() {
        return "Sotial uko welcomes you";
    }

    @GetMapping("/test")
    public void test() {
        exporter.exportToFile();
    }
}
