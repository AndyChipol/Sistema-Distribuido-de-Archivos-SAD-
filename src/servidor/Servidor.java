
package servidor;

import componentes.ArchivoInformacion;
import interfaces.ServidorInterfaz;
import interfaces.ServidorListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Servidor extends UnicastRemoteObject implements ServidorInterfaz {

    private static List<ServidorListener> listeners = new ArrayList<>();
    private static List<ArchivoInformacion> archivosCompartidos;

    protected Servidor() throws RemoteException{
        archivosCompartidos = new ArrayList<ArchivoInformacion>();
    }
    
    public static void main(String[] args){
        try{
            Servidor sev= new Servidor();
            // Binding the remote object (stub) in the registry
            Registry reg = LocateRegistry.createRegistry(52369);
            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + ":52369/SADRMI";
            Naming.rebind(url, sev);
            
            System.out.println("Servidor En linea");
            System.out.println("Lista de Archivos:"+getArchivosCompartidos().toString());
            System.out.println("Listeners Totales:"+ listeners);
            
        }
        catch (Exception e){
            System.out.println("error- " + e);
        }
    }
    // Se pueden utilizar los Listeners como un Id
    //Actualiza la lista a todos los clientes
    private void notifyActualizarListeners(List<ArchivoInformacion> archivosCompartidos){
        for (ServidorListener lListener : listeners){
            try{
                lListener.actualizarLista(archivosCompartidos);
            }
            catch (RemoteException e){
                listeners.remove(lListener);
            }
        }       
    }

    public static List<ArchivoInformacion> getArchivosCompartidos() {
        return archivosCompartidos;
    }
    
    @Override
    public void agregarServidorListener(ServidorListener listener) throws RemoteException {
        listeners.add(listener);
        //System.out.println("Listeners Totales:"+ listeners);
    }

    @Override
    public void eliminarServidorListener(ServidorListener listener) throws RemoteException {
        listeners.remove(listener);
        //System.out.println("Listeners Totales:"+ listeners);
    }

    @Override
    public List<ArchivoInformacion> getLista() throws RemoteException {
        return archivosCompartidos;
    }

    @Override
    public void agregarArchivoLista(ArchivoInformacion archivo,ServidorListener listener) throws RemoteException {
        //BUSCAR EL MISMO LISTENER Y ENCONTRARLO
        //for (ServidorListener lListener : listeners){
        //    if(lListener.equals(listener))
        //        System.out.println("Iguales"+lListener);            
        //}     
        //ENCONTRAR OTRO LISTENER QUE NO SEA EL DEL USUARIO y hacerlo de forma random
        //Asignara el segundoRespaldo del listener, para que sepa donde se encuentra el archivo
        if(listeners.size() > 1){
            while(true){
                int randomNum = (int) Math.floor(Math.random()*listeners.size());
                if(listeners.get(randomNum) != null && !listeners.get(randomNum).equals(listener)){
                    System.out.println("Encontrado");
                    archivo.setSegundoRespaldo(listeners.get(randomNum));
                    break;
                }
            }
            //Si da una exception aqui tiene que eliminar el que dio la execption
            ServidorListener prAr = archivo.getPrimerRespaldo();
            byte buffer[] = prAr.descargarArchivoCliente(archivo.getNombreArchivo());
            
            
            //Si da una exception aqui tiene que eliminar el que dio la execption
            ServidorListener sgAr = archivo.getSegundoRespaldo();
            sgAr.respaldarArchivo(buffer,archivo);
        }
        else{
            System.out.println("No se asigno otro Listener/Cliente");
            archivo.setSegundoRespaldo(null);
        }
        
        
        //sube el archivo a la lista del servidor
        archivosCompartidos.add(archivo);
        System.out.println("Actualizacion de Informacion\n\t"+archivosCompartidos.toString());
        this.notifyActualizarListeners(archivosCompartidos); //Notifica a todos los clientes del nuevo archivo
    }

    @Override
    public void actualizarListaServidor() throws RemoteException {
       this.notifyActualizarListeners(archivosCompartidos);
    }    

    @Override
    public byte[] downloadFile(String archivoSeleccionado) throws RemoteException {
            //BUSCAR EL ARCHIVO SELECCIONADO EN LA LISTA DEL SERVIDOR
            ArchivoInformacion archivoSl = null;
            for (ArchivoInformacion listaServidor : archivosCompartidos){
                if(listaServidor.getNombreArchivo().equals(archivoSeleccionado)){
                    archivoSl = listaServidor;
                    break;
                }
            }  
            //ENCONTRARLO Y DESCARGARLO DE UNO DE SUS RESPALDOS
            //archivoSl tiene el archivoInformacion que selecciono desde afuera el cliente
            try {
                ServidorListener prAr = archivoSl.getPrimerRespaldo();
                byte buffer[] = prAr.descargarArchivoCliente(archivoSeleccionado);
                System.out.println(buffer);
                return(buffer);

            } catch (RemoteException e) {
              // Asignarle un nuevo Respaldo Primario
              ServidorListener prAr = archivoSl.getPrimerRespaldo();
              if(listeners.size() > 1){
                while(true){
                    int randomNum = (int) Math.floor(Math.random()*listeners.size());
                    if(listeners.get(randomNum) != null && !listeners.get(randomNum).equals(prAr)){
                        System.out.println("Encontrado");
                        archivoSl.setPrimerRespaldo(listeners.get(randomNum));
                        break;
                    }
                }
                //Cargar 
                ServidorListener prAr2 = archivoSl.getSegundoRespaldo();
                byte buffer[] = prAr.descargarArchivoCliente(archivoSl.getNombreArchivo());
                ServidorListener sgAr = archivoSl.getPrimerRespaldo();
                sgAr.respaldarArchivo(buffer,archivoSl);
              }
            }
            try {
                ServidorListener prAr = archivoSl.getSegundoRespaldo();
                byte buffer[] = prAr.descargarArchivoCliente(archivoSeleccionado);

                return(buffer);

            } catch (RemoteException e) {
            }
            
            return null;
           
    }

    @Override
    public void eliminarArchivo(String nombreArchivo) throws RemoteException {
        
        for(int i=0;i < archivosCompartidos.size();i++){
            if(archivosCompartidos.get(i).getNombreArchivo().equals(nombreArchivo)){
                archivosCompartidos.remove(i);
                break;
            }
        }        
        System.out.println("Eliminacion de Archivo\n\t"+archivosCompartidos.toString());
        this.notifyActualizarListeners(archivosCompartidos); //Notifica a todos los clientes del nuevo archivo
    }


}