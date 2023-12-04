package com.waiter.javawaiter.tip.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tips_qr")
@AllArgsConstructor
@NoArgsConstructor
public class Tip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tip_id")
    private Long tipId;
    @Column(name = "qr_code")
    private String qrCode;
}
