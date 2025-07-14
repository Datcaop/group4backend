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
@Table(name = "genders")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id")
    private Integer genderId;
    @Column(name = "gender_name")
    private String genderName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private List<Product> products;

}
