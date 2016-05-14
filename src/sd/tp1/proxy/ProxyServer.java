package sd.tp1.proxy;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.PathParam;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Exemplo de acesso ao servico Imgur.
 * <p>
 * O URL base para programadores esta disponivel em: <br>
 * https://api.imgur.com/
 * <p> 
 * A API REST do sistema esta disponivel em: <br>
 * https://api.imgur.com/endpoints
 * <p>
 * Para poder aceder ao servico Imgur, deve criar uma app em:
 * https://api.imgur.com/oauth2/addclient onde obtera a apiKey e a apiSecret a
 * usar na criacao do objecto OAuthService.
 * Deve use a opcao: OAuth 2 authorization without a callback URL
 * <p>
 * Este exemplo usa a biblioteca OAuth Scribe, disponivel em:
 * https://github.com/scribejava/scribejava
 * Para descarregar a biblioteca deve descarregar o jar do core:
 * http://mvnrepository.com/artifact/com.github.scribejava/scribejava-core
 * e da API
 * http://mvnrepository.com/artifact/com.github.scribejava/scribejava-apis
 * <p>
 * e a biblioteca json-simple, disponivel em:
 * http://code.google.com/p/json-simple/
 * <p>
 * e a biblioteca apache commons codec, disponivel em:
 * http://commons.apache.org/proper/commons-codec/
 */
public class ProxyServer
{

	public ProxyServer(){
			
	}

	public byte[] getPictureData(OAuth2AccessToken accessToken, OAuth20Service service, String album,String pic) throws ParseException {
		
		byte[] dataPic= new byte[1024];
		
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
			if(title.equalsIgnoreCase(album)){
				Iterator imagesIt = imgs.iterator();
				while(imagesIt.hasNext()){
					JSONObject next = (JSONObject) imagesIt.next();
					String namePic = (String) next.get("title");
					if(namePic.equalsIgnoreCase(pic)){
						dataPic= (byte[]) next.get("image");
					}
				}
			}
		}
		
		return dataPic;
	}

	public boolean deletePicture(OAuth2AccessToken accessToken, OAuth20Service service, String album, String pic) throws ParseException {
		boolean sucess = false;
		
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
			if(title.equalsIgnoreCase(album)){
				Iterator imagesIt = imgs.iterator();
				while(imagesIt.hasNext()){
					JSONObject next = (JSONObject) imagesIt.next();
					String namePic = (String) next.get("title");
					if(namePic.equalsIgnoreCase(pic)){
						String idPic = (String) next.get("id");
						OAuthRequest deletee = new OAuthRequest(Verb.DELETE, "https://api.imgur.com/3/image/"+idPic , service);
						service.signRequest(accessToken, deletee);
						Response deleteRes = oar.send();
						if(deleteRes.getCode() == 200){
							sucess = true;
						}
					}
				}
			}	
		}	
		return sucess;
	}

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

			if(title.equalsIgnoreCase(album)){
				String idAlbum=(String) data.get("id");
				OAuthRequest uploudreq = new OAuthRequest(Verb.POST, "https://api.imgur.com/3/image/", service);
				service.signRequest(accessToken, uploudreq);
				albumsReq.addParameter("album", idAlbum);
				albumsReq.addParameter("title", pic);
				albumsReq.addPayload(dataImage);
				final Response deleteRes = uploudreq.send();
				System.out.println(deleteRes.getCode());
			}
		}
	}

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

	public void createAlbum(OAuth2AccessToken accessToken, OAuth20Service service, String name) throws ParseException{
		OAuthRequest albumsReq = new OAuthRequest(Verb.POST, "https://api.imgur.com/3/album/{title}", service);
		service.signRequest(accessToken, albumsReq);
		albumsReq.addParameter("title", name);
		final Response albumsRes = albumsReq.send(); 
		System.out.println(albumsRes.getCode());
	}

	public List<String> getAlbuns(OAuth2AccessToken accessToken, OAuth20Service service) throws ParseException{

		List<String> albunsList= new LinkedList<String>();
		
		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		System.out.println(albumsRes.getCode());

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

	public List<String> getPictures(OAuth2AccessToken accessToken, OAuth20Service service, String album ) throws ParseException{ 
		
		List<String> pictures = new LinkedList<String>();
		
		OAuthRequest albumsReq = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/muffinv/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		System.out.println(albumsRes.getCode());
		JSONParser parser = new JSONParser();
		JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());

		//****************GET Albuns
		JSONArray albums = (JSONArray) res.get("data");
		Iterator albumsIt = albums.iterator();
		while (albumsIt.hasNext()) {
			OAuthRequest oar = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/album/"+ albumsIt.next(), service);
			service.signRequest(accessToken, oar);
			Response albRes = oar.send();

			//***********Get Pictures
			res = (JSONObject) parser.parse(albRes.getBody());
			JSONObject data = (JSONObject) res.get("data");
			JSONArray imgs = (JSONArray) data.get("images");
			String title=(String) data.get("title");

			if(title.equalsIgnoreCase(album)){
				Iterator imagesIt = imgs.iterator();
				while(imagesIt.hasNext()){
					JSONObject next = (JSONObject) imagesIt.next();
					String pic = (String) next.get("title");
					pictures.add(pic);
				}
			}
		}
		return pictures;
	}
}