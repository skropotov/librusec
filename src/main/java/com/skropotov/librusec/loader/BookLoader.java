package com.skropotov.librusec.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skropotov.librusec.LibrusecApplication;
import com.skropotov.librusec.model.Author;
import com.skropotov.librusec.model.Book;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.Seria;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.BookRepository;
import com.skropotov.librusec.model.repository.GenreRepository;
import com.skropotov.librusec.model.repository.SeriaRepository;

@Service
public class BookLoader {
	private static final Logger log = LoggerFactory.getLogger(LibrusecApplication.class);

	@Autowired
	ArchiveLoader archiveLoader;

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	GenreRepository genreRepository;
	
	@Autowired
	SeriaRepository seriaRepository;

	@Autowired
	AuthorRepository authorRepository;

	@Value("${archive.path}")
	private String archivePath;
	
	private AuthorInfo[] getAuthors(String authorStr) {
		List<AuthorInfo> authorInfos = new ArrayList<AuthorInfo>();
		String[] authors = authorStr.split(":");
		for (String author : authors) {
			String[] names = author.split(",");
			
			if (names.length > 0) {
				AuthorInfo authorInfo = null;
				if(names.length == 1) {
					authorInfo = new AuthorInfo("", names[0], "");
				}
				
				if(names.length == 2) {
					authorInfo = new AuthorInfo(names[1], names[0], "");
				}
				
				if(names.length == 3) {
					authorInfo = new AuthorInfo(names[1], names[0], names[2]);
				}
				if (authorInfo != null) {
					authorInfos.add(authorInfo);
				}
			}
		}
		
		return authorInfos.stream().toArray(AuthorInfo[]::new);
	}
	
	private BookInfo parseBookDescription(String description, String archiveFilename) {
		String[] bookComponents = description.split(Character.toString((char)4));
		
		String authorStr = bookComponents[0];
		AuthorInfo[] authors = getAuthors(authorStr);
		
		
		String[] genres = bookComponents[1].split(":");
		String name = bookComponents[2];
		
		if (name.length() > 150) {
			name = name.substring(0,  149);
		}
		String seriaName = bookComponents[3];
		
		if (seriaName.length() > 80) {
			seriaName = seriaName.substring(0, 79);
		}
		
		String seriaNum = bookComponents[4];
		int bookSize = Integer.parseInt(bookComponents[6]);
		String bookFilename = bookComponents[7] + '.' + bookComponents[9];
		Boolean isDeleted = bookComponents[8] == "1"; //?
		String loadDateStr = bookComponents[10];
		
		log.debug(loadDateStr);
		Date loadedDate;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			loadedDate = format.parse(loadDateStr);
		}
		catch(ParseException e) {
			loadedDate = null;
		}
		
		String language = "";
		if (bookComponents.length >= 12) {
			language = bookComponents[11];
		}
		
		archiveLoader.setArchiveFilename(archiveFilename);
		Boolean hasFile = archiveLoader.hasBook(bookFilename);
		if (!hasFile) {
			return null;
		}

		return new BookInfo(name, archiveFilename, bookFilename, seriaName, seriaNum.equals("") ? null : Long.parseLong(seriaNum), loadedDate, isDeleted, language,  
				genres, authors, bookSize);
	}

	private Seria saveSeria(String seriaName) {
		Seria seria = null;

		if (!seriaName.equals("")) {
			List<Seria> series = seriaRepository.findByName(seriaName);
			if (series.isEmpty()) {
				seria = seriaRepository.save(new Seria(seriaName));
			}
			else {
				seria = series.get(0);
			}
		}
		
		return seria;
	}

	private HashSet<Author> saveAuthors(AuthorInfo[] authorInfos) {
		Author author;
		HashSet<Author> authorSet = new HashSet<Author>();

		for (AuthorInfo authorInfo : authorInfos) {
			List<Author> authors = authorRepository.findByFullName(authorInfo.getFullName());
			if (authors.isEmpty()) {
				author = authorRepository.save(new Author(authorInfo.getLastName(), authorInfo.getPrimaryName(), authorInfo.getMiddleName()));
			}
			else {
				author = authors.get(0);
			}
			authorSet.add(author);
		}
		
		return authorSet;
	}
	
	public List<BookInfo> parseFileDescription(String filename, MultipartFile file) {
		InputStream stream = null;
		String archiveFilename = archivePath + "\\" + filename.substring(0, filename.indexOf(".")) + ".zip";
		
		try {
			stream = file.getInputStream();
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}

		List<BookInfo> books = new BufferedReader(new InputStreamReader(stream))
			.lines()
			.map(description -> 
				{
					try {
						return parseBookDescription(description, archiveFilename);
					}
					catch(Exception e) {
						log.error(e.getMessage());
						log.error(description);
					}
					return null;
				})
			.filter(c -> c != null)
			.collect(Collectors.toList());

		return books;
	}
	
	public Book saveBook(BookInfo info) {
		Book book = bookRepository.findByFilename(info.getFilename());
		
		if (book == null) {
			Seria seria = saveSeria(info.getSeria());
			HashSet<Author> authors = saveAuthors(info.getAuthors());
			
			HashSet<Genre> bookGenres = new HashSet<>();
			for (String genreInfo : info.getGenres()) {
				List<Genre> genres = genreRepository.findByFb2code(genreInfo);
				bookGenres.addAll(genres);
			}
			
			try {
				book = bookRepository.save(new Book(null, info.getName(), seria, authors, bookGenres, info.getLoadedDate(), info.getLanguage(), 
					info.getArchiveName(), info.getFilename(), info.getBookSize(), info.getIsDeleted() ? 1 : 0, info.getSeriaNum()));
				return book;
			}
			catch (Exception e) {
				log.error(e.getMessage());
				log.error(info.getFilename());
			}

		}
		return book;
	}
}
