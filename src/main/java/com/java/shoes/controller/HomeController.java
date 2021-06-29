package com.java.shoes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.shoes.domain.Article;
import com.java.shoes.service.ArticleService;

@Controller
public class HomeController {
		
	@Autowired
	private ArticleService articleService;
	
	
	@RequestMapping(value = { "/" , "admin" })
	public String index(Model model) {		
		List<Article> articles = articleService.findFirstArticles();
		model.addAttribute("articles", articles);
		return "index";
	}

	
}
