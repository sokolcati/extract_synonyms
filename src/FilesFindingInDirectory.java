// Create list of all TEX-files from specified directory

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilesFindingInDirectory {

	// resultant list
	static ArrayList<File> al = new ArrayList<File>();
	static File fileLocation = null;

	// Start iteration
	public static void listing() throws IOException {
		File filePath = new File(Main.DOC_PATH);
		File[] listingAllFiles = filePath.listFiles();
		iterateOverFiles(listingAllFiles);
	}

	// Goes into directories and adds files to list
	public static ArrayList<File> iterateOverFiles(File[] files) {
		if (files == null) {
			return null;
		}
		for (File file : files) {
			if (file.isDirectory()){
				iterateOverFiles(file.listFiles()); // Calls same method again
			} else {
				fileLocation = findFileswithTxtExtension(file);
				if(fileLocation != null) {
					// System.out.println(fileLocation);
					// Add to list
					al.add(fileLocation);
				}
			}
		}
		return al;
	}

	// Check if extension is exactly "tex"
	public static File findFileswithTxtExtension(File file) {
		if(file.getName().toLowerCase().endsWith("tex")) {
			return file;
		}
		return null;
	}

	// Read and partition file
	public static List<String> partitioning(String fileName) throws IOException {
		// list of file sections
		List<String> parts = new ArrayList<String>();

		// Read file to buffer
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			// File to string
			String everything = sb.toString();

			// constant array of keywords that start new sections
			final String[] startSection = {"\\Lit", "\\section"/*, "\\abstract", "\\shorttitle"*/, "\\Title"};
			// list of indexes that start new sections
			List<Integer> indexSection = new ArrayList<Integer>();

			// Filling indexSection
			for (String start : startSection) {
				int n = everything.indexOf(start);
				if (!indexSection.contains(n)) {
					indexSection.add(n);
				}
				int m = everything.substring(n + 1).indexOf(start);
				while (m > 0) {
					n = n + m + 1;
					if (!indexSection.contains(n)) {
						indexSection.add(n);
					}
					m = everything.substring(n + 1).indexOf(start);
				}
			}

			// Sort indexSection
			Collections.sort(indexSection);

			// Filling parts
			int i = 0;
			for (int n : indexSection) {
				String part = everything.substring(i, n - 1);
				i = n;
				parts.add(part);
			}
			br.close();
			return parts;
		} finally {
			br.close();
			return parts;
		}
	}
}
