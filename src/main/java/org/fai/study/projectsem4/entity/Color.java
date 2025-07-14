package org.fai.study.projectsem4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private int colorId;
    @Column(name = "color_name")
    private String colorName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private List<Product> products;
}
