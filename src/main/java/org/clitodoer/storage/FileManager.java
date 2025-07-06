package org.clitodoer.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.clitodoer.model.Note;
import org.clitodoer.utils.Os;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 **/
public final class FileManager {
	private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
	private FileData fileData;
	private final String GLOBAL_SECTION = "global";

	public FileManager() {
		loadContent();
	}

	public void addGlobalNote(String note) {
		if (fileData == null) {
			fileData = new FileData();
		}
		if (fileData.getSections() == null) {
			List<FileData.Section> section = new ArrayList<>();
			section.add(new FileData.Section(GLOBAL_SECTION, new ArrayList<>()));
			fileData.setSections(section);
		}
		FileData.Section globalSection = fileData.getSections().stream()
				.filter(section -> GLOBAL_SECTION.equals(section.getName()))
				.findFirst()
				.orElseGet(() -> {
					FileData.Section newSection = new FileData.Section(GLOBAL_SECTION, new ArrayList<>());
					fileData.getSections().add(newSection);
					return newSection;
				});
		globalSection.addNote(new Note(globalSection.getNotes().size() + 1, note));
		save();
	}

	public void addNoteToSection(String section, String note) {
		if (fileData == null) {
			fileData = new FileData();
		}
		if (fileData.getSections() == null) {
			List<FileData.Section> newSection = new ArrayList<>();
			newSection.add(new FileData.Section(section, new ArrayList<>()));
			fileData.setSections(newSection);
		}
		FileData.Section targetSection = fileData.getSections().stream()
				.filter(sec -> section.equals(sec.getName()))
				.findFirst()
				.orElseGet(() -> {
					FileData.Section newSection = new FileData.Section(section, new ArrayList<>());
					fileData.getSections().add(newSection);
					return newSection;
				});
		targetSection.addNote(new Note(targetSection.getNotes().size() + 1, note));
		save();
	}

	public void deleteNoteFromSection(String section, int noteIndex) {
		if (fileData == null || fileData.getSections() == null) {
			return; // No data to delete from
		}
		FileData.Section targetSection = fileData.getSections().stream()
				.filter(sec -> section.equals(sec.getName()))
				.findFirst()
				.orElse(null);
		if (targetSection != null) {
			targetSection.removeNote(noteIndex - 1);
			save();
		}
	}

	public void deleteNoteFromGlobalSection(int noteIndex) {
		deleteNoteFromSection(GLOBAL_SECTION, noteIndex);
	}

	public void deleteSection(String section) {
		if (fileData == null || fileData.getSections() == null) {
			return; // No data to delete from
		}
		fileData.setSections(fileData.getSections().stream()
				.filter(sec -> !section.equals(sec.getName()))
				.toList());
		save();
	}

	public void updateNoteInGlobalSection(int noteIndex, String newText) {
		updateNoteInSection(GLOBAL_SECTION, noteIndex, newText);
	}

	public void updateNoteInSection(String section, int noteIndex, String newText) {
		if (fileData == null || fileData.getSections() == null) {
			return; // No data to update
		}
		FileData.Section targetSection = fileData.getSections().stream()
				.filter(sec -> section.equals(sec.getName()))
				.findFirst()
				.orElse(null);
		if (targetSection != null && noteIndex > 0 && noteIndex <= targetSection.getNotes().size()) {
			targetSection.updateNote(noteIndex - 1, newText);
			save();
		}
	}

	public void updateSection(String section, String newText) {
		if (fileData == null || fileData.getSections() == null) {
			return; // No data to update
		}
		FileData.Section targetSection = fileData.getSections().stream()
				.filter(sec -> section.equals(sec.getName()))
				.findFirst()
				.orElse(null);
		if (targetSection != null) {
			targetSection.setName(newText);
			save();
		}
	}

	public List<Note> listNotes(String section) {
		if (fileData == null || fileData.getSections() == null) {
			return new ArrayList<>();
		}
		return fileData.getSections().stream()
				.filter(sec -> section.equals(sec.getName()))
				.flatMap(sec -> sec.getNotes().stream())
				.toList();
	}

	public List<String> listAllSections() {
		if (fileData == null || fileData.getSections() == null) {
			return new ArrayList<>();
		}
		return fileData.getSections().stream()
				.map(FileData.Section::getName)
				.toList();
	}

	private void save() {
		Path filePath = ensureFileExist();
		if (!Files.exists(filePath)) {
			throw new RuntimeException("Failed to initialize file storage: " + filePath);
		} else {
			System.out.println("File storage initialized at: " + filePath);
			try {
				mapper.writeValue(Files.newBufferedWriter(filePath, StandardOpenOption.WRITE), this.fileData);
			} catch (IOException ex) {
				throw new RuntimeException("Failed to write file data: " + ex.getMessage(), ex);
			}
		}
	}

	private Path ensureFileExist() {
		String FILE_NAME = "data.enc";
		try {
			Path baseDir = Os.getStoragePath();
			Files.createDirectories(baseDir);
			Path filePath = baseDir.resolve(FILE_NAME);
			if (!Files.exists(filePath)) {
				Files.createFile(filePath);
			}
			return filePath;
		} catch (IOException ex) {
			throw new RuntimeException("Failed to initialize file storage: " + ex.getMessage(), ex);
		}
	}

	public void loadContent() {
		Path filePath = ensureFileExist();
		if (!Files.exists(filePath)) {
			throw new RuntimeException("Failed to initialize file storage: " + filePath);
		} else {
			System.out.println("File storage initialized at: " + filePath);
			try {
				if (Files.size(filePath) == 0) {
					fileData = new FileData();
					return;
				}
				fileData = mapper.readValue(Files.newInputStream(filePath, StandardOpenOption.READ), FileData.class);
			} catch (IOException ex) {
				throw new RuntimeException("Failed to read file data: " + ex.getMessage(), ex);
			}
		}
	}
}
