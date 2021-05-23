// Count Cohen's Kappa

import java.util.List;

public class CohensKappa {

	private double p_0(int x1, int x2, int y1, int y2){
		double summ = x1 + x2 + y1 + y2;
		return (x1 + y2) / summ;
	}

	private double p_y(int x1, int x2, int y1, int y2){
		double s = x1 + x2 + y1 + y2;
		System.out.print("p_y = ");
		double py = (x1 + y2) * (x1 + y1) / (s * s);
		System.out.println(py);
		return py;
	}

	private double p_n(int x1, int x2, int y1, int y2){
		double s = x1 + x2 + y1 + y2;
		System.out.print("p_y = ");
		double pn = (y1 + y2) * (x2 + y2) / (s * s);
		System.out.println(pn);
		return pn;
	}

	private double p_e(int x1, int x2, int y1, int y2){
		return p_y(x1, x2, y1, y2) + p_n(x1, x2, y1, y2);
	}

	public double stat(int x1, int x2, int y1, int y2){
		double er = p_e(x1, x2, y1, y2);
		System.out.print("p_e = ");
		System.out.print(er);
		double p0 = p_0(x1, x2, y1, y2);
		System.out.print("   p_0 = ");
		System.out.println(p0);
		return (p0 - er) / (1 - er);
	}

	public int getX1(List<Weight> dict1, List<Weight> dict2){
		int x1 = 0;
		for (Weight w1 : dict1){
			for (Weight w2 : dict2){
				if (w1.getFile().equalsIgnoreCase(w2.getFile())){
					if ((w1.getTerm().length() > 2) & (w2.getTerm().length() > 2)){
						if (w1.getTerm().substring(0, 3).equalsIgnoreCase(w2.getTerm()
								.replaceAll("\\?", "").substring(0, 3))){
							x1++;
						}
					}
				}
			}
		}
		return x1;
	}

}
