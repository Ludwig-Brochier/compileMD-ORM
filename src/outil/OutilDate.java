package outil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OutilDate {
	private static String _todayDate;
	public static String getTodayDate() {
		Date date = new Date(); // Date du jour
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Format de la date
		_todayDate = dateFormat.format(date);		
		return _todayDate;
	}
}
