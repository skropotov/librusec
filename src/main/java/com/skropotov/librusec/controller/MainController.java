package com.skropotov.librusec.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.skropotov.librusec.LibrusecApplication;
import com.skropotov.librusec.controller.tree.GenreTree;
import com.skropotov.librusec.controller.tree.TreeHelper;
import com.skropotov.librusec.loader.BookInfo;
import com.skropotov.librusec.loader.BookLoader;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.BookRepository;
import com.skropotov.librusec.model.repository.GenreRepository;
import com.skropotov.librusec.model.repository.SeriaRepository;

@Controller
@RequestMapping("/")
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(LibrusecApplication.class);
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	GenreRepository genreRepository;
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	SeriaRepository seriaRepository;
	
	@Autowired
	BookLoader bookLoader;

	@Value("${archive.path}")
	private String archivePath;
	
	@GetMapping()
	public String index(HttpServletRequest request, Model model) {
		List<GenreTree> genreTrees = new ArrayList<GenreTree>();
		List<Genre> genres = genreRepository.findByParent(null);
		genres.forEach((x) -> TreeHelper.addGenre(genreTrees, x));
		model.addAttribute("genreCollection", genreTrees);
		model.addAttribute("booksCount", bookRepository.count());
		model.addAttribute("authorsCount", authorRepository.count());
		model.addAttribute("seriesCount", seriaRepository.count());
		
		return "index";
	}
	
	@PostMapping()
	public String load(@ModelAttribute("filename") String filename, @RequestParam("filenameName") MultipartFile file) {
		List<BookInfo> books = bookLoader.parseFileDescription(filename, file);

		books.stream().forEach(c -> bookLoader.saveBook(c));
		
		log.info("loaded " + books.size() + " books from " + filename);

		return "redirect:/";
	}
}
