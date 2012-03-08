package com.johnson.LoanCalculator;

public class LoanCalculatorMain {
	private static final String ARG_CALC_BALANCE      = "-cb";
	private static final String ARG_CALC_PAYMENT      = "-cp";
	private static final String ARG_CALC_NUMPAYMENTS  = "-cn";
	private static final String ARG_CALC_AMOUNT       = "-ca";
	private static final String ARG_CALC_INTEREST     = "-ci";

	private static final String ARG_PAYMENT           = "-p";
	private static final String ARG_PERIOD_TOTAL      = "-N";
	private static final String ARG_PERIOD_ELAPSED    = "-n";
	private static final String ARG_AMOUNT            = "-a";
	private static final String ARG_INITIAL_PAYMENT   = "-ai";
	private static final String ARG_INTEREST          = "-i";
	private static final String ARG_OPENFEE           = "-of";
	private static final String ARG_OPENPERCENT       = "-op";

	private static final String ARG_HELP_H = "-h";
	private static final String ARG_HELP = "-help";
	private static final String ARG_HELP_QMARK = "-?";

	private static final int CALC_UNKNOWN = 0;
	private static final int CALC_BALANCE = 100;
	private static final int CALC_PAYMENT = 101;
	private static final int CALC_NUMPAYMENTS = 102;
	private static final int CALC_AMOUNT = 103;
	private static final int CALC_INTEREST = 104;

	private static void printUsage()
	{
	  System.out.println(
		   "\nUsage:\n" +
		   "\t If started with no arguments, the GUI will be launched\n" +
	       "Input values:\n\t" +
	       ARG_AMOUNT + " Set the initial amount. Ej: 19300\n\t" +
	       ARG_INITIAL_PAYMENT + " Set the initial payment, loan will be for (initial amount - initial payment) Ej: 1000, Default 0.0\n\t" +
	       ARG_INTEREST + " Set the yearly interest rate. Ej: 6.75\n\t" +
	       ARG_PAYMENT + " Set the monthly loan payment. Ej: 325.67\n\t" +
	       ARG_PERIOD_TOTAL + " Set the total loan period in months. Ej: 60\n\t" +
	       ARG_PERIOD_ELAPSED + " Set the elapsed period in months. Ej: 32\n\t" +
	       ARG_OPENFEE + " Set fees for opening the loan. Ej: 100, Default 0.0\n\t" +
	       ARG_OPENPERCENT + " Set fees for opening the loan, charged as a percentage. Ej: 2.75%, Default 0.0%\n" +

	       "Calculations, one of the following:\n\t" +
	       ARG_CALC_PAYMENT +
	       " Calculate the monthly loan payment, given: loan amount, loan period, and interest\n\t" +

	       ARG_CALC_AMOUNT +
	       " Calculate the initial loan amount, given: monthly payment, loan period, and interest\n\t" +

	       ARG_CALC_INTEREST +
	       " Calculate the loan interest, given: loan amount, loan period, and monthly payment\n\t" +

	       ARG_CALC_NUMPAYMENTS +
	       " Calculate the number of payments needed to pay a loan, given: loan amount, monthly payment, interest\n\t" +

	       ARG_CALC_BALANCE +
	       " Calculate the loan balance after making several payments, given:\n\t" +
	       "\t\t loan amount, interest, monthly payment and number of monthly payments made so far\n" +

	       "Other options:\n\t" +
	       ARG_HELP + " Display this message\n");
	}

	//
	//	 Simple Command line parser
	//
	private static int parseCommandLine(String[] args, LoanCalculator calculator) throws Exception
	{
	  if(args.length < 3)
	  {
	    if(args.length == 2 && ARG_HELP == args[1])
	    {
	      printUsage();
	      return CALC_UNKNOWN;
	    }

	    throw new Exception("Invalid number of command line arguments: " + args.length);
	  }

	  int ct = CALC_UNKNOWN;
	  for(int i = 0; i < args.length; i++)
	  {
	    String argvStr = args[i];

	       // input values
	    if(ARG_AMOUNT.equals(argvStr))
	    {
	      calculator.setAmount(Double.parseDouble(args[++i]));
	    }
	    else if(ARG_INITIAL_PAYMENT.equals(argvStr))
	    {
	      calculator.setInitialPayment(Double.parseDouble(args[++i]));
	    }
	    else if(ARG_INTEREST.equals(argvStr))
	    {
	      calculator.setInterest(Double.parseDouble(args[++i]));
	    }
	    else if(ARG_PAYMENT.equals(argvStr))
	    {
	      calculator.setPayment(Double.parseDouble(args[++i]));
	    }
	    else if(ARG_PERIOD_TOTAL.equals(argvStr))
	    {
	      calculator.setPeriodTotal(Integer.parseInt(args[++i]));
	    }
	    else if(ARG_PERIOD_ELAPSED.equals(argvStr))
	    {
	      calculator.setPeriodElapsed(Integer.parseInt(args[++i]));
	    }
	    else if(ARG_OPENFEE.equals(argvStr))
	    {
	      calculator.setOpeningFee(Double.parseDouble(args[++i]));
	    }
	    else if(ARG_OPENPERCENT.equals(argvStr))
	    {
	      calculator.setOpeningPercent(Double.parseDouble(args[++i]));
	    }

	       // calculation types
	    else if(ARG_CALC_AMOUNT.equals(argvStr))
	    {
	      ct = CALC_AMOUNT;
	    }
	    else if(ARG_CALC_BALANCE.equals(argvStr))
	    {
	      ct = CALC_BALANCE;
	    }
	    else if(ARG_CALC_INTEREST.equals(argvStr))
	    {
	      ct = CALC_INTEREST;
	    }
	    else if(ARG_CALC_NUMPAYMENTS.equals(argvStr))
	    {
	      ct = CALC_NUMPAYMENTS;
	    }
	    else if(ARG_CALC_PAYMENT.equals(argvStr))
	    {
	      ct = CALC_PAYMENT;
	    }

	       // Other args
	    else if(ARG_HELP.equals(argvStr) || ARG_HELP_H.equals(argvStr) || ARG_HELP_QMARK.equals(argvStr))
	    {
	      printUsage();
	    }
	    else
	    {
	      throw new Exception("Invalid command line argument: " + argvStr );
	    }
	  }

	  return ct;
	}


	public static void main(String[] args) {
		LoanCalculator calculator = new LoanCalculator();
		
		//
		// If started with no commands, launch the GUI
		//
		if(args.length == 0)
		{
			LoanCalculatorGui gui = new LoanCalculatorGui(calculator);
			gui.init();
			return;
		}

		//
		// Parse the command line arguments
		//
		int ct;
		try
		{
			ct = parseCommandLine(args, calculator);
		}
		catch(Exception e)
		{
			System.err.println("Error parsing command line: " + e.getMessage());
			printUsage();
			return;
		}

		try
		{
			StringBuilder buf = new StringBuilder();
			buf.append("\n");

			if(ct == CALC_BALANCE)
			{
				buf.append("Loan Balance = ").append(calculator.calculateLoanBalance());
			}
			else if(ct == CALC_PAYMENT)
			{
				double payment = calculator.calculatePayment();
				buf.append("Monthly Payment    = ").append(payment);
				buf.append("\nTotal amt paid     = ").append(payment*calculator.getPeriodTotal());

				if(calculator.getOpeningPercent() != 0.0 || calculator.getOpeningFee() != 0.0)
				{
					buf.append("\nInterest with fees = ").append(calculator.calculateEffectiveInterestRate()).append("%");
				}
			}
			else if(ct == CALC_NUMPAYMENTS)
			{
				buf.append("Number of payments = ").append(calculator.calculateNumberPayments());
			}
			else if(ct == CALC_AMOUNT)
			{
				buf.append("Initial Loan amount = ").append(calculator.calculateLoanAmount());
			}
			else if(ct == CALC_INTEREST)
			{
				buf.append("Yearly Interest Rate = ").append(calculator.calculateInterestRate()).append("%");
			}
			else
			{
				System.err.println("Unrecognized calculation type: " + ct + ", exiting");
				return;
			}

			// print the values set on the calculator
			buf.append("\n");
			buf.append(calculator.toString());
			System.out.println(buf.toString());
		}
		catch(Exception e)
		{
			System.err.println("Error executing loan calculator: " + e.getMessage());
			//printUsage();
			//return;
		}
	}

}
