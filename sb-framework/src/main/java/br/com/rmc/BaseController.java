package br.com.rmc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseController<T extends BaseEntity> {
	
	@Autowired
	protected ObjectMapper objectMapper;

	protected JpaRepository<T, Long> repository;
	protected BaseService<T> service;

	public BaseController(JpaRepository<T, Long> repository) {
		this.repository = repository;
	}

	public BaseController(JpaRepository<T, Long> repository, BaseService<T> service) {
		this.repository = repository;
		this.service = service;
	}
	
	protected JpaRepository<T, Long> getRepository() {
		return this.repository;
	}	
	
	protected BaseService<T> getService() {
		return this.service;
	}
	
	protected ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase();
	}

	@GetMapping
	public ResponseEntity<List<T>> find(
			@RequestParam(value = "entity", required = false) Optional<String> entityStr,
			@RequestParam(value = "from", required = false) Optional<String> strFrom,
			@RequestParam(value = "to", required = false) Optional<String> strTo) {
		try {
			List<T> resultList = new ArrayList<T>();

			if (!entityStr.isPresent()) {
				resultList = repository.findAll();
			} else {
				T entity = this.objectMapper.readValue(entityStr.get(), getEntityClass());
				resultList = repository.findAll(Example.of(entity, getExampleMatcher()));
			}

			return ResponseEntity.ok(resultList);
		} catch (BaseException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<T> find(@PathVariable Long id) {
		try {
			Optional<T> optionalEntity = repository.findById(id);
//			Optional<T> optionalEntity = repository.findById(id).orElseThrow(() => new BaseException("O registro informado não foi encontrado."));
			return ResponseEntity.ok(optionalEntity.get());
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O registro informado não foi encontrado.", e);
		} catch (BaseException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}

	@PostMapping
	public ResponseEntity<T> persist(@RequestBody T entity) {
		try {			
			return ResponseEntity.ok(service.persist(entity));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}

	@PutMapping
	public ResponseEntity<T> update(@RequestBody T entity) {
		try {			
			return ResponseEntity.ok(service.update(entity));
		} catch (BaseException | IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<T> delete(@PathVariable Long id) {
		try {
			Optional<T> entity = repository.findById(id);
			service.delete(entity.get());
			return ResponseEntity.ok(entity.get());
		} catch (BaseException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		Class<T> entityClass = null;
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				if (paramType.getActualTypeArguments().length == 2) {
					if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
						throw new RuntimeException(
								"Não foi possível descobrir a entidade por reflexão.");
					} else {
						entityClass = (Class<T>) paramType
								.getActualTypeArguments()[1];
					}
				} else {
					entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
				}
			} else {
				throw new RuntimeException(
						"Não foi possível descobrir a entidade por reflexão.");
			}
		}
		return entityClass;
	}
}