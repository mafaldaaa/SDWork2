package sd.tp1.proxy;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.PathParam;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ProxyServer
{

	public ProxyServer(){

	}
	
	/** Devolve albuns do imgur*/
	public List<String> getAlbuns(OAuth2AccessToken accessToken, OAuth20Service service) throws ParseException{

		List<String> albunsList= new LinkedList<String>();

		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();

		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());
		JSONArray albuns = (JSONArray) res.get("data");

		Iterator albunsIt = albuns.iterator();
		while (albunsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albunsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();
			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			String album= (String) data.get("title");
			albunsList.add(album);	
		}
		return albunsList;
	}

	/**Devolve imagens de um album do imgur*/
	public List<String> getPictures(OAuth2AccessToken accessToken, OAuth20Service service, String album ) throws ParseException{ 

		List<String> pictures = new LinkedList<String>();

		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());

		JSONArray albums = (JSONArray) res.get("data");
		Iterator albumsIt = albums.iterator();
		while (albumsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albumsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();

			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			JSONArray imgs = (JSONArray) data.get("images");
			String title=(String) data.get("title");

			if(title.equalsIgnoreCase(album)){
				Iterator imagesIt = imgs.iterator();
				while(imagesIt.hasNext()){
					JSONObject next = (JSONObject) imagesIt.next();
					String type=(String)next.get("type");
					String pic = (String) next.get("id")+".png";
					pictures.add(pic);
				}
			}
		}
		return pictures;
	}

	/**Devolve o data de uma imagem */
	public byte[] getPictureData(OAuth2AccessToken accessToken, OAuth20Service service, String album,String pic) throws ParseException, IOException {

		byte[] dataPic;

		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());
		JSONArray albuns = (JSONArray) res.get("data");

		Iterator albunsIt = albuns.iterator();
		while (albunsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albunsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();

			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			String title=(String) data.get("title");

			if(title.equalsIgnoreCase(album)){
				JSONArray imgs = (JSONArray) data.get("images");
				Iterator imagesIt = imgs.iterator();
				
				while(imagesIt.hasNext()){
					JSONObject next = (JSONObject) imagesIt.next();
					String namePic = (String) next.get("id")+".png";
				
					if(namePic.equalsIgnoreCase(pic)){
						String link = (String) next.get("link");
						URL uri= new URL(link);
						dataPic=new byte[10*10*1024];
						uri.openStream().read(dataPic);
						return dataPic;
					}
				}
			}
		}	
		return null;
	}

	/**Remove uma imagem de um album do imgur*/
	public boolean deletePicture(OAuth2AccessToken accessToken, OAuth20Service service, String album, String pic) throws ParseException {
		
		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());
		JSONArray albuns = (JSONArray) res.get("data");

		Iterator albunsIt = albuns.iterator();
		while (albunsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albunsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();

			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			JSONArray imgs = (JSONArray) data.get("images");
			String title=(String) data.get("title");
			String albumId = (String) data.get("id");

			if(title.equalsIgnoreCase(album)){
				
				Iterator imagesIt = imgs.iterator();
				while(imagesIt.hasNext()){
					JSONObject next = (JSONObject) imagesIt.next();
					String namePic = (String) next.get("id")+".png";
				
					if(namePic.equalsIgnoreCase(pic)){
						
						String idPic = (String) next.get("id");
						OAuthRequest deletee = new OAuthRequest(Verb.DELETE,  "https://api.imgur.com/3/image/"+idPic, service);
						service.signRequest(accessToken, deletee);
						Response deleteRes = deletee.send();
				
						if(deleteRes.getCode()==200){
							return true;
						}
					}
				}
			}	
		}	
		return false;
	}

	/**Faz uploud de uma imagem para um album do imgur*/
	public void uploudPicture(OAuth2AccessToken accessToken, OAuth20Service service, String album, String pic, byte[] dataImage) throws ParseException {

		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());
		JSONArray albuns = (JSONArray) res.get("data");

		Iterator albunsIt = albuns.iterator();
		while (albunsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albunsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();
			
			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			String title=(String) data.get("title");
			String idAlbum=(String) data.get("id");
			System.out.println(idAlbum);
			
			if(title.equalsIgnoreCase(album)){
				OAuthRequest uploudReq = new OAuthRequest(Verb.POST, "https://api.imgur.com/3/upload", service);
				uploudReq.addQuerystringParameter("album", idAlbum) ;
				uploudReq.addQuerystringParameter("title", pic) ;
				uploudReq.addPayload(dataImage);	
				
				service.signRequest(accessToken, uploudReq);
				final Response deleteRes = uploudReq.send();
				System.out.println(deleteRes.getBody());
			}
		}
	}

	/**Faz delete de um album do imgur*/
	public void deleteAlbum(OAuth2AccessToken accessToken, OAuth20Service service, String name) throws ParseException {

		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());
		JSONArray albuns = (JSONArray) res.get("data");

		Iterator albunsIt = albuns.iterator();
		while (albunsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albunsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();

			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			String title=(String) data.get("title");

			if(title.equalsIgnoreCase(name)){
				String id=(String) data.get("id");
				OAuthRequest deletereq = new OAuthRequest(Verb.DELETE, "https://api.imgur.com/3/album/"+ id, service);
				service.signRequest(accessToken, deletereq);
				final Response deleteRes = deletereq.send();
				System.out.println(deleteRes.getCode());
			}	
		}	
	}
	
	/**Faz create de um album no imgur*/
	public void createAlbum(OAuth2AccessToken accessToken, OAuth20Service service, String name) throws ParseException{
		OAuthRequest albumsReq = new OAuthRequest(Verb.POST, "https://api.imgur.com/3/album/{title}", service);
		service.signRequest(accessToken, albumsReq);
		albumsReq.addParameter("title", name);
		final Response albumsRes = albumsReq.send(); 
		System.out.println(albumsRes.getCode());
	}
}