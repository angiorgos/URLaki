package com.urlaki.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "urls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Urls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bigURL;

    @Column(unique = true, nullable = false)
    private String shortURL;
}
