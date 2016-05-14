package sd.tp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import sd.tp1.client.ws.ContentServer;
import sd.tp1.client.ws.ContentServerService;
import sd.tp1.client.ws.Exception_Exception;
import sd.tp1.client.ws.IOException_Exception;
import sd.tp1.gui.GalleryContentProvider;
import sd.tp1.gui.Gui;

/*
 * This class provides the album/picture content to the gui/main application.
 * 
 * Project 1 implementation should complete this class. 
 */
public class SharedGalleryContentProvider implements GalleryContentProvider {

	//comandos
	// ServidorRest: java -Djava.net.preferIPv4Stack=true -cp Jersey-bundle.jar sd.tp1.server.RestServer 10.0.25.153
	// WS: java -Djava.net.preferIPv4Stack=true sd.tp1.server.ContentServer /Users/Mafalda/Documents/SharedServerFiles 10.0.25.153
	// Client: eclipse
	// WSIMPORT: wsimport -keep -s src -d bin -p sd.tp1.client.ws http://localhost:8080/FileServer?wsdl

	Gui gui;
	public Map<String,ContentServer> servers;
	public Map<ContentServer,String> serverAlbuns;

	SharedGalleryContentProvider() throws IOException {


		//Verifca se os servidores ainda estao ligados
		Timer timer2 = new Timer();
		TimerTask myTask2 = new TimerTask() {
			public void run() {
				try {
					System.out.println("Cheking servers");
					pingServers();
					System.out.println("DONE");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer2.schedule(myTask2, 2000, 10000);

		//Procura novas conexões	
		Timer timer = new Timer();
		TimerTask myTask = new TimerTask() {
			public void run() {
				try {
					System.out.println("Searching servers");
					multicast();
					System.out.println("DONE");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(myTask,0,20000);
	}

	public void pingServers(){

		for(ContentServer s: servers.values()){

			System.out.println("Verificar servidores "+ s);
			boolean ping=false;

			try{
				ping=s.pingMethod();
				continue;

			}catch(Exception e){ 
				String al=serverAlbuns.get(s);
				try {
					s.deleteAlbum(al);
				} catch (Exception_Exception e1) {
					e1.printStackTrace();
				}
				serverAlbuns.remove(s);
				servers.remove(s);
				gui.updateAlbums();
				System.out.println("Server"+s+" are disconected");
				continue;
			}	
		}
	}


	public void multicast() throws IOException{

		int port = 9000;
		InetAddress adress;
		MulticastSocket socket = new MulticastSocket();
		ContentServer server;

		servers= new HashMap<String,ContentServer>();
		serverAlbuns=new HashMap<ContentServer,String>();
		System.out.println("Seraching for servers");
		adress = InetAddress.getByName("224.0.0.1");	

		// Manda para o servidor 
		byte[] input = new byte[1024];
		DatagramPacket packet = new DatagramPacket(input, input.length);
		packet.setAddress(adress);
		packet.setPort(port);
		socket.send(packet);

		long start = System.currentTimeMillis();
		socket.setSoTimeout(10000);
		while(false||(System.currentTimeMillis()-start)<19000){

			// Espera que o servidor responda 
			DatagramPacket datagram = new DatagramPacket(input, input.length);
			try{
				socket.receive(datagram);

				String serverAddress = new String(datagram.getData());
				URL wsURL = new URL(String.format(serverAddress));
				
				/*
				SSLContext sc = SSLContext.getInstance("TLSv1");			
				TrustManager[] trustAllCerts = { new InsecureTrustManager() };
		        sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier( new InsecureHostnameVerifier());
				*/
				
				ContentServerService service = new ContentServerService(wsURL);
				server = service.getContentServerPort();
				servers.put(serverAddress, server);
				System.err.println("WS Server"+serverAddress+" ready... ");	

			}catch(SocketTimeoutException e){ 
				continue;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Daowncall from the GUI to register itself, so that it can be updated via
	 * upcalls.
	 */
	@Override
	public void register(Gui gui) {
		if (this.gui == null) {
			this.gui = gui;
		}
	}

	/**
	 * Returns the list of albums in the system. On error this method should
	 * return null.
	 * 
	 * @throws Exception_Exception
	 */
	@Override
	public List<Album> getListOfAlbums() {


		List<String> albuns = new ArrayList<String>();
		List<String> aux= new ArrayList<String>();

		for(ContentServer s: servers.values()){
			try {
				aux=s.getAlbuns();
			} catch (Exception e) {
				return null;
			}
			for(String a: aux)
				albuns.add(a);
			for(String a: albuns){ 
				serverAlbuns.put(s,a);
			}
		}

		List<Album> lst = new ArrayList<Album>();
		for (String a : albuns) {
			lst.add(new SharedAlbum(a));
		}
		return lst;
	}

	/**
	 * Returns the list of pictures for the given album. On error this method
	 * should return null.
	 */
	@Override
	public List<Picture> getListOfPictures(Album album) {

		List<String> pic = new ArrayList<String>();
		List<Picture> lst = new ArrayList<Picture>();
		String name=album.getName();

		for(ContentServer s: servers.values()){
			String a=serverAlbuns.get(s);
			if(a.equalsIgnoreCase(name)){
				try {
					pic=s.getPictures(name);
				}catch (Exception_Exception e) {
					return null;
				}
			}
		}
		for (String p : pic) {
			lst.add(new SharedPicture(p));
		}
		return lst;
	}

	/**
	 * Returns the contents of picture in album. On error this method should
	 * return null.
	 */
	@Override
	public byte[] getPictureData(Album album, Picture picture) {

		String name=album.getName();
		String nameP=picture.getName();
		for(ContentServer s: servers.values()){
			try{	
				String a=serverAlbuns.get(s);
				if(a.equalsIgnoreCase(name)){
					return s.getPictureData(name, nameP);
				}
			}catch (Exception_Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Create a new album. On error this method should return null.
	 */
	@Override
	public Album createAlbum(String name) {

		for(ContentServer s: servers.values()){
			try {
				s.createAlbum(name);
			} catch (Exception_Exception e) {
				return null;
			}
			serverAlbuns.put(s,name);
		}
		gui.updateAlbums();
		return new SharedAlbum(name);
	}

	/**
	 * Delete an existing album.
	 */
	@Override
	public void deleteAlbum(Album album) {

		String name=album.getName();
		for(ContentServer s: servers.values()){
			try {
				s.deleteAlbum(name);
				serverAlbuns.remove(name);
			}catch (Exception_Exception e) {

			}
		}
		gui.updateAlbums();
	}

	/**
	 * Add a new picture to an album. On error this method should return null.
	 */
	@Override
	public Picture uploadPicture(Album album, String name, byte[] data) {

		String nameA=album.getName();
		for(ContentServer s: servers.values()){
			try {
				s.uploudFile(nameA, name, data); 
			} catch (IOException_Exception e) {
				return null;
			}
		}
		gui.updateAlbums();
		return new SharedPicture(name);
	}

	/**
	 * Delete a picture from an album. On error this method should return false.
	 */
	@Override
	public boolean deletePicture(Album album, Picture picture) {

		String name=album.getName();
		String namep= picture.getName();

		for(ContentServer s: servers.values()){
			String a=serverAlbuns.get(s);
			if(a.equals(name)){
				try{
					s.deletePicture(name, namep);
					gui.updateAlbums();
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Represents a shared album.
	 */
	static class SharedAlbum implements GalleryContentProvider.Album {
		final String name;

		SharedAlbum(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	/**
	 * Represents a shared picture.
	 */
	static class SharedPicture implements GalleryContentProvider.Picture {
		final String name;

		SharedPicture(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	static class SharedPictureExtended implements GalleryContentProvider.Picture {
		final String name;

		SharedPictureExtended(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	/*
	
	static public class InsecureHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			System.err.println(hostname);
			return true;
		}
	}

	static public class InsecureTrustManager implements X509TrustManager {
	    @Override
	    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
	    }

	    @Override
	    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
	    	Arrays.asList( chain ).forEach( i -> {
	    		System.err.println( "type: " + i.getType() + "from: " + i.getNotBefore() + " to: " + i.getNotAfter() );
	    	});
	    }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
	        return new X509Certificate[0];
	    }
	}
	*/
}
