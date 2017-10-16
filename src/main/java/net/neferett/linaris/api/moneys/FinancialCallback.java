package net.neferett.linaris.api.moneys;

public interface FinancialCallback<V> {

	public void done(double result, double amount, Throwable error);

}