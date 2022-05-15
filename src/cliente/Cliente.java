
package cliente;


import static cliente.ArchivoCliente.clMain;
import static cliente.ArchivoCliente.jList1;
import componentes.ArchivoInformacion;
import interfaces.ServidorInterfaz;
import interfaces.ServidorListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class Cliente extends UnicastRemoteObject implements ServidorListener{
    public static Registry regs;
    public static ServidorInterfaz sev;
    public static List<ArchivoInformacion> listaArchivos;
    static String url;
    static Remote lRemote;
    static ServidorInterfaz reg;
    public static JList lista;
    public static File directorioRespaldo;
            
    protected Cliente(JList lista) throws RemoteException{
        listaArchivos = null;
        this.lista = lista;
    }
    
    /*@code void - Se encarga de comprobar si ya existe el directorio que 
    funcionara como respaldo, si no es asi lo creara*/
    public static void comprobarDirectio(String nombreRespaldo){
        directorioRespaldo = new File("C:\\"+nombreRespaldo);
        if (!directorioRespaldo.exists()) {
            if (directorioRespaldo.mkdirs()) {
                System.out.println("Directorio creado");
            } else {
                System.out.println("Error al crear directorio");
            }
        }
    }
    /*@code void - Enviara el listener para poder iniciar la 
    comunicacion de Servidor-Cliente*/
    public static void iniciarListener(Cliente cl){
        try{
            //Lookup for the service
            url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + ":52369/SADRMI";
            lRemote = Naming.lookup(url);
            reg = (ServidorInterfaz) lRemote;
            reg.agregarServidorListener(cl);
            System.out.println("El Primer Cliente:"+reg.getLista());
            listaArchivos = reg.getLista();
        }
        catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "No Es Posible Establecer Conexion Con el Servidor.\nCerrando Programa.", "Warning",
        JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    public static void cerrarCliente(Cliente cl){
        try{
            reg.eliminarServidorListener(cl);
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
          
    /*@code void - Establecera la comunicacion con el servidor
    en el cual el cliente envia una peticion y recibe una respuesta
    por parte del servidor*/
    public static void iniciarClienteServidor(){
        try{
            regs = LocateRegistry.getRegistry("localhost",52369);
            sev = (ServidorInterfaz)regs.lookup("SADRMI");
            sev.actualizarListaServidor();
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public byte[] obtenerArchivoServidor(String archivoSeleccionado) throws RemoteException{
        return sev.downloadFile(archivoSeleccionado);
    }

    public List<ArchivoInformacion> getListaArchivos(){
        return listaArchivos;
    }
    
    @Override // esta funcion no se usa en el cliente.
    public void actualizarLista(List<ArchivoInformacion> lista) throws RemoteException {
        System.out.println(lista.toString() );
        listaArchivos = lista;
        DefaultListModel modeloActualizar = new DefaultListModel();
        for (int i = 0; i < listaArchivos.size(); i++)
                modeloActualizar.addElement(listaArchivos.get(i).getNombreArchivo());
        this.lista.setModel(modeloActualizar);
        
    }
    
    public void enviarArchivo(ArchivoInformacion archivo,ServidorListener listener) throws RemoteException{
        
        // VERIFICAR LA LISTA DE ARCHIVOS DEL SERVIDOR QUE NO HAYA UN NOMBRE IGUAL
        sev.actualizarListaServidor(); //actualizar la lista
        //recorrer la lista por si hay un nombre igual
        boolean estaArchivoServidor = false;
        for (ArchivoInformacion listaServidor : listaArchivos){
            if(listaServidor.getNombreArchivo().equals(archivo.getNombreArchivo())){
                estaArchivoServidor = true;
                break;
            }
        }  
        
        if(estaArchivoServidor){
            String inicioMensaje = "<HTML><h2>"; //Inicializo el codigo HTML
            String mensajeUno = "El servidor ya contiene un archivo con el mismo nombre.";
            String mensajeDos = "Cambie el nombre o suba otro archivo.";
            String finalMensaje = "</h2></HTML>\n"; 
            JOptionPane.showMessageDialog(
                    null,
                    inicioMensaje + mensajeUno + finalMensaje + inicioMensaje + mensajeDos + finalMensaje,
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
        else{
            archivo.setPrimerRespaldo(listener);
            copiarArchivoRespaldo(archivo);
            sev.agregarArchivoLista(archivo,listener); 
        }
        
              
    }
    
    public void eliminarArchivo(String archivo) throws RemoteException{
        sev.eliminarArchivo(archivo);
    }
    
    public void copiarArchivoRespaldo(ArchivoInformacion archivo){
        File origen = new File(archivo.getUrlArchivo());
        File destino = new File(directorioRespaldo+"\\"+archivo.getNombreArchivo()); //Meter la direccion con el nombre del archivo
        byte buffer[] = new byte[(int)origen.length()];
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(archivo.getUrlArchivo()));
            input.read(buffer,0,buffer.length);
            input.close();
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destino.getAbsolutePath()));
            output.write(buffer,0,buffer.length);
            output.flush();
            output.close();
            
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @Override
    public byte[] descargarArchivoCliente(String fileName) throws RemoteException {
         
            File file = new File(directorioRespaldo + "\\"+ fileName);
            byte buffer[] = new byte[(int)file.length()];
            try { 
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(directorioRespaldo  + "\\"+  fileName));
                input.read(buffer,0,buffer.length);
                input.close();
                System.out.print("Funciona");
             } catch(Exception e){
                System.out.println("FileImpl: "+e.getMessage());
                e.printStackTrace();
             }
         return(buffer);
    }

    @Override
    public void respaldarArchivo(byte[] buffer, ArchivoInformacion archivo) throws RemoteException {
          try {
                File directorio = new File(directorioRespaldo + "\\"+ archivo.getNombreArchivo());
                System.out.println(directorio.getPath());
                //Si se a guardado la direccion crear un archivo ahi para que se guarde la info
                String rutaArchivo = directorio.getPath();
                
                System.out.println(rutaArchivo);
                byte[] archivoDatosBytes = buffer;
                File archivoRespaldo = new File(rutaArchivo);
               
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(rutaArchivo));
                output.write(archivoDatosBytes,0,archivoDatosBytes.length);
                output.flush();
                output.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}