/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Usuario
 */
public class VentanaBuscaminas extends javax.swing.JFrame {

    int filas = 15;
    int columnas = 20;
    int numMinas = 30;
    int contadorMinas = 0;
    int contadorInterrogaciones = 0;

    Boton[][] arrayBotones = new Boton[filas][columnas];

    /**
     * Creates new form VentanaBuscaminas
     */
    public VentanaBuscaminas() {
	initComponents();
	setSize(800, 600);
	ventanaReset.setSize(500, 200);
	setResizable(false);
	labelGanador.setVisible(false);
	labelPerdedor.setVisible(false);
	getContentPane().setLayout(new GridLayout(filas, columnas));
	for (int i = 0; i < filas; i++) {
	    for (int j = 0; j < columnas; j++) {
		Boton boton = new Boton(i, j);
		boton.setBorder(null);
		getContentPane().add(boton);
		arrayBotones[i][j] = boton;
		boton.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent evt) {
			botonPulsado(evt);
		    }
		});
	    }
	}
	ponMinas(numMinas);
	cuentaminas();
    }

    private void botonPulsado(MouseEvent e) {
	Boton miBoton = (Boton) e.getComponent();
	if (e.getButton() == MouseEvent.BUTTON3) {
	    if ((miBoton.getText() == "" ||miBoton.getText() == "m") && miBoton.isEnabled() ) {
		miBoton.setText("?");
		compruebaMinas(true, miBoton.getI(), miBoton.getJ());
	    } else if (miBoton.getText() == "?") {
		miBoton.setText("");
		compruebaMinas(false, miBoton.getI(), miBoton.getJ());
	    }

	}
	if (e.getButton() == MouseEvent.BUTTON1) {
	    if (miBoton.getMina() == 1) {
		this.setVisible(false);
		ventanaReset.setVisible(true);
		labelPerdedor.setVisible(true);
	    } else {
		if (miBoton.getText() == "?") {
		    miBoton.setText("");
		    compruebaMinas(false, miBoton.getI(), miBoton.getJ());
		}
		liberaHuecos(miBoton.getI(), miBoton.getJ());
	    }
	}

    }

    private void ponMinas(int numeroMinas) {
	Random r = new Random();
	for (int i = 0; i < numeroMinas; i++) {
	    int f = r.nextInt(filas);
	    int c = r.nextInt(columnas);

	    if (arrayBotones[f][c].getMina() == 0) {
		arrayBotones[f][c].setMina(1);
//		arrayBotones[f][c].setText("m");
	    } else {
		ponMinaEnOtroLugar();
	    }
	}
    }

    private void ponMinaEnOtroLugar() {
	Random r = new Random();
	int f = r.nextInt(filas);
	int c = r.nextInt(columnas);
	if (arrayBotones[f][c].getMina() == 0) {
	    arrayBotones[f][c].setMina(1);
//	      arrayBotones[f][c].setText("m");
	} else {
	    ponMinaEnOtroLugar();
	}
    }

    //Cuentaminas es un metdodo que par cada boton calcula el numero de minas
    //que tiene alrededor
    private void cuentaminas() {
	//TODO: falta por hacer que calcule lasminas en el borde exterior
	//Probar a hacerlo con un try catch
	int minas = 0;

	for (int i = 0; i < filas; i++) {
	    for (int j = 0; j < columnas; j++) {
		//	1 2 3
		//	4 X 5
		//	6 7 8
		try {
		    minas += arrayBotones[i - 1][j - 1].getMina();//1

		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i - 1][j].getMina();//2

		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i - 1][j + 1].getMina();//3
		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i][j - 1].getMina();//4

		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i][j + 1].getMina();//5
		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i + 1][j - 1].getMina();//6   		   
		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i + 1][j].getMina();//7
		} catch (Exception e) {
		}
		try {
		    minas += arrayBotones[i + 1][j + 1].getMina();//8
		} catch (Exception e) {
		}
		arrayBotones[i][j].setNumeroMinasAlrededor(minas);
		//TODO: comentar la siguiente parte para que no aparezcan los numeros  al iniciar
//		if (arrayBotones[i][j].getMina() == 0 && minas != 0) {
//		    arrayBotones[i][j].setText(String.valueOf(minas));
//		}
		minas = 0;
	    }
	}
    }

    private void liberaHuecos(int i, int j) {

	if (arrayBotones[i][j].getNumeroMinasAlrededor() != 0) {
	    if (arrayBotones[i][j].getText() == "?") {
		arrayBotones[i][j].setText("");
		compruebaMinas(false, i, j);
	    }
	    arrayBotones[i][j].setEnabled(false);
	    arrayBotones[i][j].setFocusPainted(false);
	    arrayBotones[i][j].setText(String.valueOf(arrayBotones[i][j].getNumeroMinasAlrededor()));
	} else {
	    for (int auxI = -1; auxI <= 1; auxI++) {
		for (int auxJ = -1; auxJ <= 1; auxJ++) {
		    try {
			if (arrayBotones[i + auxI][j + auxJ].isEnabled()) {
			    if (arrayBotones[i + auxI][j + auxJ].getMina() == 0 && arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor() == 0) {
				if (arrayBotones[i + auxI][j + auxJ].getText() == "?") {
				    arrayBotones[i + auxI][j + auxJ].setText("");
				    compruebaMinas(false, i + auxI, j + auxJ);
				}
				arrayBotones[i + auxI][j + auxJ].setEnabled(false);
				arrayBotones[i + auxI][j + auxJ].setFocusPainted(false);
				liberaHuecos(i + auxI, j + auxJ);
			    }
			    if (arrayBotones[i + auxI][j + auxJ].getMina() == 0 && arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor() != 0) {
				if (arrayBotones[i + auxI][j + auxJ].getText() == "?") {
				    arrayBotones[i + auxI][j + auxJ].setText("");
				    compruebaMinas(false, i + auxI, j + auxJ);
				}
				arrayBotones[i + auxI][j + auxJ].setText(String.valueOf(arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor()));
				arrayBotones[i + auxI][j + auxJ].setEnabled(false);
				arrayBotones[i + auxI][j + auxJ].setFocusPainted(false);

			    }
			}
		    } catch (Exception e) {
		    }
		}
	    }
	}
    }

    /*
    Este metodo ira recibiendo interrogaciones e ira incrementando o disminuyendo
    el contador de minas. Cuando el contador se iguale a las minas, termina la partida.
     */
    private void compruebaMinas(boolean add, int i, int j) {
	if (add) {
	    contadorInterrogaciones++;
	    if (arrayBotones[i][j].getMina() == 1) {
		contadorMinas++;
	    }
	} else {
	    contadorInterrogaciones--;
	    if (arrayBotones[i][j].getMina() == 1) {
		contadorMinas--;
	    }
	}
	if ((contadorMinas == numMinas) && (contadorInterrogaciones == numMinas)) {
	    this.setVisible(false);
	    ventanaReset.setVisible(true);
	    labelGanador.setVisible(true);
	}
    }
     
    private void reiniciaElementos(){
	for (int i = 0; i < filas; i++) {
	    for (int j = 0; j < columnas; j++) {
		arrayBotones[i][j].setEnabled(true);
		arrayBotones[i][j].setText("");
		arrayBotones[i][j].setMina(0);
		arrayBotones[i][j].setNumeroMinasAlrededor(0);
		contadorInterrogaciones = 0;
		contadorMinas = 0;
	    }
	}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ventanaReset = new javax.swing.JDialog();
        botonReiniciar = new javax.swing.JButton();
        labelGanador = new javax.swing.JLabel();
        labelPerdedor = new javax.swing.JLabel();

        ventanaReset.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botonReiniciar.setText("Reiniciar");
        botonReiniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonReiniciarMousePressed(evt);
            }
        });
        ventanaReset.getContentPane().add(botonReiniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, -1, -1));

        labelGanador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelGanador.setText("HAS GANADO, FELICIDADES");
        labelGanador.setPreferredSize(new java.awt.Dimension(400, 20));
        ventanaReset.getContentPane().add(labelGanador, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        labelPerdedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPerdedor.setText("HAS PERDIDO POR IMBECIL");
        labelPerdedor.setPreferredSize(new java.awt.Dimension(400, 20));
        labelPerdedor.setRequestFocusEnabled(false);
        ventanaReset.getContentPane().add(labelPerdedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 364, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonReiniciarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonReiniciarMousePressed
	ventanaReset.setVisible(false);
	labelGanador.setVisible(false);
	labelPerdedor.setVisible(false);
	this.setVisible(true);
	
	reiniciaElementos();
	ponMinas(numMinas);
	cuentaminas();
    }//GEN-LAST:event_botonReiniciarMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/* Set the Nimbus look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new VentanaBuscaminas().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonReiniciar;
    private javax.swing.JLabel labelGanador;
    private javax.swing.JLabel labelPerdedor;
    private javax.swing.JDialog ventanaReset;
    // End of variables declaration//GEN-END:variables
}
