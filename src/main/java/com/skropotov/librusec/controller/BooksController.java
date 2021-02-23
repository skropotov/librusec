package com.skropotov.librusec.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skropotov.librusec.controller.tree.GenreTree;
import com.skropotov.librusec.controller.tree.TreeHelper;
import com.skropotov.librusec.loader.ArchiveLoader;
import com.skropotov.librusec.model.Author;
import com.skropotov.librusec.model.Book;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.Seria;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.BookPagingRepository;
import com.skropotov.librusec.model.repository.BookRepository;
import com.skropotov.librusec.model.repository.SeriaRepository;
import com.skropotov.librusec.model.repository.GenreRepository;

@Controller
@RequestMapping("/books")
public class BooksController {
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	BookPagingRepository bookPagingRepository;
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	SeriaRepository seriaRepository;
	
	@Autowired
	GenreRepository genreRepository;
	
	@Autowired
	ArchiveLoader archiveLoader;
	
	@Value("${booklist.pagesize}")
	private int pageSize;

	private void buildAuthorModel(Model model, Long authorId, String sortType, String sortDirection) {
		Author author = authorRepository.findById(authorId).get();
		model.addAttribute("author", author);
		
		if (sortType != null && sortType.equals("date")) {
			model.addAttribute("sortSeriaLink", "author=" + authorId.toString() + "&sort=seria");
			model.addAttribute("sortSeriaImage", "");
			if (sortDirection != null && sortDirection.equals("desc")) {
				model.addAttribute("books", bookRepository.findByAuthorsOrderByAdditionDateDesc(author));
				model.addAttribute("sortDateLink", "author=" + authorId.toString());
				model.addAttribute("sortDateImage", "books/images/sort_down.png");
			}
			else {
				model.addAttribute("books", bookRepository.findByAuthorsOrderByAdditionDateAsc(author));
				model.addAttribute("sortDateLink", "author=" + authorId.toString() + "&sort=date&direction=desc");
				model.addAttribute("sortDateImage", "books/images/sort_up.png");
			}
		}
		else if (sortType != null && sortType.equals("seria")) {
			model.addAttribute("sortDateLink", "author=" + authorId.toString() + "&sort=date");
			model.addAttribute("sortDateImage", "");
			
			if (sortDirection != null && sortDirection.equals("desc")) {
				model.addAttribute("books", bookRepository.findByAuthorsOrderBySeria_NameDescSeriaNumAsc(author));
				model.addAttribute("sortSeriaLink", "author=" + authorId.toString());
				model.addAttribute("sortSeriaImage", "books/images/sort_down.png");
			}
			else {
				model.addAttribute("books", bookRepository.findByAuthorsOrderBySeria_NameAscSeriaNumAsc(author));
				model.addAttribute("sortSeriaLink", "author=" + authorId.toString() + "&sort=seria&direction=desc");
				model.addAttribute("sortSeriaImage", "books/images/sort_up.png");
			}
		}
		else {
			model.addAttribute("books", bookRepository.findByAuthors(author));
			model.addAttribute("sortDateLink", "author=" + authorId.toString() + "&sort=date");
			model.addAttribute("sortDateImage", "");
			model.addAttribute("sortSeriaLink", "author=" + authorId.toString() + "&sort=seria");
			model.addAttribute("sortSeriaImage", "");
		}
	}
	
	private void buildSeriaModel(Model model, Long seriaId, String sortType, String sortDirection) {
		Seria seria = seriaRepository.findById(seriaId).get();
		model.addAttribute("seria", seria);
		
		if (sortType != null && sortType.equals("date")) {
			model.addAttribute("sortSeriaLink", "seria=" + seriaId.toString() + "&sort=seria");
			model.addAttribute("sortSeriaImage", "");
			
			if (sortDirection != null && sortDirection.equals("desc")) {
				model.addAttribute("books", bookRepository.findBySeriaOrderByAdditionDateDesc(seria));
				model.addAttribute("sortDateLink", "seria=" + seriaId.toString());
				model.addAttribute("sortDateImage", "books/images/sort_down.png");
			}
			else {
				model.addAttribute("books", bookRepository.findBySeriaOrderByAdditionDateAsc(seria));
				model.addAttribute("sortDateLink", "seria=" + seriaId.toString() + "&sort=date&direction=desc");
				model.addAttribute("sortDateImage", "books/images/sort_up.png");
			}
		}
		else if (sortType != null && sortType.equals("seria")) {
			model.addAttribute("sortDateLink", "seria=" + seriaId.toString() + "&sort=date");
			model.addAttribute("sortDateImage", "");
			
			if (sortDirection != null && sortDirection.equals("desc")) {
				model.addAttribute("sortSeriaLink", "seria=" + seriaId.toString());
				model.addAttribute("sortSeriaImage", "books/images/sort_down.png");
				model.addAttribute("books", bookRepository.findBySeriaOrderBySeriaNumDesc(seria));
			}
			else {
				model.addAttribute("sortSeriaLink", "seria=" + seriaId.toString() + "&sort=seria&direction=desc");
				model.addAttribute("sortSeriaImage", "");
				model.addAttribute("books", bookRepository.findBySeriaOrderBySeriaNumAsc(seria));
			}
		}
		else {
			model.addAttribute("sortDateLink", "seria=" + seriaId.toString() + "&sort=date");
			model.addAttribute("sortDateImage", "");
			model.addAttribute("sortSeriaLink", "seria=" + seriaId.toString() + "&sort=seria");
			model.addAttribute("sortSeriaImage", "");
			model.addAttribute("books", bookRepository.findBySeriaOrderBySeriaNumAsc(seria));
		}
	}
	
	private void buildGenreModel(Model model, int page, Long genreId, String sortType, String sortDirection) {
		Genre genre = genreRepository.findById(genreId).get();
		Pageable pageable;
		if (sortType != null && sortType.equals("date")) {
			if (sortDirection != null && sortDirection.equals("desc")) {
				pageable = PageRequest.of(page,  pageSize, Sort.by("AdditionDate").descending());
			}
			else {
				pageable = PageRequest.of(page,  pageSize, Sort.by("AdditionDate").ascending());
			}
		}
		else {
			pageable = PageRequest.of(page, pageSize);
		}
		
		model.addAttribute("genre", genre);
		model.addAttribute("page", page);
		model.addAttribute("sortType", sortType == null ? "none" : sortType);
		model.addAttribute("sortDirection", sortDirection == null ? "asc" : sortDirection);
		model.addAttribute("books", bookPagingRepository.findByGenres(genre, pageable));
	}
	
	private void buildNameModel(Model model, String nameSearch, String sortType, String sortDirection) {
		//TODO
	}
	
	private Long getParameter(HttpServletRequest request, String paramName) {
		String paramValue = request.getParameter(paramName);
		if (paramValue != null) {
			return Long.parseLong(paramValue);
		}
		else {
			return 0L;
		}
	}
	
	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		Long authorId = getParameter(request, "author");
		Long seriaId = getParameter(request, "seria");
		Long genreId = getParameter(request, "genre");
		Long page = getParameter(request, "page");
		String sortType = request.getParameter("sort");
		String sortDirection = request.getParameter("direction");
		String nameSearch = request.getParameter("name");
		
		List<GenreTree> genreTrees = new ArrayList<GenreTree>();
		List<Genre> genres = genreRepository.findByParent(null);
		genres.forEach((x) -> TreeHelper.addGenre(genreTrees, x));
		model.addAttribute("genreCollection", genreTrees);
		
		if (authorId > 0) {
			buildAuthorModel(model, authorId, sortType, sortDirection);
		}
		else if (seriaId > 0) {
			buildSeriaModel(model, seriaId, sortType, sortDirection);
		}
		else if (nameSearch != null) {
			buildNameModel(model, nameSearch, sortType, sortDirection);
		}
		else if (genreId > 0) {
			if (page > 0) {
				page--;
			}
			buildGenreModel(model, page.intValue(), genreId, sortType, sortDirection);
			return "books/list_genre";
		}
		return "books/list";
	}
	
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		Book book = bookRepository.findById(id).get();
		model.addAttribute("book", book);

		List<GenreTree> genreTrees = new ArrayList<GenreTree>();
		List<Genre> genres = genreRepository.findByParent(null);
		genres.forEach((x) -> TreeHelper.addGenre(genreTrees, x));
		model.addAttribute("genreCollection", genreTrees);

		archiveLoader.setArchiveFilename(book.getArchiveName());
		model.addAttribute("description", archiveLoader.getFb2Description(book.getFilename()));
		byte[] cover = archiveLoader.getCover(book.getFilename());
		if (cover != null) {
			model.addAttribute("encodeImage", Base64.getEncoder().encodeToString(cover));
		}
		else {
			model.addAttribute("encodeImage", "");
		}
		
		return "books/info";
	}
}
