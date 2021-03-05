package br.com.rmc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Util {

	public static String formatMoney(BigDecimal valor) {
		return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
	}
	
	public static BigDecimal toBigDecimal(String valor) {
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		
		DecimalFormat format = new DecimalFormat("#,##0.0#", symbols);
		format.setParseBigDecimal(true);
		
		try {
			return (BigDecimal) format.parse(valor.substring(3));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static String formatTelefone(String telefone) {
		try {
			return String.format("(%s) %s-%s",
					telefone.substring(0, 2),
					telefone.length() == 10 ? telefone.substring(2, 6) : telefone.substring(2, 7),
					telefone.length() == 10 ? telefone.substring(6) : telefone.substring(7));
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Valor do Telefone informado é inválido.", e);
		}
	}
	
	public static String formatCpf(String cpf) {
		try {
			return String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6), cpf.substring(6, 9), cpf.substring(9, 11));
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Valor do cpf informado é inválido.", e);
		}
	}
	
	public static LocalDate formatDate(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return LocalDate.parse(date, formatter);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Valor do data informada é inválido.", e);
		}
	}
	
	public static String formatLocalDateToString(LocalDate date) {
		try {
			return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Data inválida.", e);
		}
	}
	
	public static LocalDateTime formatDateTimeAtStartOfDay(String dateTime) {
		try {
			if (dateTime.length() == 9 || dateTime.length() == 10) {
				dateTime += " 00:00:00";
			}
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			return LocalDateTime.parse(dateTime, formatter).toLocalDate().atStartOfDay();
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Valor do cpf informado é inválido.", e);
		}
	}
	
	public static LocalDateTime formatDateTimeAtEndOfDay(String dateTime) {
		try {
			if (dateTime.length() == 9 || dateTime.length() == 10) {
				dateTime += " 00:00:00";
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime data = LocalDateTime.parse(dateTime, formatter).toLocalDate().atTime(LocalTime.MAX);
			dateTime = data.format(formatter);
			return LocalDateTime.parse(dateTime, formatter);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Valor do cpf informado é inválido.", e);
		}
	}
	
	/**
	 * Arredonda sempre a parte inteira para 5 ou 10. Ex: 11:15, 12:15, 16:20, 17:20
	 * @param valor
	 * @return
	 */
	public static BigDecimal round(BigDecimal valor) {
		BigDecimal valorRound = new BigDecimal(Math.ceil(valor.doubleValue()/5)*5);
		return valorRound;
	}
}