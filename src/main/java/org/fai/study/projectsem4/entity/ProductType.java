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
@Table(name = "product_type")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id")
    private Integer productTypeId;
    @Column(name = "product_type_name")
    private String productTypeName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id")
    private List<Product> products;
}
