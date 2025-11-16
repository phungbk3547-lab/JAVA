package com.example.controller;

import com.example.model.Guest;
import com.example.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestRepository guestRepository;

    @Autowired
    public GuestController(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    // ✅ Lấy danh sách tất cả khách
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        List<Guest> guests = guestRepository.findAll();
        return ResponseEntity.ok(guests);
    }

    // ✅ Lấy thông tin 1 khách theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        return guestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Thêm khách mới
    @PostMapping
    public ResponseEntity<Guest> addGuest(@RequestBody Guest guest) {
        Guest savedGuest = guestRepository.save(guest);
        return ResponseEntity.ok(savedGuest);
    }

    // ✅ Cập nhật thông tin khách
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @RequestBody Guest guestDetails) {
        return guestRepository.findById(id)
                .map(guest -> {
                    guest.setName(guestDetails.getName());
                    guest.setEmail(guestDetails.getEmail());
                    guest.setPhone(guestDetails.getPhone());
                    Guest updatedGuest = guestRepository.save(guest);
                    return ResponseEntity.ok(updatedGuest);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Xóa khách theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        if (!guestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        guestRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
