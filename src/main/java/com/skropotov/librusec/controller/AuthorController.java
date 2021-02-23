package com.skropotov.librusec.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skropotov.librusec.controller.tree.GenreTree;
import com.skropotov.librusec.controller.tree.TreeHelper;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.GenreRepository;

@Controller
@RequestMapping("authors")
public class AuthorController {
	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	GenreRepository genreRepository;

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		List<GenreTree> genreTrees = new ArrayList<GenreTree>();
		List<Genre> genres = genreRepository.findByParent(null);
		genres.forEach((x) -> TreeHelper.addGenre(genreTrees, x));
		model.addAttribute("genreCollection", genreTrees);

		String searchValue = request.getParameter("name");
		model.addAttribute("authors", authorRepository.findByFullNameContainingIgnoreCase(searchValue));
		return "books/authors";
	}
}
