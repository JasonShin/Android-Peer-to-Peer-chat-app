import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientConnectionHolder {

	private Socket connection;
	private ObjectOutputStream ouput;
	private ObjectInputStream input;
	
	public ClientConnectionHolder(Socket c, ObjectOutputStream o, ObjectInputStream i){
		this.connection = c;
		this.ouput = o;
		this.input = i;
	}
	
	public Socket getConnection() {
		return connection;
	}
	public ObjectOutputStream getOuput() {
		return ouput;
	}
	public ObjectInputStream getInput() {
		return input;
	}
	
	
	
	
}
