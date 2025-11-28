package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Khi ứng dụng khởi động xong -> in đường dẫn web ra console
    @EventListener(ApplicationReadyEvent.class)
    public void showStartupMessage() {
        System.out.println("======================================");
        System.out.println("✅ Ứng dụng đã khởi động thành công!");
        System.out.println("🌐 Mở web tại: http://localhost:8080");
        System.out.println("======================================");
    }
}
