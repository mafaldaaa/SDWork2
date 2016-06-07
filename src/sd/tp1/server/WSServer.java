package sd.tp1.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@WebService
public class WSServer {

	//WS Variables
	private static File basePath;

	//SSL variables
	static final File KEYSTORE = new File("./server.jks");
	static final char[] JKS_PASSWORD = "changeit".toCharArray();
	static final char[] KEY_PASSWORD = "changeit".toCharArray();

	public WSServer() {
		this(".");
	}
	
	/**
	 * @param pathname - directoria.
	 */
	protected WSServer(String pathname) {
		super();
		basePath = new File(pathname);
	}

	public boolean isProxy(){
		return false;
	}

	/** Devolves os Albuns da directoria*/
	@WebMethod
	public String[] getAlbuns() throws Exception {

		File f = new File(basePath, ".");
		if (f.exists()) {
			if (f.isDirectory()) {
				return f.list();
			} else
				throw new Exception("Directory not found");
		} else
			throw new Exception("File not found");
	}

	/** Devolve as imagens do Album*/
	@WebMethod
	public String[] getPictures(String album) throws Exception {

		File f = new File(basePath, album);
		if (f.exists()) {
			if (f.isDirectory()) {
				File[] files = f.listFiles(new FilenameFilter() {
					public boolean accept(File f, String name) {
						return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")
								|| name.toLowerCase().endsWith(".jpeg");
					}
				});
				String[] nameFiles = new String[files.length];
				for (int i = 0; i < files.length; i++) {
					nameFiles[i] = files[i].getName();
				}
				return nameFiles;
			} else
				throw new Exception("Directory not found");
		} else
			throw new Exception("File not found");
	}

	/**Devolve o data da imagem*/
	@WebMethod
	public byte[] getPictureData(String album, String picture) throws Exception {

		File f = new File(basePath + "/" + album + "/" + picture);
		byte[] pic = new byte[(int) f.length()];

		if (f.exists()) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			bis.read(pic, 0, pic.length);
			bis.close();
			return pic;
		} else
			throw new Exception("File not found");
	}

	/**Cria um album na directoria*/
	@WebMethod
	public void createAlbum(String name) throws Exception {

		File f = new File(basePath + "/" + name);

		if (!f.exists()) {
			f.mkdirs();
		} else
			throw new Exception("Album already exists");
	}

	/**Apaga o album da directoria*/
	@WebMethod
	public void deleteAlbum(String album) throws Exception {

		File f = new File(basePath + "/" + album);
		if (f.exists()) {
			if (!f.isFile()) {
				f.delete();
			} else
				throw new Exception("is a File");
		} else
			throw new Exception("Album not exists");
	}

	/**Uploud uma imagem para o album da directoria*/
	@WebMethod
	public void uploudFile(String album, String pic, byte[] data) throws IOException {

		File f = new File(basePath + "/" + album + "/" + pic);
		FileOutputStream output = new FileOutputStream(f);
		output.write(data);
		output.close();
	}

	/**Apaga a imagem do album da directoria*/
	@WebMethod
	public boolean deletePicture(String album, String picture) throws Exception {

		File f = new File(basePath + "/" + album + "/" + picture);
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
				return true;
			} else
				throw new Exception("is not file");
		} else {
			throw new Exception("File not exists");
		}
	}

	public boolean pingMethod() {
		return true;
	}

	public static void main(String args[]) throws Exception {

		String path = args.length > 0 ? args[0] : ".";
		String URL = "http://" + args[1] + ":" + args[2] + "/" + args[3];

		// Load and initialize the server's java keystore.
		KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore store = KeyStore.getInstance("JKS");
		try (FileInputStream fis = new FileInputStream(KEYSTORE)) {
			store.load(fis, JKS_PASSWORD);
			keyFactory.init(store, KEY_PASSWORD);
		}

		// Prepare the server's trust manager
		TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(store);

		// Create and initialize the ssl context.
		SSLContext ssl = SSLContext.getInstance("TLS");
		ssl.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), new SecureRandom());

		// Create the HTTPS server using the ssl context.
		HttpsConfigurator configurator = new HttpsConfigurator(ssl);
		HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(args[1], Integer.parseInt(args[2])), -1);
		httpsServer.setHttpsConfigurator(configurator);
		HttpContext httpContext = httpsServer.createContext("/" + args[3]);
		httpsServer.start();

		WSServer impl = new WSServer(path);
		Endpoint ep = Endpoint.create( impl);
		ep.publish(httpContext);


		// Só para quando WS import descomentar wsimport -keep -s src -d bin -p sd.tp1.client.ws http://serverAddress/server?wsdl
		//String url2 = URL.replace("https", "http").replace("" + Integer.parseInt(args[2]), "" + (Integer.parseInt(args[2])+1));
		//Endpoint.publish(url2, impl);

		System.err.println("FileServer started@ " + URL);

		final InetAddress address = InetAddress.getByName("224.0.0.1");
		MulticastSocket socket = new MulticastSocket(9000);
		socket.joinGroup(address);

		/* Recebe do cliente */
		byte[] buffer = new byte[65536];

		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			URL = URL + "!ws";
			byte[] data = URL.getBytes();
			DatagramPacket datagram = new DatagramPacket(data, data.length);
			datagram.setAddress(address);
			datagram.setPort(packet.getPort());
			socket.send(datagram); 
		}
	}
}
