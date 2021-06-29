package com.java.shoes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.java.shoes.domain.Article;
import com.java.shoes.domain.ArticleBuilder;
import com.java.shoes.domain.Brand;
import com.java.shoes.domain.Category;
import com.java.shoes.domain.Size;
import com.java.shoes.service.ArticleService;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ServletContext servletConext;

	@RequestMapping("/add")
	public String addArticle(Model model) {
		Article article = new Article();
		model.addAttribute("article", article);
		model.addAttribute("allSizes", articleService.getAllSizes());
		model.addAttribute("allBrands", articleService.getAllBrands());
		model.addAttribute("allCategories", articleService.getAllCategories());
		return "addArticle";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addArticlePost(@ModelAttribute("article") Article article, HttpServletRequest request,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		String root = servletConext.getRealPath("/" + "WEB-INF" + File.separator + "classes" + File.separator + "static"
				+ File.separator + "image" + File.separator + "article");
		File dir = new File(root);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String path = dir.getAbsolutePath();
		// Get the file and save it somewhere
		byte[] bytes = multipartFile.getBytes();
		Path upload = Paths.get(path + File.separator + multipartFile.getOriginalFilename());
		Files.write(upload, bytes);

		Article newArticle = new ArticleBuilder().withTitle(article.getTitle()).withDescription(article.getDescription()).stockAvailable(article.getStock())
				.withPrice(article.getPrice()).imageLink(multipartFile.getOriginalFilename())
				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
				.ofCategories(Arrays.asList(request.getParameter("category").split("\\s*,\\s*")))
				.ofBrand(Arrays.asList(request.getParameter("brand").split("\\s*,\\s*"))).build();
		articleService.saveArticle(newArticle);
		return "redirect:article-list";
	}

	@RequestMapping("/article-list")
	public String articleList(Model model) {
		List<Article> articles = articleService.findAllArticles();
		model.addAttribute("articles", articles);
		return "articleList";
	}

	@RequestMapping("/edit")
	public String editArticle(@RequestParam("id") Long id, Model model) {
		Article article = articleService.findArticleById(id);
		String preselectedSizes = "";
		for (Size size : article.getSizes()) {
			preselectedSizes += (size.getValue() + ",");
		}
		String preselectedBrands = "";
		for (Brand brand : article.getBrands()) {
			preselectedBrands += (brand.getName() + ",");
		}
		String preselectedCategories = "";
		for (Category category : article.getCategories()) {
			preselectedCategories += (category.getName() + ",");
		}
		model.addAttribute("article", article);
		model.addAttribute("preselectedSizes", preselectedSizes);
		model.addAttribute("preselectedBrands", preselectedBrands);
		model.addAttribute("preselectedCategories", preselectedCategories);
		model.addAttribute("allSizes", articleService.getAllSizes());
		model.addAttribute("allBrands", articleService.getAllBrands());
		model.addAttribute("allCategories", articleService.getAllCategories());
		return "editArticle";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editArticlePost(@ModelAttribute("article") Article article, HttpServletRequest request) {
		Article newArticle = new ArticleBuilder().withTitle(article.getTitle()).withDescription(article.getDescription()).stockAvailable(article.getStock())
				.withPrice(article.getPrice()).imageLink(article.getPicture())
				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
				.ofCategories(Arrays.asList(request.getParameter("category").split("\\s*,\\s*")))
				.ofBrand(Arrays.asList(request.getParameter("brand").split("\\s*,\\s*"))).build();
		newArticle.setId(article.getId());
		articleService.saveArticle(newArticle);
		return "redirect:article-list";
	}

	@RequestMapping("/delete")
	public String deleteArticle(@RequestParam("id") Long id) {
		articleService.deleteArticleById(id);
		return "redirect:article-list";
	}

}
