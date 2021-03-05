package br.com.rmc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.rmc.config.BaseApplication;

@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		BaseApplication.verifyRequiriments();
		SpringApplication.run(Application.class, args);
	}
	
//	@Bean
//	public Formatter<LocalDate> localDateFormatter() {
//		return new Formatter<LocalDate>() {
//			@Override
//			public String print(LocalDate localDate, Locale locale) {
//				return DateTimeFormatter.ISO_DATE.format(localDate);
//			}
//
//			@Override
//			public LocalDate parse(String data, Locale locale) throws ParseException {
//				return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//			}
//			
//		};
//	}
	
//	@Bean
//	public FormattingConversionService conversionService() {
//		DefaultFormattingConversionService conversionService = 
//				new DefaultFormattingConversionService(false);
//		
//		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
//		registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//		registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
//		registrar.registerFormatters(conversionService);
//		return conversionService;
//	}

}