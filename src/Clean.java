// Clean text from stop-words and punctuation marks

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clean {

	static String result;
	static String[] eng = {"q", "w", "e", "r", "t", "y", "u", "i", "o" ,"p", "a", "s", "d", "f", "®",
							"g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "ї", "ў", "¬"};
	static String[] markL1 = {"\\.", ",", ";", ":", "!", "\\?", "\n",
							"\\{", "}", "\\[", "]", ">", "<", "\\(", "\\)"};
	static String[] wordL2 = {"от", "но", "он", "мы", "вы", "ее", "её", "то",
									"бы", "во", "их", "ой", "эх", "не", "за",
									"же", "на", "по", "со", "из", "до", "да"};
	static String[] wordL3 = {"или", "его", "вам", "вас", "что", "все", "всё", "ого",
								"они", "мне", "для", "без", "над", "под", "при", "нет"};
	static String[] wordL4 = {"дабы", "даже", "весь", "меня"};
	static String[] wordL5 = {"всего", "итого", "браво", "таким", "после",
								"потом", "затем", "может", "ближе", "позже"};
	static String[] wordL6 = {"скажем", "что-то", "где-то", "как-то", "дальше",
												"раньше", "однако", "вообще"};
	static String[] wordL8 = {"извините", "например", "когда-то", "какой-то",
											"допустим", "в общем", "вероятно"};

	// Clean from punctuation marks
	private static String marks(String part) {
		result = part;
		for (String mark : markL1) {
			result = result.replaceAll(mark, " ");
		}
		return result;
	}

	// Clean from stop words
	private static String words(String part) {
		result = part.toLowerCase();
		result = result.replaceAll("\\s[а-яА-Я]\\s", "   ");
		for (String word : wordL2) {
			result = result.replaceAll("\\s" + word + "\\s", "    ");
		}
		for (String word : wordL3) {
			result = result.replaceAll("\\s" + word + "\\s", "     ");
		}
		for (String word : wordL4) {
			result = result.replaceAll("\\s" + word + "\\s", "      ");
		}
		for (String word : wordL5) {
			result = result.replaceAll("\\s" + word + "\\s", "       ");
		}
		for (String word : wordL6) {
			result = result.replaceAll("\\s" + word + "\\s", "        ");
		}
		for (String word : wordL8) {
			result = result.replaceAll("\\s" + word + "\\s", "          ");
		}
		result = result.replaceAll("\\s" + "на самом деле" + "\\s", "               ");
		result = result.replaceAll("\\s" + "честно говоря" + "\\s", "               ");
		result = result.replaceAll("\\s" + "который" + "\\s", "         ");
		result = result.replaceAll("\\s" + "спасибо" + "\\s", "         ");
		result = result.replaceAll("\\s" + "здравствуйте" + "\\s", "              ");
		result = result.replaceAll("\\s" + "лишь только" + "\\s", "             ");
		return result;
	}

	// Clean from marks and stop words
	public static String clean(String part) {
		result = part.replace(".~", ". ");
		result = marks(result);
		result = words(result);
		return result;
	}

	public static List<String> mystemOut(String out){
		List<String> resultList = new ArrayList();
		int start;
		int end;
		int mark;
		String norm;
		start = out.indexOf('{') + 1;
		while(start != 0){
			end = out.indexOf('}');
			if (end < start)
				System.out.println(out);
				//out = out.substring(3);
			end = out.indexOf('}');
			if(end == -1){
				start = 0;
			} else {
				norm = out.substring(start, end);
				mark = norm.indexOf('|');
				if(mark != -1)
					norm = norm.substring(0, mark);
				//if (!resultList.contains(norm))
				resultList.add(norm);
				out = out.substring(end + 1);
				start = out.indexOf('{') + 1;
			}
		}
		return resultList;
	}

	public static String fileToString(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null)
		{
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		// File to string
		return sb.toString();
	}

	public static List<String> normalize(String fileName) throws IOException, InterruptedException {
		Process pr;
		BufferedReader input;
		BufferedWriter writer;
		String doc = Clean.fileToString(fileName);
		doc = Clean.clean(doc);
		doc = Clean.deleteEng(doc);
		writer = new BufferedWriter(new FileWriter("/Users/Catrin/diploma/src/input.txt"));
		writer.write(doc);
		writer.close();
		pr = Runtime.getRuntime().exec(Main.MYSTEM_PATH + " " + Main.MYSTEM_FILE);
		input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String outMysteam = input.readLine();
		pr.waitFor();
		input.close();
		if(outMysteam == null){
			//System.out.println("GOVNO");
			List<String> c = new ArrayList<String>();
			//c.add("пустотища");
			return c;
		}
		return Clean.mystemOut(outMysteam);
	}

	public static List<List<String>> normalize(List<File> files) throws IOException, InterruptedException {
		Process pr;
		BufferedReader input;
		if (Main.IS_NEW_MYSTEM) {
			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(Main.MYSTEM_FILE));
			for (File fileName : files) {
				String doc = Clean.fileToString(fileName.toString());
				doc = Clean.clean(doc);
				doc = Clean.deleteEng(doc);
				writer.write(doc);
				writer.write(" NEWLINE ");
			}
			writer.close();
		}
		pr = Runtime.getRuntime().exec("/Users/Catrin/Downloads/mystem /Users/Catrin/diploma/src/input.txt");
		input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = input.readLine();
		List<List<String>> outMysteam = new ArrayList<>();
		line = line.replaceAll("NEWLINE??", ";");
		line = line.replaceAll("NEWLINE", "");
		List<String> c = Arrays.asList(line.split(";"));
		List<String> stringList = new ArrayList<>();
		for (String str : c) {
			if (!str.equalsIgnoreCase("E{")){
				if (str.length() > 4){
					if (str.substring(0,4).equalsIgnoreCase("E??}"))
						stringList.add(str.substring(4));
				} else
					if (!str.equalsIgnoreCase("E??}"))
						stringList.add(str);
			}
		}
		for (String str : stringList) {
			c = Clean.mystemOut(str);
			if (!c.isEmpty())
				outMysteam.add(c);
		}
		pr.waitFor();
		input.close();
		return outMysteam;
	}

	public static String deleteEng(String doc){
		result = doc;
		//System.out.println(doc);
		for (String c : eng) {
			result = result.replaceAll(c, "");
		}
		result = result.replaceAll("%", "");
		result = result.replaceAll("@", "");
		result = result.replaceAll("\\*", "");
		result = result.replaceAll("[0-9]", "");
		result = result.replaceAll("\\|", "");
		result = result.replaceAll("\\$", "");
		result = result.replaceAll("_", "");
		result = result.replaceAll("~", "");
		result = result.replaceAll("\\+", "");
		result = result.replaceAll("=", "");
		result = result.replaceAll("\\\\", "");
		result = result.replaceAll("\\^", "");
		while (result.contains("--"))
			result = result.replaceAll("--", "-");
		while (result.contains("  "))
			result = result.replaceAll("  ", " ");
		return result;
	}

	public List<Weight> dictToList (String dictionary){
		List<Weight> l = new ArrayList<>();
		int start = dictionary.indexOf("<file name:");
		int end;
		while (start > -1){
			int eof = dictionary.indexOf("</file>");
			end = dictionary.substring(start + 20).indexOf(">");
			String fileName = dictionary.substring(start + 20, start + end + 19);
			String termPart = dictionary.substring(start + end + 22, eof);
			dictionary = dictionary.substring(eof + 8);
			int st = termPart.indexOf("<item>");
			while (st > -1){
				int fin = termPart.indexOf("</item>");
				int pos1 = termPart.substring(st + 7, fin - 1).indexOf("<startpos>") + 10;
				int pos2 = termPart.substring(st + 7, fin - 1).indexOf("</startpos>");
				int stPos = Integer.parseInt(termPart.substring(st + 7, fin - 1).substring(pos1, pos2));
				pos1 = termPart.substring(st + 7, fin - 1).indexOf("<endpos>") + 8;
				pos2 = termPart.substring(st + 7, fin - 1).indexOf("</endpos>");
				int endPos = Integer.parseInt(termPart.substring(st + 7, fin - 1).substring(pos1, pos2));
				pos1 = termPart.substring(st + 7, fin - 1).indexOf("<linktext>") + 10;
				pos2 = termPart.substring(st + pos1 + 7, fin - 1).indexOf("<linktext") + st + pos1 - 1;
				String term = termPart.substring(st + 7, fin - 1).substring(pos1, pos2);
				termPart = termPart.substring(fin + 8);
				l.add(new Weight(stPos, endPos, fileName, term));
				st = termPart.indexOf("<item>");
			}
			start = dictionary.indexOf("<file name:");
		}
		return l;
	}

}
