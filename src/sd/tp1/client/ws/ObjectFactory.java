
package sd.tp1.client.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sd.tp1.client.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAlbuns_QNAME = new QName("http://server.tp1.sd/", "getAlbuns");
    private final static QName _GetPictureData_QNAME = new QName("http://server.tp1.sd/", "getPictureData");
    private final static QName _GetPictureDataResponse_QNAME = new QName("http://server.tp1.sd/", "getPictureDataResponse");
    private final static QName _DeleteAlbumResponse_QNAME = new QName("http://server.tp1.sd/", "deleteAlbumResponse");
    private final static QName _CreateAlbumResponse_QNAME = new QName("http://server.tp1.sd/", "createAlbumResponse");
    private final static QName _IsProxy_QNAME = new QName("http://server.tp1.sd/", "isProxy");
    private final static QName _Exception_QNAME = new QName("http://server.tp1.sd/", "Exception");
    private final static QName _UploudFileResponse_QNAME = new QName("http://server.tp1.sd/", "uploudFileResponse");
    private final static QName _DeletePicture_QNAME = new QName("http://server.tp1.sd/", "deletePicture");
    private final static QName _IOException_QNAME = new QName("http://server.tp1.sd/", "IOException");
    private final static QName _IsProxyResponse_QNAME = new QName("http://server.tp1.sd/", "isProxyResponse");
    private final static QName _UploudFile_QNAME = new QName("http://server.tp1.sd/", "uploudFile");
    private final static QName _GetAlbunsResponse_QNAME = new QName("http://server.tp1.sd/", "getAlbunsResponse");
    private final static QName _GetPicturesResponse_QNAME = new QName("http://server.tp1.sd/", "getPicturesResponse");
    private final static QName _DeleteAlbum_QNAME = new QName("http://server.tp1.sd/", "deleteAlbum");
    private final static QName _PingMethod_QNAME = new QName("http://server.tp1.sd/", "pingMethod");
    private final static QName _PingMethodResponse_QNAME = new QName("http://server.tp1.sd/", "pingMethodResponse");
    private final static QName _GetPictures_QNAME = new QName("http://server.tp1.sd/", "getPictures");
    private final static QName _CreateAlbum_QNAME = new QName("http://server.tp1.sd/", "createAlbum");
    private final static QName _DeletePictureResponse_QNAME = new QName("http://server.tp1.sd/", "deletePictureResponse");
    private final static QName _UploudFileArg2_QNAME = new QName("", "arg2");
    private final static QName _GetPictureDataResponseReturn_QNAME = new QName("", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sd.tp1.client.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteAlbumResponse }
     * 
     */
    public DeleteAlbumResponse createDeleteAlbumResponse() {
        return new DeleteAlbumResponse();
    }

    /**
     * Create an instance of {@link GetAlbuns }
     * 
     */
    public GetAlbuns createGetAlbuns() {
        return new GetAlbuns();
    }

    /**
     * Create an instance of {@link GetPictureData }
     * 
     */
    public GetPictureData createGetPictureData() {
        return new GetPictureData();
    }

    /**
     * Create an instance of {@link GetPictureDataResponse }
     * 
     */
    public GetPictureDataResponse createGetPictureDataResponse() {
        return new GetPictureDataResponse();
    }

    /**
     * Create an instance of {@link DeletePicture }
     * 
     */
    public DeletePicture createDeletePicture() {
        return new DeletePicture();
    }

    /**
     * Create an instance of {@link UploudFileResponse }
     * 
     */
    public UploudFileResponse createUploudFileResponse() {
        return new UploudFileResponse();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link IsProxyResponse }
     * 
     */
    public IsProxyResponse createIsProxyResponse() {
        return new IsProxyResponse();
    }

    /**
     * Create an instance of {@link IsProxy }
     * 
     */
    public IsProxy createIsProxy() {
        return new IsProxy();
    }

    /**
     * Create an instance of {@link CreateAlbumResponse }
     * 
     */
    public CreateAlbumResponse createCreateAlbumResponse() {
        return new CreateAlbumResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link GetPicturesResponse }
     * 
     */
    public GetPicturesResponse createGetPicturesResponse() {
        return new GetPicturesResponse();
    }

    /**
     * Create an instance of {@link DeleteAlbum }
     * 
     */
    public DeleteAlbum createDeleteAlbum() {
        return new DeleteAlbum();
    }

    /**
     * Create an instance of {@link PingMethod }
     * 
     */
    public PingMethod createPingMethod() {
        return new PingMethod();
    }

    /**
     * Create an instance of {@link PingMethodResponse }
     * 
     */
    public PingMethodResponse createPingMethodResponse() {
        return new PingMethodResponse();
    }

    /**
     * Create an instance of {@link UploudFile }
     * 
     */
    public UploudFile createUploudFile() {
        return new UploudFile();
    }

    /**
     * Create an instance of {@link GetAlbunsResponse }
     * 
     */
    public GetAlbunsResponse createGetAlbunsResponse() {
        return new GetAlbunsResponse();
    }

    /**
     * Create an instance of {@link DeletePictureResponse }
     * 
     */
    public DeletePictureResponse createDeletePictureResponse() {
        return new DeletePictureResponse();
    }

    /**
     * Create an instance of {@link CreateAlbum }
     * 
     */
    public CreateAlbum createCreateAlbum() {
        return new CreateAlbum();
    }

    /**
     * Create an instance of {@link GetPictures }
     * 
     */
    public GetPictures createGetPictures() {
        return new GetPictures();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAlbuns }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "getAlbuns")
    public JAXBElement<GetAlbuns> createGetAlbuns(GetAlbuns value) {
        return new JAXBElement<GetAlbuns>(_GetAlbuns_QNAME, GetAlbuns.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPictureData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "getPictureData")
    public JAXBElement<GetPictureData> createGetPictureData(GetPictureData value) {
        return new JAXBElement<GetPictureData>(_GetPictureData_QNAME, GetPictureData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPictureDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "getPictureDataResponse")
    public JAXBElement<GetPictureDataResponse> createGetPictureDataResponse(GetPictureDataResponse value) {
        return new JAXBElement<GetPictureDataResponse>(_GetPictureDataResponse_QNAME, GetPictureDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "deleteAlbumResponse")
    public JAXBElement<DeleteAlbumResponse> createDeleteAlbumResponse(DeleteAlbumResponse value) {
        return new JAXBElement<DeleteAlbumResponse>(_DeleteAlbumResponse_QNAME, DeleteAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "createAlbumResponse")
    public JAXBElement<CreateAlbumResponse> createCreateAlbumResponse(CreateAlbumResponse value) {
        return new JAXBElement<CreateAlbumResponse>(_CreateAlbumResponse_QNAME, CreateAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsProxy }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "isProxy")
    public JAXBElement<IsProxy> createIsProxy(IsProxy value) {
        return new JAXBElement<IsProxy>(_IsProxy_QNAME, IsProxy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploudFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "uploudFileResponse")
    public JAXBElement<UploudFileResponse> createUploudFileResponse(UploudFileResponse value) {
        return new JAXBElement<UploudFileResponse>(_UploudFileResponse_QNAME, UploudFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "deletePicture")
    public JAXBElement<DeletePicture> createDeletePicture(DeletePicture value) {
        return new JAXBElement<DeletePicture>(_DeletePicture_QNAME, DeletePicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsProxyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "isProxyResponse")
    public JAXBElement<IsProxyResponse> createIsProxyResponse(IsProxyResponse value) {
        return new JAXBElement<IsProxyResponse>(_IsProxyResponse_QNAME, IsProxyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploudFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "uploudFile")
    public JAXBElement<UploudFile> createUploudFile(UploudFile value) {
        return new JAXBElement<UploudFile>(_UploudFile_QNAME, UploudFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAlbunsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "getAlbunsResponse")
    public JAXBElement<GetAlbunsResponse> createGetAlbunsResponse(GetAlbunsResponse value) {
        return new JAXBElement<GetAlbunsResponse>(_GetAlbunsResponse_QNAME, GetAlbunsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPicturesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "getPicturesResponse")
    public JAXBElement<GetPicturesResponse> createGetPicturesResponse(GetPicturesResponse value) {
        return new JAXBElement<GetPicturesResponse>(_GetPicturesResponse_QNAME, GetPicturesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "deleteAlbum")
    public JAXBElement<DeleteAlbum> createDeleteAlbum(DeleteAlbum value) {
        return new JAXBElement<DeleteAlbum>(_DeleteAlbum_QNAME, DeleteAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingMethod }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "pingMethod")
    public JAXBElement<PingMethod> createPingMethod(PingMethod value) {
        return new JAXBElement<PingMethod>(_PingMethod_QNAME, PingMethod.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingMethodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "pingMethodResponse")
    public JAXBElement<PingMethodResponse> createPingMethodResponse(PingMethodResponse value) {
        return new JAXBElement<PingMethodResponse>(_PingMethodResponse_QNAME, PingMethodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPictures }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "getPictures")
    public JAXBElement<GetPictures> createGetPictures(GetPictures value) {
        return new JAXBElement<GetPictures>(_GetPictures_QNAME, GetPictures.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "createAlbum")
    public JAXBElement<CreateAlbum> createCreateAlbum(CreateAlbum value) {
        return new JAXBElement<CreateAlbum>(_CreateAlbum_QNAME, CreateAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.tp1.sd/", name = "deletePictureResponse")
    public JAXBElement<DeletePictureResponse> createDeletePictureResponse(DeletePictureResponse value) {
        return new JAXBElement<DeletePictureResponse>(_DeletePictureResponse_QNAME, DeletePictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = UploudFile.class)
    public JAXBElement<byte[]> createUploudFileArg2(byte[] value) {
        return new JAXBElement<byte[]>(_UploudFileArg2_QNAME, byte[].class, UploudFile.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetPictureDataResponse.class)
    public JAXBElement<byte[]> createGetPictureDataResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetPictureDataResponseReturn_QNAME, byte[].class, GetPictureDataResponse.class, ((byte[]) value));
    }

}
