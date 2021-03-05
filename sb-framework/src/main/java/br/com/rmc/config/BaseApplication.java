package br.com.rmc.config;

import br.com.rmc.BaseException;

public class BaseApplication {

	public static void verifyRequiriments() {
		try {
			Class.forName("br.com.rmc.Application");
			Class.forName("br.com.rmc.ServletInitializer");
			Class.forName("br.com.rmc.security.UserApplication");
			Class.forName("br.com.rmc.security.UserApplicationController");
			Class.forName("br.com.rmc.security.UserApplicationRepository");
			Class.forName("br.com.rmc.security.UserApplicationService");
		} catch (ClassNotFoundException e) {
			String errorMessage = "Classe de implementação obrigatória não encontrada no classpath."
					+ " As seguintes classes precisam ser implementadas: \n"
					+ "\t br.com.rmc.Application \n"
					+ "\t br.com.rmc.ServletInitializer \n"
					+ "\t br.com.rmc.security.UserApplication \n"
					+ "\t br.com.rmc.security.UserApplicationController \n"
					+ "\t br.com.rmc.security.UserApplicationRepository \n"
					+ "\t br.com.rmc.security.UserApplicationService \n";
			throw new BaseException(errorMessage, e);
		}
	}
}