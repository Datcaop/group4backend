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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private Integer cateId;
    @Column(name = "cate_nam")
    private String cateName;
    @Lob
    @Column(name = "cate_description",columnDefinition = "TEXT")
    private String cateDes;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_id")
    private List<Product> products;

}
