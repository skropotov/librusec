package com.skropotov.librusec.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class ArchiveLoader {
	private String archiveFilename;

	public String getArchiveFilename() {
		return archiveFilename;
	}

	public void setArchiveFilename(String archiveFilename) {
		this.archiveFilename = archiveFilename;
	}

	public Boolean hasBook(String filename) {
		File f = new File(archiveFilename);
		if(!f.exists()) {
			return false;
		}
		
		try {
			ZipFile zipFile = new ZipFile(archiveFilename);
			try {
				Enumeration<? extends ZipEntry> e = zipFile.entries();
				
				while(e.hasMoreElements()) {
					ZipEntry entry = (ZipEntry)e.nextElement();
				 
					if(entry.getName().toLowerCase().equals(filename)) {
						return true;
					}
					
				}	
			}
			finally {
				zipFile.close();
			}
		}
		catch (IOException e) {
			return false;
		}
		
		return false;
	}

	public String getFb2Description(String filename) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String bookContent = "";
		try {
			ZipFile zipFile = new ZipFile(archiveFilename);
			Document document = null;
			try {
				ZipEntry zipEntry = zipFile.getEntry(filename);
				InputStream stream = zipFile.getInputStream(zipEntry);

				DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(stream);
			}
			finally {
				zipFile.close();
			}
			
			NodeList nList = document.getFirstChild().getChildNodes();
			Node node = null;
			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "description") {
					node = nList.item(i);
				}
			}
			
			if (node != null) {
				nList = node.getChildNodes();
			}
			node = null;

			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "title-info") {
					node = nList.item(i);
				}
			}

			if (node != null) {
				nList = node.getChildNodes();
			}
			node = null;

			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "annotation") {
					node = nList.item(i);
				}
			}
			if (node != null) {
				return node.getTextContent();
			}
			
		}
		catch(Exception e) {
			return "";
		}
		return bookContent;
	}
	
	public byte[] getCover(String filename) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		String coverId = "";
		try {
			ZipFile zipFile = new ZipFile(archiveFilename);
			Document document = null;
			try {
				ZipEntry zipEntry = zipFile.getEntry(filename);
				InputStream stream = zipFile.getInputStream(zipEntry);

				DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(stream);
				document.getDocumentElement().normalize();
			}	
			finally {
				zipFile.close();
			}

			NodeList nList = document.getFirstChild().getChildNodes();
			Node node = null;
			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "description") {
					node = nList.item(i);
				}
			}
			
			if (node != null) {
				nList = node.getChildNodes();
			}
			node = null;

			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "title-info") {
					node = nList.item(i);
				}
			}

			if (node != null) {
				nList = node.getChildNodes();
			}
			node = null;

			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "coverpage") {
					node = nList.item(i);
				}
			}

			if (node != null) {
				nList = node.getChildNodes();
			}
			node = null;

			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "image") {
					node = nList.item(i);
				}
			}

			if (node != null) {
				NamedNodeMap attributes = node.getAttributes();
				for(int i = 0; i < attributes.getLength(); i++) {
					if (attributes.item(i).getNodeName() == "l:href") {
						coverId = attributes.item(i).getNodeValue().substring(1);
					}
				}
			}

			String cover = "";
			nList = document.getFirstChild().getChildNodes();
			node = null;
			for(int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeName() == "binary") {
					node = nList.item(i);
					NamedNodeMap attributes = node.getAttributes();
					for(int j = 0; j < attributes.getLength(); j++) {
						if (attributes.item(j).getNodeName() == "id") {
							String id = attributes.item(j).getNodeValue();
							if (id.compareTo(coverId) == 0) {
								cover = node.getTextContent();
							}
						}
					}
				}
			}
			
			if (cover != "") {
				if (cover.length() < 5000000) { 
					return Base64.getMimeDecoder().decode(cover);
				}
			}
		}
		catch(Exception e) {
			return null;
		}
		
		return null;
	}

}
