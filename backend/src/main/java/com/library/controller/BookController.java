package com.library.controller;

import com.library.model.Book;
import com.library.model.Inventory;
import com.library.model.BorrowingRecord;
import com.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(bookService.getAllInventory());
    }

    @GetMapping("/records")
    public ResponseEntity<List<BorrowingRecord>> getUserRecords(Authentication authentication) {
        String phone = authentication.getName();
        return ResponseEntity.ok(bookService.getUserRecords(phone));
    }

    @PostMapping("/inventory/{inventoryId}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long inventoryId, Authentication authentication) {
        try {
            String phone = authentication.getName();
            String result = bookService.borrowBook(phone, inventoryId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/records/{recordId}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long recordId) {
        try {
            String result = bookService.returnBook(recordId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
