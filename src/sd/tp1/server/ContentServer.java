package sd.tp1.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;

import org.json.simple.parser.ParseException;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import sd.tp1.proxy.ProxyServer;

@WebService
public class ContentServer {

	private static File basePath;
	private static OAuth20Service service;
	private static OAuth2AccessToken accessToken;
	private ProxyServer proxy;

	static final File KEYSTORE = new File("./server.jks");
	static final char[] JKS_PASSWORD = "changeit".toCharArray();
	static final char[] KEY_PASSWORD = "changeit".toCharArray();

	protected ContentServer() {
		super();
		proxy = new ProxyServer();
	}

	@WebMethod
	public String[] getAlbuns() throws Exception {
		List<String> proxyAlbuns = proxy.getAlbuns(accessToken, service);
		String[] albuns = new String[proxyAlbuns.size()];

		proxyAlbuns.toArray(albuns);
		return albuns;
	}

	@WebMethod
	public String[] getPictures(String path) throws Exception {
		List<String> proxyPictures = proxy.getPictures(accessToken, service, path);
		String[] pictures = new String[proxyPictures.size()];

		proxyPictures.toArray(pictures);
		return pictures;
	}

	@WebMethod
	public byte[] getPictureData(String album, String picture) throws Exception {
		return proxy.getPictureData(accessToken, service, album, picture);
	}

	@WebMethod
	public void createAlbum(String name) throws Exception {
		proxy.createAlbum(accessToken, service, name);
	}

	@WebMethod
	public void deleteAlbum(String album) throws Exception {
		proxy.deleteAlbum(accessToken, service, album);
	}

	@WebMethod
	public void uploudFile(String album, String pic, byte[] data) throws IOException {
		try {
			proxy.uploudPicture(accessToken, service, album, pic, data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@WebMethod
	public boolean deletePicture(String album, String picture) throws Exception {
		return proxy.deletePicture(accessToken, service, album, picture);
	}

	public boolean pingMethod() {
		return true;
	}

	public static void conect() {

		// Substituir pela API key atribuida
		final String apiKey = "4b5822842d6f342";
		;
		// Substituir pelo API secret atribuido
		final String apiSecret = "88558045a3bf71fe45866b58bb04c097bebeb129";

		service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret).build(ImgurApi.instance());
		final Scanner in = new Scanner(System.in);

		// Obtain the Authorization URL
		System.out.println("A obter o Authorization URL...");
		final String authorizationUrl = service.getAuthorizationUrl();
		System.out.println("Necessario dar permissao neste URL:");
		System.out.println(authorizationUrl);
		System.out.println("e copiar o codigo obtido para aqui:");
		System.out.print(">>");
		final String code = in.nextLine();

		// Trade the Request Token and Verifier for the Access Token
		System.out.println("A obter o Access Token!");
		accessToken = service.getAccessToken(code);
	}

	public static void main(String args[]) throws Exception {
		String URL = "http://" + args[1] + ":" + args[2] + "/" + args[3];
		/*
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
		



		String path = args.length > 0 ? args[0] : ".";
		
		
		// Create the HTTPS server using the ssl context.
		HttpsConfigurator configurator = new HttpsConfigurator(ssl);
		HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(args[1], Integer.parseInt(args[2])), -1);
		httpsServer.setHttpsConfigurator(configurator);
		HttpContext httpContext = httpsServer.createContext("/"+args[3]);
		httpsServer.start();
		
		ContentServer impl = new ContentServer();
		Endpoint ep = Endpoint.create( impl);
		ep.publish(httpContext);
		
		
		
		
		String url2 = URL.replace("https", "http").replace("" + Integer.parseInt(args[2]), "" + (Integer.parseInt(args[2])+1));
		//Endpoint.publish(url2, impl);
		
		System.err.println("FileServer started@ " + URL);
		
		*/
		ContentServer impl = new ContentServer();
		Endpoint.publish(URL, new ContentServer());
		
		System.err.println("FileServer started");

		conect();

		final InetAddress address = InetAddress.getByName("224.0.0.1");// Define
																		// endereço
																		// multicast
		MulticastSocket socket = new MulticastSocket(9000);// abre socket com
															// esse enderço na
															// porta 9000
		socket.joinGroup(address);// junta-se ao grupo multicast com esse
									// endereço

		// Recebe do cliente
		byte[] buffer = new byte[65536];

		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			// Responder ao cliente
			byte[] data = URL.getBytes();
			DatagramPacket datagram = new DatagramPacket(data, data.length);// porque
																			// cada
																			// pacote
																			// tem
																			// a
																			// propria
																			// address;
			datagram.setAddress(address);// ficamos com o address e ja podemos
											// fazer o resto.
			datagram.setPort(packet.getPort());
			socket.send(datagram); // quando tiver de mandar para o cliente,
									// temos de mudar para unicast,
		}

	}
}
