// Main class for keyword and synonym extracting

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static final String DOC_PATH = "/Users/Catrin/Практика/stats/all_TEX";
	public static final String TERM_MARKUP = "/Users/Catrin/Практика/stats/out.txt";
	public static final String MYSTEM_FILE = "/Users/Catrin/diploma/src/input.txt";
	public static final String MYSTEM_PATH = "/Users/Catrin/Downloads/mystem";

	public static final Boolean IS_NEW_MYSTEM = false;
	public static final String JUST_ONE_TERM = "";
	public static final String SAVE_KEYWORDS = "";
	public static BufferedWriter writerKey = null;

	public static void main(String[] args) throws IOException, InterruptedException {

		if (!SAVE_KEYWORDS.equalsIgnoreCase("")){
			writerKey = new BufferedWriter(new FileWriter(SAVE_KEYWORDS));
		}

		FilesFindingInDirectory FilesStructure = new FilesFindingInDirectory();
		Clean Clean = new Clean();
		TF_IDF TF_IDF = new TF_IDF();
		CohensKappa k = new CohensKappa();
		String kwDictionary = Clean.fileToString(TERM_MARKUP);
		System.out.println("Start working");
		FilesStructure.listing(); // store passes to files into list al
		//List<String> parts;
		//int kkk = 0;
		List<List<String>> Files = Clean.normalize(FilesFindingInDirectory.al);
		int fileNum = 0;
		double minWeight = 100;
		double maxWeight = -1;
		List<Weight> termFiles = new ArrayList<Weight>();
		List<Weight> keyFiles = new ArrayList<Weight>();
		for (List<String> doc : Files) {
			int numTerm = 0;
			List<Weight> termDoc = new ArrayList<Weight>();
			List<String> terms = new ArrayList<>();
			for (String term : doc) {
				if (!terms.contains(term)) {
					terms.add(term);
					termDoc.add(numTerm, new Weight(term, TF_IDF.TF_IDF(term, doc, Files), FilesFindingInDirectory.al.get(fileNum).getName()));
					numTerm++;
				}
			}
			fileNum++;
			for (Weight w : termDoc){
				//w.print();
				if (minWeight > w.getTFIDF())
					minWeight = w.getTFIDF();
				if (maxWeight < w.getTFIDF())
					maxWeight = w.getTFIDF();
			}
			termFiles.addAll(termDoc);
		}

		System.out.print("MIN TF-IDF : ");
		System.out.print(minWeight);
		System.out.print("    MAX TF-IDF : ");
		System.out.println(maxWeight);

		if (JUST_ONE_TERM.equalsIgnoreCase("")){
			List<Weight> termFiles2 = Clean.dictToList(kwDictionary);
			int summ1 = termFiles2.size(); // x1+y1
			double wKey = minWeight;

			Weight last = null;
			int first = 0;
			for (Weight w : termFiles) {
				w.markKeywords(wKey);
				if (first > 0) {
					if (w.isKeyword()) {
						if (last != null) {
							if (last.isKeyword()) {
								if (last.getFile().equalsIgnoreCase(w.getFile())) {
									String term = w.getTerm() + " " + last.getTerm();
									last = new Weight(w.getFile(), term, 0, true);
								} else {
									keyFiles.add(last);
									last = new Weight(w);
								}
							} else
								last = new Weight(w);
						} else
							last = new Weight(w);
					} else {
						if (last != null)
							if (last.isKeyword()) {
								keyFiles.add(last);
							}
						last = new Weight(w);
					}
				} else
					first++;
			}

			CohensKappa CK = new CohensKappa();
			System.out.print("x1 + x2 + x3 + x4 = ");
			int summ = termFiles.size(); // x1+x2+y1+y2
			System.out.println(summ);
			System.out.print("x1 + x2 = ");
			int summ2 = keyFiles.size(); // x1+x2
			System.out.println(summ2);
			System.out.println("KAPPA");
			int x1 = CK.getX1(termFiles2, keyFiles);
			System.out.print("x1 = ");
			System.out.println(x1);
			System.out.print("k = ");
			System.out.println(CK.stat(x1, summ2 - x1, summ1 - x1, summ - summ1 - summ2 + x1));

			for (Weight w : termFiles) {
				w.markKeywords(1000);
				if (!SAVE_KEYWORDS.equalsIgnoreCase("")) {
					if (w.isKeyword())
						w.print(SAVE_KEYWORDS);
				}
			}

		} else {
			for (Weight w : termFiles){
				if (w.getTerm().equalsIgnoreCase(JUST_ONE_TERM)){
					w.print();
				}
			}
		}

		if (!SAVE_KEYWORDS.equalsIgnoreCase("")){
			writerKey.close();
		}
	}
}
