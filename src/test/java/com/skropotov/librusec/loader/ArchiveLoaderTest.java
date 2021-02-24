package com.skropotov.librusec.loader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArchiveLoaderTest {
	@Autowired
	ArchiveLoader archiveLoader;
	
	@Value("${archive.path}")
	private String archivePath;
	
	@Test
	public void TestHasBook() {
		archiveLoader.setArchiveFilename(archivePath + "\\fb2-000024-030559.zip");
		assertFalse(archiveLoader.hasBook("11.fb2"));
		assertTrue(archiveLoader.hasBook("100.fb2"));

		archiveLoader.setArchiveFilename(archivePath + "\\nonExistArchive.zip");
		assertFalse(archiveLoader.hasBook("100.fb2"));
	}
	
	@Test
	public void TestGetFb2Description() {
		archiveLoader.setArchiveFilename(archivePath + "\\fb2-000024-030559.zip");
		assertEquals("", archiveLoader.getFb2Description("100.fb2"));
		assertEquals("Эта приключенческая повесть отражает события, происходившие в период революции и гражданской войны в Дагестане. Главный герой ее смелый и отважный Хасан встал во главе горцев, поднявшихся на борьбу за свободу и справедливость.\n\n", archiveLoader.getFb2Description("235.fb2"));

		archiveLoader.setArchiveFilename(archivePath + "\\nonExistArchive.zip");
		assertEquals("", archiveLoader.getFb2Description("235.fb2"));
	}
	
	@Test
	public void TestGetCover() {
		archiveLoader.setArchiveFilename(archivePath + "\\fb2-000024-030559.zip");
		assertEquals(null, archiveLoader.getCover("NonExist.fb2"));
		assertEquals(null, archiveLoader.getCover("100.fb2"));
		assertEquals(10651, archiveLoader.getCover("235.fb2").length);

		archiveLoader.setArchiveFilename(archivePath + "\\nonExistArchive.zip");
		assertEquals(null, archiveLoader.getCover("100.fb2"));
}
}
