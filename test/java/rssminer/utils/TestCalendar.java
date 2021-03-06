package rssminer.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestCalendar {

	public static void main(String[] args) {

		GregorianCalendar gc = new GregorianCalendar();

		gc.add(GregorianCalendar.DAY_OF_YEAR, -1);
		System.out.println(gc.getTime());

		gc.add(GregorianCalendar.DAY_OF_YEAR, -6);
		System.out.println(gc.getTime());

		gc.add(GregorianCalendar.DAY_OF_YEAR, -23);
		System.out.println(gc.getTime());

		gc.add(GregorianCalendar.DAY_OF_YEAR, -440);
		System.out.println(gc.getTime());

	}
}
