package org.fai.study.projectsem4.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fai.study.projectsem4.entity.ENUM.EShip;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shippings")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_id")
    private Integer shipId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EShip status;

    @Column(name = "image")
    private String image;

    @Column(name= "time_estimate")
    private LocalDateTime timeEstimate;

    @Column(name = "notification")
    private String notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id", nullable = false)
    @JsonIgnore
    private User shipper;





}
