package com.trace.file.design.factory;

/**
 * Concrete Factory Class
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 01-September-2026 20:48:39
 */
public class FourDigitFormatter implements FormatterInterface {

	public FourDigitFormatter(int digits) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String format(long last) {
		return String.format("%04d", last);
	}

}
