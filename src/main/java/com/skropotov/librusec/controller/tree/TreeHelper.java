package com.skropotov.librusec.controller.tree;

import java.util.List;

import com.skropotov.librusec.model.Genre;

public class TreeHelper {
	public static void addGenre(List<GenreTree> genreTrees, Genre genre) {
		genreTrees.add(new GenreTree(genre.getId().toString(), genre.getName(), genre.getParent() == null ? "#" : genre.getParent().getId().toString()));
		genre.getChildren().forEach(x -> addGenre(genreTrees, x));
	}
}
