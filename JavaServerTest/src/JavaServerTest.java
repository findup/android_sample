import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaServerTest {

	private static final int PORT = 1234;
	private static Logger _logger;
	
	/**
	 * ‚ß‚¢‚ñ
	 * @param args
	 */
	public static void main(String args[]) {
		
		ServerSocket serversocket;
		
		_logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		_logger.info("main()");
		
		try {
			serversocket = new ServerSocket(PORT);
			
			Socket acceptedSocket = serversocket.accept();
			_logger.info("accepted");

			ConnectionThread cn = new ConnectionThread(acceptedSocket);
			cn.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static class ConnectionThread extends Thread {

		Socket _socket;
		
		public ConnectionThread(Socket socket){
			_socket = socket;
		}
		
		@Override
		public void run() {
			try {
				_logger.info("ConnectionThread exeucute.");
				
				InputStream inputStream = _socket.getInputStream();
	//			OutputStream outputStream = _socket.getOutputStream();
				
//				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
							
//				CharBuffer cb = CharBuffer.allocate(1024 * 10);
				byte[] buffer = new byte[10000];
				
//?				while (reader.ready()) {
				int r;
				while ((r = inputStream.read(buffer)) != -1) {
//					int r = reader.read(cb);
					_logger.info(Integer.toString(r));
					
//					if (r == -1) {
//						// EOF
//						_logger.info("End of stream.");
//						
//						break;
//					}
				}
				
				_logger.log(Level.INFO, "terminate thread.");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			super.run();
		}
	}
}
