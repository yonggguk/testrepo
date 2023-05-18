package com.example.springnew.controller;

import com.example.springnew.model.News;
import com.example.springnew.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class NewsController {
    private final NewsRepository newsRepository;

    @GetMapping("/newsmain")
    public ModelAndView newMain(Optional<Integer> page, Optional<Integer> size){
        int newsSize = size.isPresent() ? size.get() : 10;
        int newsPage = page.isPresent() ? page.get() : 0;
        ModelAndView mav = new ModelAndView();

        PageRequest pageRequest = PageRequest.of(newsPage, newsSize);
        Page<News> newsPageList = newsRepository.findAll(pageRequest);
        mav.addObject("newsList", newsPageList);

        if(newsPageList.isEmpty()){
            mav.addObject("msg", "작성된 뉴스가 없습니다");
        } else{
            mav.addObject("newsList", newsPageList.toList());
            mav.addObject("newsPageInfo", newsPageList);
        }
        mav.setViewName("newsBoard");
        return mav;
    }

    @GetMapping("/listOne")
    @ResponseBody
    public News getListOneNews(int id){
        News news = newsRepository.findById(id).get();
        news.setCnt(news.getCnt()+1);
        newsRepository.save(news);
        return news;
    }

    @GetMapping("/delete")
    @Transactional
    public String deleteNews(int id){
        newsRepository.deleteById(id);
        return "redirect:/newsmain";
    }

    @PostMapping("/insert")
    @Transactional
    public String insertNews(News news){
        newsRepository.save(news);
        return "redirect:/newsmain";
    }

    @PostMapping("/update")
    @Transactional
    public String updateNews(News news){
        News oldNews = newsRepository.findById(news.getId()).get();
        oldNews.setTitle(news.getTitle());
        oldNews.setContent(news.getContent());
        oldNews.setWriter(news.getWriter());
        return "redirect:/newsmain";

    }
    @GetMapping("/writer")
    public ModelAndView writerNews(String writer, Optional<Integer> page, Optional<Integer> size){
        int newsSize = size.orElse(10);
        int newsPage = page.orElse(0);
        ModelAndView mav = new ModelAndView();
        PageRequest pageRequest = PageRequest.of(newsPage, newsSize);
        Page<News> newsPageList = newsRepository.findByWriter(writer, pageRequest);

        if(newsPageList.isEmpty()){
            mav.addObject("msg", "작성된 뉴스가 없습니다");
        } else{
            mav.addObject("newsList", newsPageList.toList());
            mav.addObject("newsPageInfo", newsPageList);
        }
        mav.setViewName("newsWriterBoard");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchNews(String keyword, Optional<Integer> page, Optional<Integer> size){
        int newsSize = size.orElse(10);
        int newsPage = page.orElse(0);

        PageRequest pageRequest = PageRequest.of(newsPage, newsSize);
        Page<News> newsSearchList = newsRepository.findByContentContains(keyword, pageRequest);
        ModelAndView mav = new ModelAndView();
        if(newsSearchList.isEmpty()){
            mav.addObject("msg", "작성된 뉴스가 없습니다");
        } else{
            mav.addObject("newsList", newsSearchList.toList());
            mav.addObject("newsPageInfo", newsSearchList);
            mav.addObject("keyword", keyword);
        }
        mav.setViewName("newsSearchBoard");
        return mav;
    }

}
