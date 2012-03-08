package com.johnson.LoanCalculator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class LoanCalculatorGui extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final int MAIN_WIN_SIZE_X=800;
	private static final int MAIN_WIN_SIZE_Y=1000;
	private static final int TEXT_FIELD_WIDTH=5;
	private static final String CLEAR_ENTRY_STR="Clear Entries";
	private static final String CALCULATE_STR="Calculate";
	private LoanCalculator loanCalculator;

	private TextHandler textHandler;
	private RadioHandler radioHandler;
	private ButtonHandler buttonHandler;
	
	private NumberFormat numFormatter;

	private JLabel jlbAmount;
	private JLabel jlbInitialAmount;
	private JLabel jlbOpenPercent;
	//private JLabel jlbOpenFee;
	private JLabel jlbInterest;
	private JLabel jlbPayment;
	private JLabel jlbPeriod;

	private JTextField jtfAmount;
	private JTextField jtfInitialAmount;
	private JTextField jtfOpenPercent;
	//private JTextField jtfOpenFee;
	private JTextField jtfInterest;
	private JTextField jtfPayment;
	private JTextField jtfPeriod;

	private JRadioButton jrbCalcPayment;
	private JRadioButton jrbCalcAmount;
	private JRadioButton jrbCalcInterest;
	private JRadioButton jrbCalcNumPayments;
	private JRadioButton jrbCalcBalance;
	
	private JTextArea jtaResults;

	public LoanCalculatorGui(LoanCalculator loanCalculator)
	{
		this.loanCalculator = loanCalculator;
	}

	// the default ctor cant be called
	@SuppressWarnings("unused")
	private LoanCalculatorGui()
	{
	}

	public void init()
	{
		this.loanCalculator = new LoanCalculator();
		this.numFormatter = NumberFormat.getNumberInstance();
		this.numFormatter.setMaximumFractionDigits(2);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("LoanCalculator");
		this.setSize(MAIN_WIN_SIZE_X, MAIN_WIN_SIZE_Y);

		//
		// Action Listeners, one for each type of action
		//
		textHandler = new TextHandler();
		radioHandler = new RadioHandler();
		buttonHandler = new ButtonHandler();

		//
		// Some commonly used borders
		//
		Border raisedEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		Border spacedBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		Border compoundBorder = BorderFactory.createCompoundBorder(raisedEtchedBorder, spacedBorder);

		//
		// Radio buttons
		//
		jrbCalcPayment = new JRadioButton("Monthly Payment", true);
		jrbCalcAmount = new JRadioButton("Amount", false);
		jrbCalcInterest = new JRadioButton("Interest", false);
		jrbCalcNumPayments = new JRadioButton("Number of Payments", false);
		jrbCalcBalance = new JRadioButton("Balance", false);

		jrbCalcPayment.addActionListener(radioHandler);
		jrbCalcAmount.addActionListener(radioHandler);
		jrbCalcInterest.addActionListener(radioHandler);
		jrbCalcNumPayments.addActionListener(radioHandler);
		jrbCalcBalance.addActionListener(radioHandler);

		ButtonGroup calcTypeGroup = new ButtonGroup();
		calcTypeGroup.add(jrbCalcPayment);
		calcTypeGroup.add(jrbCalcAmount);
		calcTypeGroup.add(jrbCalcInterest);
		calcTypeGroup.add(jrbCalcNumPayments);
		calcTypeGroup.add(jrbCalcBalance);

		JPanel jplRadio = new JPanel();
		jplRadio.setLayout(new BoxLayout(jplRadio, BoxLayout.PAGE_AXIS));
		jplRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
		jplRadio.setBorder(BorderFactory.createTitledBorder(compoundBorder, "Calculation type"));
		jplRadio.add(jrbCalcPayment);
		jplRadio.add(jrbCalcAmount);
		jplRadio.add(jrbCalcInterest);
		jplRadio.add(jrbCalcNumPayments);
		jplRadio.add(jrbCalcBalance);

		//
		// Labels with corresponding Text fields
		// The labels are in one panel and text fields in another
		// these 2 panels are then wrapped in the jplFields panel
		//
		jlbAmount = new JLabel("Amount");
		jlbInitialAmount = new JLabel("Initial Payment");
		jlbOpenPercent = new JLabel("Loan Fee %");
		//jlbOpenFee = new JLabel("Loan Fee");
		jlbInterest = new JLabel("Interest %");
		jlbPayment = new JLabel("Payment");
		jlbPeriod = new JLabel("Months");

		jtfAmount = new JTextField();
		jtfInitialAmount = new JTextField();
		jtfOpenPercent = new JTextField();
		//jtfOpenFee = new JTextField();
		jtfInterest = new JTextField();
		jtfPayment = new JTextField();
		jtfPeriod = new JTextField();

		JPanel labelPanel = new JPanel();
		JPanel tfPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		tfPanel.setLayout(new BoxLayout(tfPanel, BoxLayout.Y_AXIS));

		createLabelAndTextField(jlbAmount, jtfAmount, labelPanel, tfPanel);
		createLabelAndTextField(jlbInitialAmount, jtfInitialAmount, labelPanel, tfPanel);
		createLabelAndTextField(jlbOpenPercent, jtfOpenPercent, labelPanel, tfPanel);
		//createLabelAndTextField(jlbOpenFee, jtfOpenFee, labelPanel, tfPanel);
		createLabelAndTextField(jlbInterest, jtfInterest, labelPanel, tfPanel);
		createLabelAndTextField(jlbPayment, jtfPayment, labelPanel, tfPanel);
		createLabelAndTextField(jlbPeriod, jtfPeriod, labelPanel, tfPanel);
		setFieldEnabled(jlbPayment, jtfPayment, false);

		JPanel jplFields = new JPanel();
		jplFields.setLayout(new BoxLayout(jplFields, BoxLayout.X_AXIS));
		jplFields.setAlignmentX(Component.LEFT_ALIGNMENT);
		jplFields.setBorder(BorderFactory.createTitledBorder(compoundBorder, "Input fields"));
		jplFields.add(labelPanel);
		jplFields.add(Box.createRigidArea(new Dimension(10, 0)));
		jplFields.add(tfPanel);
		//jplFields.add(Box.createRigidArea(new Dimension(20, 0)));

		//
		// Results area
		//
		jtaResults = new JTextArea(6, 20);
		jtaResults.setEditable(false);
		JScrollPane scrollPane = 
			new JScrollPane(jtaResults,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel jplResults = new JPanel();
		jplResults.setLayout(new BoxLayout(jplResults, BoxLayout.Y_AXIS));
		jplResults.setAlignmentX(Component.LEFT_ALIGNMENT);
		jplResults.setBorder(BorderFactory.createTitledBorder(compoundBorder, "Calculation Results"));
		jplResults.add(scrollPane);

		//
		// Buttons
		//
		JButton calculate = new JButton(CALCULATE_STR);
		calculate.setActionCommand(CALCULATE_STR);
		calculate.addActionListener(buttonHandler);

		JButton clear = new JButton(CLEAR_ENTRY_STR);
		clear.setActionCommand(CLEAR_ENTRY_STR);
		clear.addActionListener(buttonHandler);
		JPanel jplButtons = new JPanel();
		jplButtons.setLayout(new BoxLayout(jplButtons, BoxLayout.X_AXIS));
		jplButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
		jplButtons.setBorder(spacedBorder);
		jplButtons.add(Box.createRigidArea(new Dimension(10, 0)));
		jplButtons.add(calculate);
		jplButtons.add(Box.createRigidArea(new Dimension(10, 0)));
		jplButtons.add(clear);

		//
		// Populate the Main Panel/Frame
		//
		JPanel jplMain = new JPanel();
		jplMain.setLayout(new BoxLayout(jplMain, BoxLayout.Y_AXIS));
		jplMain.add(jplRadio);
		jplMain.add(jplFields);
		jplMain.add(jplResults);
		jplMain.add(jplButtons);
		jplMain.setBorder(spacedBorder);

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(jplMain);
		this.pack();
		this.setVisible(true);
	}

	private void createLabelAndTextField(JLabel jlb, JTextField jtf, JPanel jlbPanel, JPanel jtfPanel)
	{
		jlb.setAlignmentX(Component.LEFT_ALIGNMENT);
		jtf.setAlignmentX(Component.LEFT_ALIGNMENT);
		jtf.addActionListener(textHandler);
		jtf.setColumns(TEXT_FIELD_WIDTH);

		jlbPanel.add(jlb);
		jlbPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		jtfPanel.add(jtf);
	}

	private void setFieldEnabled(JLabel jlb, JTextField jtf, boolean enabled)
	{
		jlb.setEnabled(enabled);
		jtf.setEnabled(enabled);
	}

	private void getInputFields()
	{
		String fieldValue;
		
		try {
			fieldValue = jtfAmount.getText();
			if(fieldValue != null) {
				loanCalculator.setAmount(Double.parseDouble(fieldValue));
			}
		}
		catch(Exception ignore) {
		}

		try {
			fieldValue = jtfInitialAmount.getText();
			if(fieldValue != null) {
				loanCalculator.setInitialPayment(Double.parseDouble(fieldValue));
			}
		}

		catch(Exception ignore) {
		}
		try {
			fieldValue = jtfOpenPercent.getText();
			if(fieldValue != null) {
				loanCalculator.setOpeningPercent(Double.parseDouble(fieldValue));
			}
		}
		catch(Exception ignore) {
		}

		try {
			fieldValue = jtfInterest.getText();
			if(fieldValue != null) {
				loanCalculator.setInterest(Double.parseDouble(fieldValue));
			}
		}
		catch(Exception ignore) {
		}

		try {
			fieldValue = jtfPayment.getText();
			if(fieldValue != null) {
				loanCalculator.setPayment(Double.parseDouble(fieldValue));
			}
		}
		catch(Exception ignore) {
		}

		try {
			fieldValue = jtfPeriod.getText();
			if(fieldValue != null) {
				loanCalculator.setPeriodTotal(Integer.parseInt(fieldValue));
			}
		}
		catch(Exception ignore) {
		}
	}

	private class TextHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// This gets called if enter is pressed in a JTextField

			if (e.getSource() == jtfAmount) {
			}
			else if(e.getSource() == jtfInterest) {
			}
			else if(e.getSource() == jtfPayment) {
			}
			else if(e.getSource() == jtfPeriod) {
				ActionEvent event = new ActionEvent(e.getSource(), e.getID(), CALCULATE_STR);
				buttonHandler.actionPerformed(event);
			}
		}
	}

	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try {
				if (CLEAR_ENTRY_STR.equals(e.getActionCommand())) {
					//
					// clear each entry
					//
					jtfAmount.setText("");
					jtfInitialAmount.setText("");
					jtfOpenPercent.setText("");
					jtfInterest.setText("");
					jtfPayment.setText("");
					jtfPeriod.setText("");
					jtaResults.setText("");
				}
				else if(CALCULATE_STR.equals(e.getActionCommand())) {
					//
					// do the calculation
					//
					
					// Get all the values and set them on the loanCalculator
					loanCalculator.resetInput();
					getInputFields();
					StringBuilder buf = new StringBuilder();

					if(jrbCalcAmount.isSelected()) {
						buf.append("Initial Loan amount = ");
						buf.append(numFormatter.format(loanCalculator.calculateLoanAmount()));
					}
					else if(jrbCalcBalance.isSelected()) {
						buf.append("Loan Balance = ");
						buf.append(numFormatter.format(loanCalculator.calculateLoanBalance()));
					}
					else if(jrbCalcInterest.isSelected()) {
						buf.append("Yearly Interest Rate = ");
						buf.append(numFormatter.format(loanCalculator.calculateInterestRate()));
					}
					else if(jrbCalcNumPayments.isSelected()) {
						buf.append("Number of payments = ");
						buf.append(numFormatter.format(loanCalculator.calculateNumberPayments()));
					}
					else if(jrbCalcPayment.isSelected()) {
						double payment = loanCalculator.calculatePayment();
						buf.append("Monthly Payment = ");
						buf.append(numFormatter.format(payment));
						buf.append("\nTotal amt paid = ");
						buf.append(numFormatter.format(payment*loanCalculator.getPeriodTotal()));

						if(loanCalculator.getOpeningPercent() != 0.0 || loanCalculator.getOpeningFee() != 0.0)
						{
							buf.append("\nInterest with fees = ");
							buf.append(numFormatter.format(loanCalculator.calculateEffectiveInterestRate())).append("%");
						}
					}
					
					jtaResults.setText(buf.toString());
				}
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}

	private class RadioHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// First activate all the fields
			setFieldEnabled(jlbAmount, jtfAmount, true);
			setFieldEnabled(jlbInterest, jtfInterest, true);
			setFieldEnabled(jlbPayment, jtfPayment, true);
			setFieldEnabled(jlbPeriod, jtfPeriod, true);
			setFieldEnabled(jlbOpenPercent, jtfOpenPercent, true);
			setFieldEnabled(jlbInitialAmount, jtfInitialAmount, true);

			//
			// Now deactivate fields that dont apply to the calculation type
			//
			if (e.getSource() == jrbCalcPayment) {
				setFieldEnabled(jlbPayment, jtfPayment, false);
			}
			else if(e.getSource() == jrbCalcAmount) {
				setFieldEnabled(jlbAmount, jtfAmount, false);
				setFieldEnabled(jlbOpenPercent, jtfOpenPercent, false);
				setFieldEnabled(jlbInitialAmount, jtfInitialAmount, false);
			}
			else if(e.getSource() == jrbCalcInterest) {
				setFieldEnabled(jlbInterest, jtfInterest, false);
				setFieldEnabled(jlbOpenPercent, jtfOpenPercent, false);
				setFieldEnabled(jlbInitialAmount, jtfInitialAmount, false);
			}
			else if(e.getSource() == jrbCalcNumPayments) {
				setFieldEnabled(jlbPeriod, jtfPeriod, false);
				setFieldEnabled(jlbOpenPercent, jtfOpenPercent, false);
				setFieldEnabled(jlbInitialAmount, jtfInitialAmount, false);
			}
			else if(e.getSource() == jrbCalcBalance) {
			}
		}
	}
}
