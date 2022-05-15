package componentes;
import interfaces.ServidorListener;
import java.io.Serializable;
/*
@code Objeto que Almacena informacion de un archivo en java
@param nombreArchivo nombre del archivo
@param urlArchivo ubicacion del archivo en la maquina
*/
public class ArchivoInformacion implements Serializable{
    private String nombreArchivo;
    private String urlArchivo;
    ServidorListener primerRespaldo; //PROPIO CLIENTE
    ServidorListener segundoRespaldo; // NULL
    
    public ArchivoInformacion(String n, String u){
        setNombreArchivo(n);
        setUrlArchivo(u);
    }
    
    @Override
    public String toString() {
        return nombreArchivo;
    }

    public ServidorListener getPrimerRespaldo() {
        return primerRespaldo;
    }

    public ServidorListener getSegundoRespaldo() {
        return segundoRespaldo;
    }
    
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setPrimerRespaldo(ServidorListener primerRespaldo) {
        this.primerRespaldo = primerRespaldo;
    }

    public void setSegundoRespaldo(ServidorListener segundoRespaldo) {
        this.segundoRespaldo = segundoRespaldo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }
}
