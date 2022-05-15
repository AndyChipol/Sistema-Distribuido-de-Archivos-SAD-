package cliente;

import componentes.ArchivoInformacion;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import interfaces.ServidorInterfaz;
import interfaces.ServidorListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;



public class ArchivoCliente extends javax.swing.JFrame implements WindowListener {

    public ArchivoInformacion archivoInfo;
    public DefaultListModel modelo;
    public static Cliente clMain;
    
    public ArchivoCliente() throws RemoteException {
        initComponents();
        modifyLabel();
        archivoInfo = new ArchivoInformacion("","");
        modelo = new DefaultListModel();
        jList1.setModel(modelo);
        
        /*========================================================================================*/
        /*   INICIO DEL CLIENTE*/
        clMain = new Cliente(jList1);
        String directorioRespaldo = JOptionPane.showInputDialog("Nombre De la Carpeta respaldo");
        clMain.comprobarDirectio(directorioRespaldo);
        clMain.iniciarListener(clMain);
        actualizarJlist(clMain.getListaArchivos());        
        clMain.iniciarClienteServidor();
        //System.out.print(cls.getListaArchivos());     
        addWindowListener(this);

        
    }
    
    public void actualizarJlist(List<ArchivoInformacion> ls){
        modelo.removeAllElements();
        for (int i = 0; i < ls.size(); i++)
            modelo.addElement(ls.get(i).getNombreArchivo());
        jList1.setModel(modelo);
    }
    
    public void modifyLabel(){
        TransferHandler th = new TransferHandler(){
            @Override
            public boolean canImport(JComponent jc, DataFlavor[] dfs) {
                return true; //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean importData(JComponent jc, Transferable t) {
                try {
                   List<File> archivos = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                   for(File archivo: archivos){
                       System.out.println(archivo.getName()+"\n\t"+archivo.getAbsolutePath());
                       archivoInfo.setNombreArchivo(archivo.getName());
                       archivoInfo.setUrlArchivo(archivo.getAbsolutePath());
                       direccion.setText(archivo.getName());
                   }
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }  
        };
        obtenerArchivo.setTransferHandler(th);      
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        obtenerArchivo = new javax.swing.JLabel();
        subirArchivo = new javax.swing.JButton();
        guardarArchivo = new javax.swing.JButton();
        direccion = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        verDireccionCompleta = new javax.swing.JButton();
        eliminarArchivo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Archivos Distribuidos ");
        setMinimumSize(new java.awt.Dimension(695, 524));
        setPreferredSize(new java.awt.Dimension(695, 524));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 450, 360));

        obtenerArchivo.setBackground(new java.awt.Color(255, 102, 51));
        obtenerArchivo.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        obtenerArchivo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        obtenerArchivo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(obtenerArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 150, 110));

        subirArchivo.setBorder(null);
        subirArchivo.setContentAreaFilled(false);
        subirArchivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        subirArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subirArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(subirArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 288, 150, 30));

        guardarArchivo.setBorder(null);
        guardarArchivo.setContentAreaFilled(false);
        guardarArchivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        guardarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(guardarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 338, 150, 30));

        direccion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        direccion.setForeground(new java.awt.Color(255, 255, 255));
        direccion.setText("ARCHIVO SELECCIONADO");
        getContentPane().add(direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 458, -1, -1));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 941, -1, -1));

        verDireccionCompleta.setBorder(null);
        verDireccionCompleta.setContentAreaFilled(false);
        verDireccionCompleta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        verDireccionCompleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verDireccionCompletaActionPerformed(evt);
            }
        });
        getContentPane().add(verDireccionCompleta, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, 150, 30));

        eliminarArchivo.setBorder(null);
        eliminarArchivo.setContentAreaFilled(false);
        eliminarArchivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eliminarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(eliminarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 237, 150, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Web 1280 – 1.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void subirArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subirArchivoActionPerformed
        try {
            if(!archivoInfo.getNombreArchivo().equals("")){
                clMain.enviarArchivo(archivoInfo,clMain);
                actualizarJlist(clMain.getListaArchivos());
            }else{
                String inicioMensaje = "<HTML><h2>"; //Inicializo el codigo HTML
                String mensajeUno = "No se selecciono un archivo";
                String finalMensaje = "</h2></HTML>\n"; 
                JOptionPane.showMessageDialog(
                    null,
                    inicioMensaje + mensajeUno + finalMensaje + inicioMensaje + finalMensaje,
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
                );
            }
            
            jList1.clearSelection();
        } catch (RemoteException ex) {
            Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_subirArchivoActionPerformed

    private void guardarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarArchivoActionPerformed
        //Abrir el seleccionador archivo y configurar que solamente
        //pueda abrir directorios y no seleccionar archivos
        JFileChooser seleccionarDirectorio = new JFileChooser();
        seleccionarDirectorio.setCurrentDirectory(new File("."));
        seleccionarDirectorio.setDialogTitle("Guardar El Archivo...");
        seleccionarDirectorio.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        seleccionarDirectorio.setAcceptAllFileFilterUsed(false);
        //Obtener la direccion donde se guardara el archivo
        if(seleccionarDirectorio.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            try {
                if(jList1.getSelectedValue() != null){
                    File directorio = seleccionarDirectorio.getSelectedFile();
                    System.out.println(directorio.getPath());
                    //Si se a guardado la direccion crear un archivo ahi para que se guarde la info
                    String rutaArchivo = directorio.getPath();
                    rutaArchivo = rutaArchivo + "\\" + jList1.getSelectedValue();
                    System.out.println(rutaArchivo);

                    File archivo = new File(rutaArchivo);

                    System.out.println("Archivo Seleccionado:"+jList1.getSelectedValue());
                    String archivoSeleccionado = jList1.getSelectedValue();
                    byte[] archivoDatosBytes = clMain.obtenerArchivoServidor(archivoSeleccionado);

                    System.out.print(archivoDatosBytes);
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(rutaArchivo));
                    output.write(archivoDatosBytes,0,archivoDatosBytes.length);
                    output.flush();
                    output.close();
                }else{
                    String inicioMensaje = "<HTML><h2>"; //Inicializo el codigo HTML
                    String mensajeUno = "Seleccione un Archivo de la lista";
                    String finalMensaje = "</h2></HTML>\n"; 
                    JOptionPane.showMessageDialog(
                        null,
                        inicioMensaje + mensajeUno + finalMensaje + inicioMensaje + finalMensaje,
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
                
                jList1.clearSelection();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_guardarArchivoActionPerformed

    private void verDireccionCompletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verDireccionCompletaActionPerformed
        if(!archivoInfo.getNombreArchivo().equals("")){
            String inicioMensaje = "<HTML><h2>"; //Inicializo el codigo HTML
            String mensajeUno = archivoInfo.getUrlArchivo();
            String finalMensaje = "</h2></HTML>\n"; 
            JOptionPane.showMessageDialog(null, inicioMensaje + mensajeUno + finalMensaje,"Direccion Completa",JOptionPane.INFORMATION_MESSAGE);
        }else{
            String inicioMensaje = "<HTML><h2>"; //Inicializo el codigo HTML
            String mensajeUno = "No se selecciono un archivo";
            String finalMensaje = "</h2></HTML>\n"; 
            JOptionPane.showMessageDialog(
                null,
                inicioMensaje + mensajeUno + finalMensaje + inicioMensaje + finalMensaje,
                "Warning",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_verDireccionCompletaActionPerformed

    private void eliminarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarArchivoActionPerformed
        System.out.println("BtnEliminar");
        // System.out.println("Archivo A Eliminar:" +jList1.getSelectedValue());
        //actualizarJlist(clMain.getListaArchivos());

        
             
        System.out.println("Archivo A Eliminar:" +jList1.getSelectedValue());
        String archivoSeleccionadoList = String.valueOf(jList1.getSelectedValue());


        try {
            //ELIMINAR ARCHIVO
            clMain.eliminarArchivo(archivoSeleccionadoList);
        } catch (RemoteException ex) {
            Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ACTUALIZAR LISA DE ARCHIVOS
        actualizarJlist(clMain.getListaArchivos());        

            
         
        
        
    }//GEN-LAST:event_eliminarArchivoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws RemoteException {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ArchivoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ArchivoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ArchivoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ArchivoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ArchivoCliente().setVisible(true);
                } catch (RemoteException ex) {
                    Logger.getLogger(ArchivoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel direccion;
    private javax.swing.JButton eliminarArchivo;
    private javax.swing.JButton guardarArchivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    public static javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel obtenerArchivo;
    private javax.swing.JButton subirArchivo;
    private javax.swing.JButton verDireccionCompleta;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosing(WindowEvent we) {
        clMain.cerrarCliente(clMain);
        System.out.println("Estoy cerrando");
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
       
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }
}
