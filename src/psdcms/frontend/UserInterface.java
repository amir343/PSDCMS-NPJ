package psdcms.frontend;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JTextField;

import java.util.*;
import javax.swing.JTable;
import java.awt.Label;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JButton jButton = null;
	private FrontendImpl FE; // @jve:decl-index=0:
	private JTextField jTextField = null;
	private JButton jButton1 = null;
	private JFrame frame;
	private File file;
	private JTable jTable = null;
	private Label label = null;
	private Label MatrixAlbl = null;
	private Label MatrixBlbl = null;
	/**
	 * This is the default constructor
	 */
	public UserInterface() {
		super();
		initialize();
		frame = this;

	}

	public void run(FrontendImpl fe) {
		FE = fe;

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1800, 3400);
		this.setContentPane(getJContentPane());
		this.setTitle("DCMS");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			MatrixBlbl = new Label();
			MatrixBlbl.setBounds(new Rectangle(39, 134, 288, 21));
			MatrixBlbl.setText("");
			MatrixBlbl.setVisible(false);
			MatrixAlbl = new Label();
			MatrixAlbl.setBounds(new Rectangle(39, 111, 285, 21));
			MatrixAlbl.setText("");
			MatrixAlbl.setVisible(false);
			label = new Label();
			label.setBounds(new Rectangle(39, 11, 581, 21));
			label.setText("Please write the path to a file containing two matrices and then click compute");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getJButton(), null);
			jPanel.add(getJTextField(), null);
			jPanel.add(getJButton1(), null);
			jPanel.add(getJTable(), null);
			jPanel.add(label, null);
			jPanel.add(MatrixAlbl, null);
			jPanel.add(MatrixBlbl, null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Compute!");
			jButton.setBounds(new Rectangle(37, 74, 233, 25));
			jButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (jTextField.getText().isEmpty() == true)
						return;
					if (file.isFile()) {
						int[][] matrixA = null, matrixB = null;

						try {
							Scanner scn = new Scanner(file);
							int row = scn.nextInt();
							int col = scn.nextInt();
							matrixA = new int[row][col];

							for (int i = 0; i < row; ++i) {
								for (int j = 0; j < col; ++j) {
									matrixA[i][j] = scn.nextInt();
								}
							}

							int row1 = scn.nextInt();
							int col1 = scn.nextInt();
							matrixB = new int[row1][col1];

							int height = row*jTable.getRowHeight();
							int width = 60*col1;
							
							jTable.setVisible(false);
							jTable = new JTable(row, col1);
							jTable.setBounds(new Rectangle(38, 156, width,
									height));
							jTable.setVisible(true);
							jPanel.add(jTable, null);
							MatrixAlbl.setText("Matrix A: "+ row + " x " + col);
							MatrixBlbl.setText("Matrix B: "+ row1 + " x " + col1);
							MatrixAlbl.setVisible(true);
							MatrixBlbl.setVisible(true);
							jPanel.repaint();
							
							for (int i = 0; i < row1; ++i) {
								for (int j = 0; j < col1; ++j) {

									matrixB[i][j] = scn.nextInt();
								}

							}

						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						FE.PublishTask(matrixA, matrixB);

					}
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setBounds(new Rectangle(38, 40, 460, 19));
		}
		return jTextField;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(514, 38, 107, 23));
			jButton1.setText("Browse");
			jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					JFileChooser jfc = new JFileChooser(
							"/data/1-Actions/DCMS-project/PSDCMS-NPJ/");
					jfc.showOpenDialog(frame);
					file = jfc.getSelectedFile();
					jTextField.setText(file.getAbsolutePath());
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
			jTable.setBounds(new Rectangle(13, 164, 375, 80));
			jTable.setVisible(false);
		}
		return jTable;
	}

	public synchronized void updateTable(int value, int row, int col) {
		// System.out.println("Received " + value + " row " + row + ", col " +
		// col);
		jTable.setValueAt(value, row, col);
		jTable.setVisible(false);
		jTable.setVisible(true);
		jPanel.repaint();
		frame.repaint();

	}

} 