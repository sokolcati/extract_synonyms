// Count TF, IDF and TF/IDF

import java.io.*;
import java.util.List;

public class TF_IDF {

	static int result;

	// Count amount of word term in text
	private static int count(String term, List<String> doc) {
		result = 0;
		for (String word : doc) {
			if (term.equals(word))
				result++;
		}
		return result;
	}

	// Count TF
	private static double TF(String term, List<String> docList) {
		double result = 0;
		for (String word : docList) {
			result++;
		}
		if(result >= 1) {
			return count(term, docList) / result;
		}
		return 0;
	}

	// Count IDF
	private static double IDF(String term, List<List<String>> allFiles) throws IOException, InterruptedException {
		double countFiles = 0;
		double termFiles = 0;
		for (List<String> outDoc : allFiles){
			if (outDoc != null){
				countFiles++;
				if (outDoc.contains(term))
					termFiles++;
			}
		}
		return countFiles / termFiles;
	}

	// Count TF/IDF
	public static double TF_IDF(String term, List<String> docList, List<List<String>> allFiles) throws IOException, InterruptedException {
		return TF(term, docList) * IDF(term, allFiles);
	}
}
