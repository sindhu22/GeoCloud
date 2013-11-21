import java.util.ArrayList;
import java.util.StringTokenizer;
//Authors Abhijeet,Dhananjay,Kumar,Sindhu

public class knn {

	public static float calc5nearNeighbours(String base, ArrayList<String> old,
			ArrayList<Float> oldAvgs, ArrayList<ArrayList<Float>> nearest5Matches) {

		Float baseTempVarSum = 0f;
		ArrayList<Float> nearest5sums = new ArrayList<Float>();
		ArrayList<Float> nearest5nextDayVars = new ArrayList<Float>();
		
		StringTokenizer baseVals = new StringTokenizer(base);
		int baseYearSamples = baseVals.countTokens();
		while (baseVals.hasMoreElements()) {
			baseTempVarSum += Float.parseFloat(baseVals.nextToken());
		}
		for (int j = 0; j < old.size(); j++) {
			String str = old.get(j);
			StringTokenizer oldVals = new StringTokenizer(str);
			Float[] oldTempArr = new Float[oldVals.countTokens()];
//			System.out.println("****" + oldVals.countTokens());
			int k = 0;
			while (oldVals.hasMoreElements()) {
				oldTempArr[k] = Float.parseFloat(oldVals.nextToken());
				k++;
			}
			// System.out.println(baseYearSamples);
			for (int l = 0; l < oldTempArr.length - baseYearSamples; l++) {

				float tempSum = 0f;
				ArrayList<Float> tempArr = new ArrayList<Float>();

				for (int m = l; m < l + baseYearSamples; m++) {
					tempSum += oldTempArr[m];
					tempArr.add(oldTempArr[m]+oldAvgs.get(j));
				}

				int size = nearest5sums.size();

				if (size < 1) {

					nearest5sums.add(tempSum);
					nearest5nextDayVars.add(oldTempArr[l + baseYearSamples]);
					nearest5Matches.add(tempArr);

				} else if (size < 5) {
					nearest5sums.add(tempSum);
					nearest5nextDayVars.add(oldTempArr[l + baseYearSamples]);
					nearest5Matches.add(tempArr);

					int p = nearest5sums.size();

					while (p > 1
							&& nearest5sums.get(p - 1) < nearest5sums
									.get(p - 2)) {

						float temp_var = nearest5sums.get(p - 1);
						float temp_var1 = nearest5nextDayVars.get(p - 1);
						ArrayList<Float> temp_var2 = nearest5Matches.get(p - 1);
						nearest5sums.set(p - 1, nearest5sums.get(p - 2));
						nearest5nextDayVars.set(p - 1,
								nearest5nextDayVars.get(p - 2));
						nearest5Matches.set(p - 1, nearest5Matches.get(p - 2));
						nearest5sums.set(p - 2, temp_var);
						nearest5nextDayVars.set(p - 2, temp_var1);
						nearest5Matches.set(p - 2, temp_var2);
						p--;

					}

				} else {
					if (tempSum < nearest5sums.get(size - 1)) {
						nearest5sums.set(size - 1, tempSum);
						nearest5nextDayVars.set(size - 1, oldTempArr[l
								+ baseYearSamples]);
						nearest5Matches.set(size - 1, tempArr);

						int p = nearest5sums.size();
						while (p > 1
								&& nearest5sums.get(p - 1) < nearest5sums
										.get(p - 2)) {

							float temp_var = nearest5sums.get(p - 1);
							float temp_var1 = nearest5nextDayVars.get(p - 1);
							ArrayList<Float> temp_var2 = nearest5Matches
									.get(p - 1);
							nearest5sums.set(p - 1, nearest5sums.get(p - 2));
							nearest5nextDayVars.set(p - 1,
									nearest5nextDayVars.get(p - 2));
							nearest5Matches.set(p - 1,
									nearest5Matches.get(p - 2));
							nearest5sums.set(p - 2, temp_var);
							nearest5nextDayVars.set(p - 2, temp_var1);
							nearest5Matches.set(p - 2, temp_var2);
							p--;

						}
					}
				}

			}
		}
//		System.out.println(nearest5sums.size() + " REMO "
//				+ nearest5nextDayVars.size());
		float sum = 0f;
		for (int g = 0; g < nearest5sums.size(); g++) {
			sum += nearest5nextDayVars.get(g);
//			System.out.println(nearest5sums.get(g) + " REMO "
//					+ nearest5nextDayVars.get(g));
		}
		float knnVal = sum / nearest5sums.size();
		return knnVal;
	}
}
