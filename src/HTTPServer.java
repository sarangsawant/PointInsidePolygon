
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HTTPServer {
	
	public static void main(String[] args) {
		final ServerSocket server;
		
		try {
			server = new ServerSocket(8080);
			System.out.println("Listeneing on 8080");
			
			UnitedStates states = new UnitedStates();
			states.populateStatesMetadata();

			while(true) {
				try (Socket socket = server.accept()) {
					
					InputStreamReader isr = new InputStreamReader(socket.getInputStream());
					BufferedReader reader = new BufferedReader(isr); 
					String line = reader.readLine(); 
					while (!line.isEmpty()) {
						System.out.println(line);
						line = reader.readLine();
					}

					/*List<double[]> list = new ArrayList<>();
					
					double[] input = {-77.036133,40.513799};
					list.add(input);
					
					double[] input1 = {-121.66522, 38.169285};
					list.add(input1);
					
					double[] input2 = {-100.051535, 38.998918};
					list.add(input2);
					
					double[] input3 = {0.0,0.0};
					list.add(input3);
					*/

					//for(double[] arr : list)
					double[] inputCoordinates = new double[2];
					inputCoordinates[0] = -77.036133;
					inputCoordinates[0] = 40.513799;

					String[] result = new String[1];
					result[0] = states.findStateForPoint(inputCoordinates);
					
					String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + result.toString();

					socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
					}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
