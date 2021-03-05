package br.com.rmc;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
public abstract class BaseService<T extends BaseEntity> implements Serializable {

	private static final long serialVersionUID = 1828223622214103561L;
	
	protected JpaRepository<T, Long> repository;
	
	public BaseService(JpaRepository<T, Long> repository) {
		this.repository = repository;
	}
	
	protected JpaRepository<T, Long> getRepository() {
		return this.repository;
	}	
	
	protected void validateRequireds(T entity) {
		Assert.notNull(entity, "O registro informado está nulo.");
	}	

	public T persist(T entity) {
		validateRequireds(entity);
		return this.repository.save(entity);
	}
	
	public T update(T entity) {
		validateRequireds(entity);
		return this.repository.save(entity);
	}	
	
	public void delete(T entity) {
		validateRequireds(entity);
		this.repository.delete(entity);
	}	

//	@Override
//	public Page<T> findAll(Pageable pageable) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> S save(S entity) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Optional<T> findById(Long id) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public boolean existsById(Long id) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public long count() {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public void deleteById(Long id) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void delete(T entity) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void deleteAll(Iterable<? extends T> entities) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void deleteAll() {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public <S extends T> Optional<S> findOne(Example<S> example) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> long count(Example<S> example) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> boolean exists(Example<S> example) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public void deleteAllInBatch() {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void deleteInBatch(Iterable<T> arg0) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public List<T> findAll() {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public List<T> findAll(Sort arg0) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> List<S> findAll(Example<S> arg0) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> List<S> findAll(Example<S> arg0, Sort arg1) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public List<T> findAllById(Iterable<Long> arg0) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public void flush() {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public T getOne(Long arg0) {
//		throw new BaseException("Operação não permitida. Método não implementado na camada service.");
//	}
//
//	@Override
//	public <S extends T> List<S> saveAll(Iterable<S> arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <S extends T> S saveAndFlush(S arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
