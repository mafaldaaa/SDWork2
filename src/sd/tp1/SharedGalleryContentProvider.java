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
import sd.tp1.client.ws.WSServerService;
import sd.tp1.gui.GalleryContentProvider;
import sd.tp1.gui.Gui;
import sd.tp1.client.ws.WSServer;

public class SharedGalleryContentProvider implements GalleryContentProvider {

	// COMANDOS
	// WS: java -Djava.net.preferIPv4Stack=true -classpath $JARLASSPATH sd.tp1.server.WSServer /Users/Mafalda/Documents/teste 192.168.1.3 8000 serverws
	// Proxy: java -Djava.net.preferIPv4Stack=true -classpath $JARLASS sd.tp1.server.ContentServer ola 192.168.1.3 9001 serverproxy
	///Users/Mafalda/Documents/workspace/SDTP2/libJar
	
	Gui gui;
	public Map<String,WSServer> serversWS; //Guarda dos servidores do tipo  WSServer
	public Map<String,ContentServer> serversProxy; //Guarda os servidores do tipo ContentServer
	public Map<WSServer,List<String>> serverAlbunsWS; //Guarda os albuns dos servidores vindos do WS
	public Map<ContentServer,List<String>> serverAlbunsproxy;//Guardas os albuns dos servidores vindos do Proxy.

	/**
	 * Realiza a replicação para os servidores do tipo WSServer
	 */
	public void replicationsWS(WSServer server) throws Exception_Exception, IOException_Exception{

		//Assumindo que o multicast so detecta um servidor de cada vez

		List<String> albunsWSNew=serverAlbunsWS.get(server);

		//Replicação para todos os Proxy
		for(ContentServer p: serversProxy.values()){
			List<String> albunsproxy= serverAlbunsproxy.get(p);

			//VE WS
			for(String a: albunsproxy){
				if(!albunsWSNew.contains(a)){
					server.createAlbum(a); //CRIA NO NOVO SERVIDOR	
					List<String> picturesAlbum=p.getPictures(a); //Imagens dos que la estao
					for(String pic: picturesAlbum){
						byte[] data= p.getPictureData(a, pic);
						server.uploudFile(a, pic, data); //Uploud no novo servidor;
					}
					serverAlbunsWS.get(server).add(a);
				}
			}
			//Ve Proxy
			for(String a: albunsWSNew){
				if(!albunsproxy.contains(a)){ //CRIA NOS SERVERS ANTIGOS
					p.createAlbum(a);
					List<String> newPictures=server.getPictures(a);
					for(String pic: newPictures){
						byte[] data=server.getPictureData(a, pic);
						p.uploudFile(a, pic, data);
					}
					serverAlbunsproxy.get(p).add(a);
				}
			}
		}

		//Replica para todos os WS
		for(WSServer s: serversWS.values()){
			List<String> albunsWS= serverAlbunsWS.get(s);

			for(String a: albunsWS){ //CRIA NO NOVO SERVIDOR
				if(!albunsWSNew.contains(a)){
					server.createAlbum(a);
					List<String> picturesAlbuns=s.getPictures(a);
					for(String pic:picturesAlbuns){
						byte[] data= s.getPictureData(a, pic);
						server.uploudFile(a, pic, data);
					}
					serverAlbunsWS.get(server).add(a);
				}
			}
			for(String a: albunsWSNew){//CRIA NOS ANTIGOS
				if(!albunsWS.contains(a)){
					s.createAlbum(a);
					List<String> picturesAlbuns=server.getPictures(a);
					for(String pic: picturesAlbuns){
						byte[] data=server.getPictureData(a, pic);
						s.uploudFile(a, pic, data);
					}
					serverAlbunsWS.get(s).add(a);
				}
			}
		}
	}
	
	/**
	 * Realiza a replicação para os servidores do tipo ContentServer (proxy)
	 */
	public void replicationProxys(ContentServer server) throws Exception_Exception, IOException_Exception{

		//Assumindo que o multicast so detecta um servidor de cada vez

		List<String> albunsProxynew=serverAlbunsproxy.get(server);

		//Replicação para todos os Proxy
		for(ContentServer p: serversProxy.values()){
			List<String> albunsproxy= serverAlbunsproxy.get(p);

			for(String a: albunsproxy){
				if(!albunsProxynew.contains(a)){
					server.createAlbum(a);
					List<String> picturesAlbuns=p.getPictures(a);
					for(String pic:picturesAlbuns){
						byte[] data= p.getPictureData(a, pic);
						server.uploudFile(a, pic, data);
					}
					serverAlbunsproxy.get(server).add(a);
				}
			}
			for(String a: albunsProxynew){
				if(!albunsproxy.contains(a)){
					p.createAlbum(a);
					List<String> picturesAlbuns=server.getPictures(a);
					for(String pic: picturesAlbuns){
						byte[] data=server.getPictureData(a, pic);
						p.uploudFile(a, pic, data);
					}
					serverAlbunsproxy.get(p).add(a);
				}
			}
		}

		//Replica para todos os WS
		for(WSServer s: serversWS.values()){
			List<String> albunsWs= serverAlbunsWS.get(s);

			for(String a: albunsWs){
				if(!albunsProxynew.contains(a)){
					server.createAlbum(a);
					List<String> picturesAlbuns = s.getPictures(a);
					for(String pic: picturesAlbuns){
						byte[] data= s.getPictureData(a, pic);
						server.uploudFile(a, pic, data);
					}
					serverAlbunsproxy.get(server).add(a);
				}
			}
			for(String a: albunsProxynew){
				if(!albunsWs.contains(a)){
					s.createAlbum(a);
					List<String> picturesAlbuns=server.getPictures(a);
					for(String pic:picturesAlbuns){
						byte[] data=server.getPictureData(a, pic);
						s.uploudFile(a, pic, data);
					}
					serverAlbunsWS.get(s).add(a);
				}
			}
		}
	}

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

		for(ContentServer s: serversProxy.values()){
			System.out.println("Verificar servidores WS "+ s);
			boolean ping=false;
			try{
				ping=s.pingMethod();
				continue;
			}catch(Exception e){ 
				List<String> al=serverAlbunsproxy.get(s);
				try {
					for(String a: al){
						s.deleteAlbum(a);
					}
				} catch (Exception_Exception e1) {
					e1.printStackTrace();
				}
				serverAlbunsproxy.remove(s);
				serversProxy.remove(s);
				gui.updateAlbums();
				System.out.println("Server WS"+s+" are disconected");
				continue;
			}	
		}

		for(WSServer s: serversWS.values()){
			System.out.println("Verificar servidores WS "+ s);
			boolean ping=false;
			try{
				ping=s.pingMethod();
				continue;
			}catch(Exception e){ 
				List<String> al=serverAlbunsproxy.get(s);
				try {
					for(String a: al){
						s.deleteAlbum(a);
					}
				} catch (Exception_Exception e1) {
					e1.printStackTrace();
				}
				serverAlbunsWS.remove(s);
				serversProxy.remove(s);
				gui.updateAlbums();
				System.out.println("Server WS"+s+" are disconected");
				continue;
			}	
		}
	}

	/**
	 * Realiza o multicast
	 */
	public void multicast() throws IOException{
		
		int port = 9000;
		InetAddress adress;
		MulticastSocket socket = new MulticastSocket();
		ContentServer serverproxy;
		WSServer serverws;

		serversWS= new HashMap<String,WSServer>();
		serversProxy=new HashMap<String,ContentServer>();
		serverAlbunsWS=new HashMap<WSServer,List<String>>();
		serverAlbunsproxy=new HashMap<ContentServer,List<String>>();

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

				//Multicast continua sem segurança, por isso quando recebemos o datagramPacket temos de mudar a address de http para  https
				String tmp[] = new String(datagram.getData()).split("!");
				String serverAddress = tmp[0];
				String newServerAddress = serverAddress.replace("http", "https");
				System.out.println(newServerAddress);
				URL URL = new URL(String.format(newServerAddress));


				SSLContext sc = SSLContext.getInstance("TLSv1");			
				TrustManager[] trustAllCerts = { new InsecureTrustManager() };
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier( new InsecureHostnameVerifier());			

				if(tmp[1].equals("ws")){
					System.err.println("Connecting to:" + URL );
					WSServerService service = new WSServerService(URL);

					serverws = service.getWSServerPort();
					serversWS.put(serverAddress, serverws);
					System.err.println("WS Server"+serverAddress+" ready... ");	
				
					//Adiciona albuns do servidor a lista
					List<String> aux=serverws.getAlbuns();
					List<String> album= new ArrayList<String>();
					for(String albuns: aux){
						if (albuns.charAt(0) == '.') {
							  continue; 
						}
						album.add(albuns);		
					}
					serverAlbunsWS.put(serverws,album);	
					replicationsWS(serverws);
				}
				else if(tmp[1].equals("proxy")){
					System.err.println("Connecting to:" + URL );
					ContentServerService service = new ContentServerService(URL);

					serverproxy = service.getContentServerPort();
					serversProxy.put(serverAddress, serverproxy);
					System.err.println("Proxy Server"+serverAddress+" ready... ");
					
					//Adiciona albuns do servidor a lista
					List<String> aux=serverproxy.getAlbuns();
					serverAlbunsproxy.put(serverproxy,aux);
					replicationProxys(serverproxy);
				}
				gui.updateAlbums();

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
		for(WSServer s: serversWS.values()){
			List<String> aux=serverAlbunsWS.get(s);
			for(String a: aux){
				if(!albuns.contains(a))
					albuns.add(a);
			}
		}

		for(ContentServer s: serversProxy.values()){
			List<String> aux= serverAlbunsproxy.get(s);
			for(String a: aux){
				if(!albuns.contains(a))
					albuns.add(a);
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

		for(ContentServer s: serversProxy.values()){
			List<String> albuns=serverAlbunsproxy.get(s);
			for(String a:albuns){
				if(a.equalsIgnoreCase(name)){
					try {
						pic=s.getPictures(name);
					}catch (Exception_Exception e) {
						return null;
					}
				}
			}	
		}

		for(WSServer s: serversWS.values()){
			List<String> albuns=serverAlbunsWS.get(s);
			for(String a:albuns){
				if(a.equalsIgnoreCase(name)){
					try {
						pic=s.getPictures(name);
					}catch (Exception_Exception e) {
						return null;
					}
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

		for(WSServer s: serversWS.values()){
			List<String> albuns=serverAlbunsWS.get(s);
			for(String a:albuns){
				if(a.equalsIgnoreCase(name)){
					try{
						return s.getPictureData(name, nameP);
					}catch (Exception_Exception e) {
						return null;
					}
				}
			}
		}
		for(ContentServer s: serversProxy.values()){
			List<String> albuns=serverAlbunsproxy.get(s);
			for(String a:albuns){
				System.out.println(a);
				if(a.equalsIgnoreCase(name)){
					try{
						byte[] b= s.getPictureData(name, nameP);
						return b;
					}catch (Exception_Exception e) {
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Create a new album. On error this method should return null.
	 */
	@Override
	public Album createAlbum(String name) {

		for(ContentServer s: serversProxy.values()){
			try {
				if(!serverAlbunsproxy.get(s).contains(name))
					s.createAlbum(name);
			} catch (Exception_Exception e) {
				return null;
			}
			serverAlbunsproxy.get(s).add(name);
		}
		gui.updateAlbums();
		for(WSServer s: serversWS.values()){
			try {
				if(!serverAlbunsWS.get(s).contains(name))
					s.createAlbum(name);
			} catch (Exception_Exception e) {
				return null;
			}
			serverAlbunsWS.get(s).add(name);
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
		for(ContentServer s: serversProxy.values()){
			try {
				s.deleteAlbum(name);	
			}catch (Exception_Exception e) {
			}
			serverAlbunsproxy.get(s).remove(name);
		}
		
		for(WSServer s: serversWS.values()){
			try {
				s.deleteAlbum(name);
			}catch (Exception_Exception e) {
			}
			serverAlbunsWS.get(s).remove(name);
		}
		gui.updateAlbums();
	}

	/**
	 * Add a new picture to an album. On error this method should return null.
	 */
	@Override
	public Picture uploadPicture(Album album, String name, byte[] data) {

		String nameA=album.getName();

		for(WSServer s:serversWS.values()){
			List<String> albuns= serverAlbunsWS.get(s);
			for(String a: albuns){
				if(a.equals(nameA)){
					try {
						s.uploudFile(nameA, name, data); 
					} catch (IOException_Exception e) {
						return null;
					}
				}
			}
		}
		gui.updateAlbums();
		for(ContentServer s:serversProxy.values()){
			List<String> albuns= serverAlbunsproxy.get(s);
			for(String a: albuns){
				if(a.equals(nameA)){
					try {
						s.uploudFile(nameA, name, data); 
					} catch (IOException_Exception e) {
						return null;
					}
				}
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
		boolean res=false;

		for(WSServer s: serversWS.values()){
			List<String> albuns=serverAlbunsWS.get(s);
			for(String a: albuns){
				if(a.equals(name)){
					try{
						s.deletePicture(name, namep);
						
						res= true;
					} catch (Exception e) {
						res= false;
					}
				}
			}
		}
		gui.updateAlbums();
		for(ContentServer s: serversProxy.values()){
			List<String> albuns=serverAlbunsproxy.get(s);
			for(String a: albuns){
				if(a.equals(name)){
					try{
						s.deletePicture(name, namep);
						
						res= true;
					} catch (Exception e) {
						res= false;
					}
				}
			}
		}
		gui.updateAlbums();
		return res;
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

	//SSL Methods
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

}
