// Struct with term and TF_IDF(term, doc)

import java.io.IOException;

public class Weight {

	String fileName;
	String term;
	double TFIDF;
	boolean key = false;
	int stPos;
	int endPos;

	public Weight (int st, int end, String f, String t){
		this.stPos = st;
		this.endPos = end;
		this.fileName = f;
		this.term = t;
		this.key = true;
		this.TFIDF = 0;
	}

	public double getTFIDF(){
		return TFIDF;
	}

	public String getTerm(){
		return term;
	}

	public String getFile(){
		return fileName;
	}

	private boolean getKey(){
		return key;
	}

	private Weight(){
		term = "";
		TFIDF = 0;
		fileName = "";
		key = false;
	}

	public Weight(String f, String t, double w, Boolean k){
		term = t;
		TFIDF = w;
		fileName = f;
		key = k;
	}

	public Weight(Weight w){
		//this.(w.getFile(), w.getTerm(), w.getTFIDF(), w.getKey());
		term = w.getTerm();
		TFIDF = w.getTFIDF();
		fileName = w.getFile();
		key = w.getKey();
	}

	public Weight(String str, double w){
		term = str;
		TFIDF = w;
	}

	public Weight(String str, double w, String f){
		this.term = str;
		this.TFIDF = w;
		this.fileName = f;
	}

	public void print(){
		System.out.print(fileName + "  ");
		System.out.print(term + "  ");
		System.out.println(TFIDF);
	}

	public void print(String file) throws IOException {
		try {
			Main.writerKey.write(fileName + "  " + term + "  ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.writerKey.write(String.valueOf(TFIDF));
		Main.writerKey.write("\n");
	}

	public boolean isKeyword(){
		return key;
	}

	public void markKeywords(double minWeight){
		if (TFIDF >= minWeight)
			key = true;
	}

	public boolean equals(Weight w){
		if (this.getTerm() == w.getTerm())
			return true;
		return false;
	}

}
