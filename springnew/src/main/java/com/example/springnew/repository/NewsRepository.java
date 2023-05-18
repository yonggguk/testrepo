package com.example.springnew.repository;

import com.example.springnew.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    Page<News> findByWriter(String writer, PageRequest pageRequest);
    Page<News> findByContentContains(String keyword, PageRequest pageRequest);
}
