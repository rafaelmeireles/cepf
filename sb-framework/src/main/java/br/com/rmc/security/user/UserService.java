package br.com.rmc.security.user;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.Assert;

import br.com.rmc.BaseException;
import br.com.rmc.BaseService;
import br.com.rmc.service.EmailService;

public abstract class UserService<T extends User> extends BaseService<T> {
	
	private static final long serialVersionUID = 4044055203125228964L;
	
	@Autowired
	private EmailService emailService;
	
	public UserService(JpaRepository<T, Long> repository) {
		super(repository);
	}
	
	@Override
	protected void validateRequireds(T user) {
		super.validateRequireds(user);
		Assert.notNull(user.getUsername(), "Campo obrigatório 'LOGIN' não informado");
		Assert.notNull(user.getPassword(), "Campo obrigatório 'SENHA' não informado");
		Assert.notNull(user.getCpf(), "Campo obrigatório 'CPF' não informado");
		Assert.notNull(user.getActive(), "Campo obrigatório 'ATIVO' não informado");
		Assert.notNull(user.getRoles(), "Campo obrigatório 'PAPEL' não informado");
		Assert.notEmpty(user.getRoles(), "Campo obrigatório 'PAPEL' não informado");
	}
	
	@Override
	public T persist(T user) {
		String password = null;
		
		password = RandomStringUtils.randomAlphanumeric(12).toUpperCase();
		user.setPassword(hash(password));
		
		user = super.persist(user);
		
		emailService.send(user.getEmail(), "Senha de Acesso", "Olá " + user.getUsername() + " bem vindo ao CEPF,\n\nsegue abaixo a sua senha gerada automaticamente pelo sistema. \n\n \t" + password);
		
		return user;
	}
	
	@Override
	public T update(T user) {
		Optional<T> optionalUser = repository.findById(user.getId());

		user.setPassword(optionalUser.get().getPassword());
		user = super.update(user);
		
		return user;
	}
	
	public void resetPassword(T user) {
		String newPassword = RandomStringUtils.randomAlphanumeric(12).toUpperCase();
		user.setPassword(hash(newPassword));
		
		super.update(user);
		
		emailService.send(user.getEmail(), "Recuperação de Senha", "Olá " + user.getUsername() + ",\n\nsegue abaixo a nova senha gerada automaticamente pelo sistema. \n\n \t" + newPassword);
	}
	
	public void changePassword(T user) {
		user.setPassword(hash(user.getNewPassword()));
		super.update(user);
	}	
	
	private String hash(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(password.getBytes(Charset.forName("UTF8")));
			
			byte[] resultByte = messageDigest.digest();
			return new String(Hex.encode(resultByte));
		} catch (Exception e) {
			throw new BaseException("Falha ao processar a operação. Contacte o Administrador do Sistema.");
		}
	}	
}
