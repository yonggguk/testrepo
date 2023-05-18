package com.example.springnew.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class News {
    @Id
    private int id;

    private String writer;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDate writedate;

    private  int cnt;

}
