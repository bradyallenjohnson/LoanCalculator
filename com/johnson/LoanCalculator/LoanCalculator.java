package com.johnson.LoanCalculator;

public class LoanCalculator
{
	private double amount;    // loan amount
	private boolean amountSet;
	
	private double initialPayment;     // initial down payment

	private double interest;          // interest rate, something like 6.75
	private double interestPeriodic;  // this will be .0675/12
	private boolean interestSet;

	private double payment;       // payment amount
	private boolean paymentSet;

	private int periodTotal;     // total payment periods
	private boolean periodTotalSet;

	private int periodElapsed;   // number of elapsed payment periods
	private boolean periodElapsedSet;

	// These two are used if loans charge a fee opening fee or percentage
	private double openingFee;
	private double openingPercent;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
		this.amountSet = true;
	}

	public double getInitialPayment() {
		return initialPayment;
	}

	public void setInitialPayment(double initialPayment) {
		this.initialPayment = initialPayment;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
		this.interestPeriodic = interest/100.0/12.0;
		this.interestSet = true;
	}

	public double getOpeningFee() {
		return openingFee;
	}

	public void setOpeningFee(double openingFee) {
		this.openingFee = openingFee;
	}

	public double getOpeningPercent() {
		return openingPercent;
	}

	public void setOpeningPercent(double openingPercent) {
		this.openingPercent = openingPercent;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
		this.paymentSet = true;
	}

	public int getPeriodElapsed() {
		return periodElapsed;
	}

	public void setPeriodElapsed(int periodElapsed) {
		this.periodElapsed = periodElapsed;
		this.periodElapsedSet = true;
	}

	public int getPeriodTotal() {
		return periodTotal;
	}

	public void setPeriodTotal(int periodTotal) {
		this.periodTotal = periodTotal;
		this.periodTotalSet = true;
	}

	public void resetInput()
	{
		amountSet = false;
		interestSet = false;
		paymentSet = false;
		periodTotalSet = false;
		periodElapsedSet = false;
	}

	//
	//	 The actual calculation methods
	//

	/**
	 * Loan balance after n payments have been made:
	 *   B_n = A*(1+i)^n - (P/i)*((1+i)^n - 1)
	 */
	public double calculateLoanBalance() throws Exception
	{
	  if(!amountSet || !interestSet || !periodElapsedSet || !paymentSet)
	  {
	    throw new Exception("Must set loan amount, interest, and elapsed period for this calculation" );
	  }

	  return (amount*Math.pow((1+interestPeriodic), periodElapsed)) -
	         (payment/interestPeriodic)*(Math.pow((1+interestPeriodic), periodElapsed)-1);
	}

	/**
	 * Payment amount on a loan:
	 *   P = i*A / (1 - (1+i)^-N)
	 */
	public double calculatePayment() throws Exception
	{
	  if(!amountSet || !interestSet || !periodTotalSet)
	  {
	    throw new Exception("Must set loan amount, interest, and total period for this calculation" );
	  }

	  double totalAmount = amount - initialPayment;
	  totalAmount = totalAmount + openingFee + (totalAmount * (openingPercent/100.0));

	  return (interestPeriodic*totalAmount) /
	         (1 - Math.pow((1+interestPeriodic), (-1*periodTotal)));
	}

	/**
	 * Number of payments on a loan:
	 *   N = -log(1-i*A/P) / log(1+i)
	 *      (You can use any logarithm base, as long as both logs use the same base.)
	 *      Aunt Sally offers to lend you $3500 at 6% for that new home theater system you want.
	 *      If you pay her back $100 a month, how long will it take?
	 *      Solution:  6% per year is 0.5% per month, or 0.005. P = 100 and A = 3500. N = 38.57
	 */
	public double calculateNumberPayments() throws Exception
	{
	  if(!amountSet || !interestSet || !paymentSet)
	  {
	    throw new Exception("Must set loan amount, interest, and payment for this calculation" );
	  }

	  return (-1.0*Math.log10(1.0-(interestPeriodic*amount/payment))) /
	         Math.log10(1.0 + interestPeriodic);
	}

	/**
	 * Original loan amount:
	 *   A = (P/i)*(1 - (1+i)^-N)
	 */
	public double calculateLoanAmount() throws Exception
	{
	  if(!paymentSet || !interestSet || !periodTotalSet)
	  {
	    throw new Exception("Must set payment, interest, and total period for this calculation" );
	  }

	  return (payment/interestPeriodic) *
	         (1 - Math.pow((1+interestPeriodic), (-1*periodTotal)));
	}

	/**
	 * Interest Rate:
	 *   i = (((1 + P/A)^(1/q) - 1 )^q - 1)  NOTICE: This is an approximate not an exact solution
	 *   where q = log(1+1/N) / log(2)
	*/
	public double calculateInterestRate() throws Exception
	{
	  if(!amountSet || !paymentSet || !periodTotalSet)
	  {
	    throw new Exception("Must set amount, payment, and total period for this calculation" );
	  }

	  double q = Math.log10(1.0 + 1.0/periodTotal) / Math.log10(2.0);
	  double monthlyInterest = Math.pow((Math.pow((1.0 + payment/amount), 1.0/q) -1.0), q) -1.0;

	  return monthlyInterest*12*100;
	}

	public double calculateEffectiveInterestRate() throws Exception
	{
	  if(!amountSet || !periodTotalSet)
	  {
	    throw new Exception("Must set amount and total period for this calculation" );
	  }

	  double payment = calculatePayment();
	  double totalAmount = amount - initialPayment;

	  double q = Math.log10(1.0 + 1.0/periodTotal) / Math.log10(2.0);
	  double monthlyInterest = Math.pow((Math.pow((1.0 + payment/totalAmount), 1.0/q) -1.0), q) -1.0;

	  return monthlyInterest*12*100;
	}

	public String toString()
	{
	  StringBuilder buf = new StringBuilder();

	  //ss << "LoanCalculator set values:\n";

	  if(amountSet)
	  {
		  buf.append("Initial Amount:      ").append(amount).append("\n");
	  }

	  if(initialPayment != 0.0)
	  {
		  buf.append("Initial Payment:     ").append(initialPayment).append("\n");
		  buf.append("Actual Loan Amount:  ").append((amount - initialPayment)).append("\n");
	  }

	  if(interestSet)
	  {
		  buf.append("Yearly Interest:     ").append(interest).append("%\n");
	    //ss << "Yearly Interest:     " << interest
	    //   << "\nMonthly Interest:    " << interestPeriodic << "\n";
	  }

	  if(paymentSet)
	  {
		  buf.append("Monthly payment:     ").append(payment).append("\n");
	  }

	  if(periodTotalSet)
	  {
		  buf.append("Loan Period:         ").append(periodTotal).append(" months\n");
	  }

	  if(periodElapsedSet)
	  {
		  buf.append("Elapsed Period:      ").append(periodElapsed).append(" months\n");
	  }

	  if(openingFee != 0.0)
	  {
		  buf.append("Opening Fee:       ").append(openingFee).append("\n");
	  }

	  if(openingPercent != 0.0)
	  {
		  buf.append("Opening Fee %:       ").append(openingPercent).append("% = ");
	      buf.append(openingPercent/100*(amount - initialPayment)).append("\n");
	  }

	  return buf.toString();
	}
}
