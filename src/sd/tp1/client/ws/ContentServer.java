
package sd.tp1.client.ws;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ContentServer", targetNamespace = "http://server.tp1.sd/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ContentServer {


    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAlbuns", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.GetAlbuns")
    @ResponseWrapper(localName = "getAlbunsResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.GetAlbunsResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/getAlbunsRequest", output = "http://server.tp1.sd/ContentServer/getAlbunsResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server.tp1.sd/ContentServer/getAlbuns/Fault/Exception")
    })
    public List<String> getAlbuns()
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getPictures", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.GetPictures")
    @ResponseWrapper(localName = "getPicturesResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.GetPicturesResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/getPicturesRequest", output = "http://server.tp1.sd/ContentServer/getPicturesResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server.tp1.sd/ContentServer/getPictures/Fault/Exception")
    })
    public List<String> getPictures(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns byte[]
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getPictureData", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.GetPictureData")
    @ResponseWrapper(localName = "getPictureDataResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.GetPictureDataResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/getPictureDataRequest", output = "http://server.tp1.sd/ContentServer/getPictureDataResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server.tp1.sd/ContentServer/getPictureData/Fault/Exception")
    })
    public byte[] getPictureData(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg0
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "createAlbum", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.CreateAlbum")
    @ResponseWrapper(localName = "createAlbumResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.CreateAlbumResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/createAlbumRequest", output = "http://server.tp1.sd/ContentServer/createAlbumResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server.tp1.sd/ContentServer/createAlbum/Fault/Exception")
    })
    public void createAlbum(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg0
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "deleteAlbum", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.DeleteAlbum")
    @ResponseWrapper(localName = "deleteAlbumResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.DeleteAlbumResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/deleteAlbumRequest", output = "http://server.tp1.sd/ContentServer/deleteAlbumResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server.tp1.sd/ContentServer/deleteAlbum/Fault/Exception")
    })
    public void deleteAlbum(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws IOException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "uploudFile", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.UploudFile")
    @ResponseWrapper(localName = "uploudFileResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.UploudFileResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/uploudFileRequest", output = "http://server.tp1.sd/ContentServer/uploudFileResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://server.tp1.sd/ContentServer/uploudFile/Fault/IOException")
    })
    public void uploudFile(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        byte[] arg2)
        throws IOException_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deletePicture", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.DeletePicture")
    @ResponseWrapper(localName = "deletePictureResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.DeletePictureResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/deletePictureRequest", output = "http://server.tp1.sd/ContentServer/deletePictureResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://server.tp1.sd/ContentServer/deletePicture/Fault/Exception")
    })
    public boolean deletePicture(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1)
        throws Exception_Exception
    ;

    /**
     * 
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "pingMethod", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.PingMethod")
    @ResponseWrapper(localName = "pingMethodResponse", targetNamespace = "http://server.tp1.sd/", className = "sd.tp1.client.ws.PingMethodResponse")
    @Action(input = "http://server.tp1.sd/ContentServer/pingMethodRequest", output = "http://server.tp1.sd/ContentServer/pingMethodResponse")
    public boolean pingMethod();

}
