
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitedStates {

	Map<String, List<double[]>> stateCoordinatesMap = new HashMap<String, List<double[]>>();
	
	private void parseLine(String line) {
		String[] stateDetails = line.replaceAll("\\{", "").replaceAll("\\}","").split(":");
		
		String stateName = stateDetails[1].split(",")[0].replace("\"", "").trim();
		
		String[] points = stateDetails[2].replaceAll("\\[", "").replaceAll("\\]","").split(",");
		
		List<double[]> coordinates = new ArrayList<>();
		for(int i=0; i<points.length; i= i+2) {
			double[] singlePoint = new double[2];
			
			singlePoint[0] = Double.parseDouble(points[i].trim());
			singlePoint[1] = Double.parseDouble(points[i+1].trim());
			
			coordinates.add(singlePoint);
		}
		stateCoordinatesMap.put(stateName, coordinates);
	}
	
	private void print() {
		stateCoordinatesMap.forEach((k,v) -> {
				System.out.println("state ->" + k);
				for(double[] d : v) {
					System.out.println(d[0] + " " + d[1]);
				}
		});
	}
	public void populateStatesMetadata() {
		File file = new File("states.json");
		 
		  BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				parseLine(st);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//print();
		
	}		
	
	public String findStateForPoint(double[] pointToFind) {
		String result = "The point does not belong to any state";
		
		if(pointToFind[0] > 0 || pointToFind[1] < 0)
			return result;
		
		for(Map.Entry<String, List<double[]>> state : stateCoordinatesMap.entrySet()) {
			List<double[]> coordinates = state.getValue();
			
			double[] extreme = {Double.MAX_VALUE,pointToFind[1]};
			int count = 0;
			
			for(int i=0;i<coordinates.size()-1;i++) {
				if(checkForIntersection(coordinates.get(i),coordinates.get(i+1),pointToFind,extreme)) {
					
					if(checkForOrientation(coordinates.get(i),pointToFind, coordinates.get(i+1)) == 0) {
						
						if (checkForPointsOnSegment(coordinates.get(i),pointToFind, coordinates.get(i+1)))
							return state.getKey();
					}
					
					count++;
				}
				
			}
			
			if(count%2 == 1) {
				result = state.getKey();
				System.out.println("Intersection count " + count);
				return result;
			}
		}
		return result;
	}
	
	private int checkForOrientation(double[] p, double[] q, double[] r) {
		double value = ((q[1]-p[1]) * (r[0]-q[0])) - ((q[0] - p[0]) * (r[1]-q[1]));
		
		if(value == 0.0)
			return 0;
		
		return (value > 0) ? 1 : 2;
	}
	
	private boolean checkForIntersection(double[] p1, double[] q1, double[] p2, double[] q2) {
		// Find the four orientations needed for general and
	    // special cases
	    int o1 = checkForOrientation(p1, q1, p2);
	    int o2 = checkForOrientation(p1, q1, q2);
	    int o3 = checkForOrientation(p2, q2, p1);
	    int o4 = checkForOrientation(p2, q2, q1);
	    
	    // General case
	    if (o1 != o2 && o3 != o4)
	        return true;
	 
	    // Special Cases
	    // p1, q1 and p2 are colinear and p2 lies on segment p1q1
	    if (o1 == 0 && checkForPointsOnSegment(p1, p2, q1)) return true;
	 
	    // p1, q1 and p2 are colinear and q2 lies on segment p1q1
	    if (o2 == 0 && checkForPointsOnSegment(p1, q2, q1)) return true;
	 
	    // p2, q2 and p1 are colinear and p1 lies on segment p2q2
	    if (o3 == 0 && checkForPointsOnSegment(p2, p1, q2)) return true;
	 
	     // p2, q2 and q1 are colinear and q1 lies on segment p2q2
	    if (o4 == 0 && checkForPointsOnSegment(p2, q1, q2)) return true;
	 
	    return false; // Doesn't fall in any of the above cases
	}
	
	// Given three colinear points p, q, r, the function checks if
	// point q lies on line segment 'pr'
	private boolean checkForPointsOnSegment(double[] p, double[] q, double[] r) {
		if (q[0] <= Math.max(p[0], r[0]) && q[0] >= Math.min(p[0], r[0]) &&
	            q[1] <= Math.max(p[1], r[1]) && q[1] >= Math.min(p[1], r[1]))
	        return true;
		
	    return false;
	}
}
