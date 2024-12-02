package com.Cataloger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;
 

@Entity
@Table(name="product")
@Data

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation")
    private String designation ; 

    @Column(name = "prix")
    private Double prix ; 

    @Column(name = "quantite")
    private int quantite ; 

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
 