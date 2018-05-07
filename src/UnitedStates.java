
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnitedStates {

	// Map for state name and its longitudes and latitudes
	Map<String, List<double[]>> stateCoordinatesMap = new HashMap<String, List<double[]>>();

	/**
	 * This function parses each line from input file and inserts it into
	 * stateCoordinatesMap
	 * 
	 * @param line
	 */
	private void parseLine(String line) {
		String[] stateDetails = line.replaceAll("\\{", "").replaceAll("\\}", "").split(":");

		String stateName = stateDetails[1].split(",")[0].replace("\"", "").trim();

		String[] points = stateDetails[2].replaceAll("\\[", "").replaceAll("\\]", "").split(",");

		List<double[]> coordinates = new ArrayList<>();
		for (int i = 0; i < points.length; i = i + 2) {
			double[] singlePoint = new double[2];

			singlePoint[0] = Double.parseDouble(points[i].trim());
			singlePoint[1] = Double.parseDouble(points[i + 1].trim());

			coordinates.add(singlePoint);
		}

		stateCoordinatesMap.put(stateName, coordinates);
	}

	/**
	 * This function reads states.json file and populates stateCoordinatesMap
	 */
	public void populateStatesMetadata() {
		File file = new File("src/states.json");

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				parseLine(st);
			}

		} catch (FileNotFoundException e) {
			System.err.println("File states.json not found on the specified location");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error in reading file!!");
			e.printStackTrace();
		}

	}

	/**
	 * This function iterates over all states to find if point belongs to that
	 * state
	 * 
	 * @param pointToFind
	 *            (pointToFind[0] -> longitude and pointToFind[1]->latitude)
	 * @return
	 */
	public Set<String> findStateForPoint(double[] pointToFind) {
		String errorStr = "The point {" + pointToFind[0] + "," + pointToFind[1] + "} does NOT belong to any US state";

		Set<String> results = new HashSet<String>();
		if (pointToFind[0] > 0 || pointToFind[1] < 0) {
			results.add(errorStr);
			return results;
		}

		for (Map.Entry<String, List<double[]>> state : stateCoordinatesMap.entrySet()) {
			List<double[]> coordinates = state.getValue();

			double[] extremePoint = { Double.MAX_VALUE, pointToFind[1] };
			int count = 0;

			for (int i = 0; i < coordinates.size() - 1; i++) {
				if (checkForIntersection(coordinates.get(i), coordinates.get(i + 1), pointToFind, extremePoint)) {

					if (checkForOrientation(coordinates.get(i), pointToFind, coordinates.get(i + 1)) == 0) {

						if (checkForPointsOnSegment(coordinates.get(i), pointToFind, coordinates.get(i + 1)))
							results.add(state.getKey());
					}

					count++;
				}
			}

			// if number of intersections are odd, point belongs to that state
			if (count % 2 == 1) {
				results.add(state.getKey());
				System.out.println("count " + count);
			}
		}

		if (results.size() == 0)
			results.add(errorStr);

		return results;
	}

	/**
	 * This function check if line segment p1-q1 intersect with segment p2-q2
	 * 
	 * @param p1
	 * @param q1
	 * @param p2
	 * @param q2
	 * @return
	 */
	private boolean checkForIntersection(double[] p1, double[] q1, double[] p2, double[] q2) {
		int o1 = checkForOrientation(p1, q1, p2);
		int o2 = checkForOrientation(p1, q1, q2);
		int o3 = checkForOrientation(p2, q2, p1);
		int o4 = checkForOrientation(p2, q2, q1);

		if (o1 != o2 && o3 != o4)
			return true;

		// p1, q1 and p2 are colinear and p2 lies on segment p1-q1
		if (o1 == 0 && checkForPointsOnSegment(p1, p2, q1))
			return true;

		// p1, q1 and p2 are colinear and q2 lies on segment p1-q1
		if (o2 == 0 && checkForPointsOnSegment(p1, q2, q1))
			return true;

		// p2, q2 and p1 are colinear and p1 lies on segment p2-q2
		if (o3 == 0 && checkForPointsOnSegment(p2, p1, q2))
			return true;

		// p2, q2 and q1 are colinear and q1 lies on segment p2-q2
		if (o4 == 0 && checkForPointsOnSegment(p2, q1, q2))
			return true;

		return false; // If segments do not intersect
	}

	/**
	 * This function checks for orientation of 3 points by finding slope of each
	 * line segment
	 * 
	 * @param p
	 * @param q
	 * @param r
	 * @return 0-> if all points are colinear, 1->if orientation is clockwise
	 *         and 2->if orientation is anti clockwise
	 */
	private int checkForOrientation(double[] p, double[] q, double[] r) {
		double value = ((q[1] - p[1]) * (r[0] - q[0])) - ((q[0] - p[0]) * (r[1] - q[1]));

		if (value == 0.0)
			return 0;

		return (value > 0) ? 1 : 2;
	}

	/**
	 * This function check of point q lies on line segment p-r
	 */
	private boolean checkForPointsOnSegment(double[] p, double[] q, double[] r) {
		if (q[0] <= Math.max(p[0], r[0]) && q[0] >= Math.min(p[0], r[0]) && q[1] <= Math.max(p[1], r[1])
				&& q[1] >= Math.min(p[1], r[1]))
			return true;

		return false;
	}
}
