
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
			System.out.println("Listening on port 8080");
			
			double longitude = 0;
			double latitude = 0;
			
			UnitedStates states = new UnitedStates();
			states.populateStatesMetadata();

			while(true) {
				try (Socket socket = server.accept()) {
					
					InputStreamReader isr = new InputStreamReader(socket.getInputStream());
					BufferedReader reader = new BufferedReader(isr); 
					String line = reader.readLine(); 
					
					while (!line.isEmpty()) {
						line = reader.readLine();
						
						if(line.contains("longitude"))
							longitude = Double.parseDouble(line.split(":")[1].trim());
						
						if(line.contains("latitude"))
							latitude = Double.parseDouble(line.split(":")[1].trim());
						
					}

					
					double[] inputCoordinates = new double[2];
					inputCoordinates[0] = longitude;
					inputCoordinates[1] = latitude;
					System.out.println(inputCoordinates[0] + " -- " + inputCoordinates[1]);

					String result = states.findStateForPoint(inputCoordinates);
					
					String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + result;

					socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
					}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
