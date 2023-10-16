package br.com.gwenilorac.biblioteca.app.client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyInternalFrame extends JInternalFrame{

	MyInternalFrame(String nome){
		super(nome, true,
		          true, 
		          true, 
		          true);
		    
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		add(panel);
		setPreferredSize(new Dimension(600, 400));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		
		setVisible(true);
	}
	    
}
