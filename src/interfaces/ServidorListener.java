package interfaces;

import componentes.ArchivoInformacion;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServidorListener extends Remote{
    void actualizarLista(List<ArchivoInformacion> lista) throws RemoteException;
    public byte[] descargarArchivoCliente(String fileName) throws RemoteException;
    public void respaldarArchivo(byte[] buffer,ArchivoInformacion archivo) throws RemoteException;
}