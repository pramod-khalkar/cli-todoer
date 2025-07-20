package org.clitodoer.utils;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Pramod Khalkar
 * @since : 20/07/25, Sun
 **/
@UtilityClass
public class Helper {
	public Map<String, List<String>> extractNotesBySection(String text) {
		Map<String, List<String>> sectionNotes = new LinkedHashMap<>();
		String[] sections = text.split("(?=‣\\s)");
		Pattern rowPattern = Pattern.compile("\\|\\s*\\d+\\s*\\|\\s*(.*?)\\s*\\|");
		for (String sectionBlock : sections) {
			if (!sectionBlock.startsWith("‣")) continue;
			String[] lines = sectionBlock.split("\\R", 2);
			String sectionName = lines[0].trim().substring(1).trim(); // Remove '‣'
			Matcher matcher = rowPattern.matcher(sectionBlock);
			List<String> notes = matcher.results()
					.map(m -> m.group(1))
					.toList();
			sectionNotes.put(sectionName, notes);
		}
		return sectionNotes;
	}

	public List<String> extractNotesFromOutput(String output) {
		return Pattern.compile("\\|\\s*\\d+\\s*\\|\\s*(.*?)\\s*\\|")
				.matcher(output)
				.results()
				.map(mr -> mr.group(1))
				.toList();
	}
}
