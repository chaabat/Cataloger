package com.Cataloger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
@Table(name="category")
@Data
public class Category {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name ; 

    @Column(name = "description")
    private String description ; 

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
