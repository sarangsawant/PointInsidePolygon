
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

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
			while (true) {
				try (Socket socket = server.accept()) {

					InputStreamReader isr = new InputStreamReader(socket.getInputStream());
					BufferedReader reader = new BufferedReader(isr);

					String line;
					// read headers
					line = reader.readLine();
					while (!line.isEmpty())
						line = reader.readLine();

					// read body of post request
					char[] params = new char[100];
					reader.read(params);
					line = new String(params).trim();

					Set<String> results = null;
					if (!line.isEmpty()) {
						longitude = Double.parseDouble(line.split("&")[0].split("=")[1].trim());
						latitude = Double.parseDouble(line.split("&")[1].split("=")[1].trim());

						double[] inputCoordinates = new double[2];
						inputCoordinates[0] = longitude;
						inputCoordinates[1] = latitude;

						results = states.findStateForPoint(inputCoordinates);
					} else
						results.add(new String("Please Enter Longitude and Latitude of the point!!!"));

					String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + results.toString();
					socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				}

			}
		} catch (IOException e) {
			System.err.println("Error in reading post request");
			e.printStackTrace();
		}

	}
}
