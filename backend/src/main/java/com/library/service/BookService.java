package com.library.service;

import com.library.model.Book;
import com.library.model.Inventory;
import com.library.model.BorrowingRecord;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.InventoryRepository;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.StoredProcedureRepository;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final InventoryRepository inventoryRepository;
    private final BorrowingRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final StoredProcedureRepository spRepository;

    public BookService(BookRepository bookRepository, InventoryRepository inventoryRepository,
            BorrowingRecordRepository recordRepository, UserRepository userRepository,
            StoredProcedureRepository spRepository) {
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.spRepository = spRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public List<BorrowingRecord> getUserRecords(String phone) {
        User user = userRepository.findByPhoneNumber(phone).orElseThrow();
        return recordRepository.findByUser_UserId(user.getUserId());
    }

    @Transactional
    public String borrowBook(String phone, Long inventoryId) {
        User user = userRepository.findByPhoneNumber(phone).orElseThrow();
        int result = spRepository.borrowBook(user.getUserId(), inventoryId);
        if (result == -1) {
            throw new RuntimeException("Inventory is not available");
        }
        return "Book borrowed successfully";
    }

    @Transactional
    public String returnBook(Long recordId) {
        int result = spRepository.returnBook(recordId);
        if (result == -1) {
            throw new RuntimeException("Return failed: record not found or already returned");
        }
        return "Book returned successfully";
    }
}
