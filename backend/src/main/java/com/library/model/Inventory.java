package com.library.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @ManyToOne
    @JoinColumn(name = "isbn", nullable = false)
    private Book book;

    @Column(name = "store_time")
    private LocalDateTime storeTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('在庫', '出借中', '整理中', '遺失', '損毀', '廢棄') DEFAULT '在庫'")
    private Status status;

    public enum Status {
        在庫, 出借中, 整理中, 遺失, 損毀, 廢棄
    }
}
