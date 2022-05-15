package interfaces;

import componentes.ArchivoInformacion;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServidorInterfaz extends Remote{
    public void agregarServidorListener(ServidorListener addTemperatureListener) throws RemoteException;
    public void eliminarServidorListener(ServidorListener addTemperatureListener) throws RemoteException;
    public List<ArchivoInformacion> getLista() throws RemoteException;
    public void agregarArchivoLista(ArchivoInformacion archivo,ServidorListener listener) throws RemoteException;
    public void actualizarListaServidor() throws RemoteException;
    public byte[] downloadFile(String fileName) throws RemoteException;
    public void eliminarArchivo(String nombreArchivo) throws RemoteException;
}
