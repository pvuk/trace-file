package com.trace.file.design.factory;

/**
 * Concrete Factory Class
 * 
 * @author PULIPATI VENKATA UDAYKIRAN
 * @since Tuesday 01-September-2026 20:47:58
 */
public class EightDigitFormatter implements FormatterInterface {

	public EightDigitFormatter(int digits) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String format(long last) {
		return String.format("%08d", last);
	}

}
