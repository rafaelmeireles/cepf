package br.com.rmc;

//import java.beans.PropertyDescriptor;
import java.io.Serializable;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.lang.reflect.TypeVariable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.persistence.EntityManager;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//import javax.persistence.Query;
//import javax.validation.constraints.NotNull;
//
//import org.apache.commons.beanutils.PropertyUtils;
//import org.apache.commons.lang.reflect.FieldUtils;
//
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Example;
//import org.hibernate.criterion.Example.PropertySelector;
//import org.hibernate.criterion.MatchMode;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//import org.hibernate.internal.CriteriaImpl.Subcriteria;


public abstract class BaseServiceSig{//<T extends BaseEntity> implements Serializable {

	private static final long serialVersionUID = 1842224239037307878L;
//
//	//		@Log
//	protected Logger log;
//
//	private Class<T> entityClass;
//
//	@Inject
//	private EntityManager entityManager;
//
//	public EntityManager getEntityManager() {
//		return entityManager;
//	}
//
//	public void setEntityManager(EntityManager entityManager) {
//		this.entityManager = entityManager;
//	}
//
//	public boolean isManaged(T entity) {
//		return entity.getId() != null;
//	}
//
//	public void persist(T entity) {
//		entity.validate();
//		getEntityManager().persist(entity);
//		getEntityManager().flush();
//	}
//
//	public T update(T entity) {
//
//		T updatedEntity = entity;
//
//		updatedEntity.validate();
//
//		if (!getEntityManager().contains(entity)) {
//			updatedEntity = getEntityManager().merge(entity);
//		}
//
//		getEntityManager().flush();
//
//		return updatedEntity;
//	}
//
//	public void update(List<T> entities) {
//		for (T entity : entities) {
//			update(entity);
//		}
//	}
//
//	public void remove(T entity) throws BaseException {
//		getEntityManager().remove(entity);
//		getEntityManager().flush();
//	}
//
//	public void remove(List<T> list) throws BaseException {
//		for (T t : list) {
//			remove(t);
//		}
//	}
//
//	public void refresh(T entity) {
//		getEntityManager().refresh(entity);
//	}
//
//	public T findSingleResult(T entity) throws BaseException {
//		return findSingleResult(entity, MatchMode.ANYWHERE, null);
//	}
//
//	public T findSingleResult(T entity, CriteriaVisitor visitor) throws BaseException {
//		return findSingleResult(entity, MatchMode.ANYWHERE, visitor);
//	}
//
//	public T findSingleResult(T entity, MatchMode matchMode) throws BaseException {
//		return findSingleResult(entity, matchMode, null); 
//	}
//
//	public T findSingleResult(T entity, MatchMode matchMode, CriteriaVisitor visitor) throws BaseException {
//		try {
//			return this.find(entity, matchMode, visitor).get(0);
//		} catch (IndexOutOfBoundsException e) {
//			return null;
//		}		
//	}
//
//	/**
//	 * Obtem o valor do maior registro de chave primária que pode
//	 * ser utilizado na tabela.
//	 * 
//	 * @param id
//	 * @return
//	 */
//	public Long max() {
//		Query query = getEntityManager().createQuery(" SELECT max(id) " +
//				" FROM "+getEntityClass().getCanonicalName()+" o");	
//		return (Long) query.getSingleResult();
//	}
//
//	public Long max(CriteriaVisitor visitor) {
//		return max(null, visitor);
//	}
//
//	public Long max(String property, CriteriaVisitor visitor) {
//		Criteria criteria = getSession().createCriteria(getEntityClass()).setProjection(Projections.max(property != null ? property : "id"));
//
//		if (visitor != null) {
//			visitor.visit(criteria);
//		}
//
//		return (Long) criteria.uniqueResult();
//	}	
//
//	public Integer count(T instance) {
//		return count(instance, MatchMode.ANYWHERE, null);
//	}
//
//	public Integer count(T instance, MatchMode m) {
//		return count(instance, m, null);
//	}
//
//	public Integer count(T instance, CriteriaVisitor visitor) {
//		return count(instance, MatchMode.ANYWHERE, visitor);
//	}
//
//	public Integer count(T instance, MatchMode m, CriteriaVisitor visitor) {
//
//		Criteria criteria = null;
//
//		try {
//			criteria = createCriteria(instance, null, true, m, visitor);
//		} catch (BaseException e) {
//			e.printStackTrace();
//		}
//
//		if (visitor != null) {
//			visitor.visit(criteria);
//		}
//
//		criteria = criteria.setProjection(Projections.rowCount());
//
//		return ((Long) criteria.uniqueResult()).intValue();
//	}	
//
//	public T find(Long id) throws BaseException {
//		T instance = null;
//		instance = getEntityManager().find(getEntityClass(), id);
//		return instance;
//	}
//
//	public List<T> findAll() throws BaseException {
//		return findAll(null, true);
//	}
//
//	public List<T> findAll(String orderBy, boolean asc) throws BaseException {
//		return findAll(orderBy, asc, null, null);
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<T> findAll(String orderBy, boolean asc, Integer firstResult,
//			Integer maxResults) {
//
//		Criteria criteria = getSession().createCriteria(getEntityClass());
//
//		if (orderBy != null) {
//			Order order = asc ? Order.asc(orderBy) : Order.desc(orderBy);
//			criteria.addOrder(order.ignoreCase());
//		}
//
//		if (firstResult != null && maxResults != null) {
//			criteria.setFirstResult(firstResult);
//			criteria.setMaxResults(maxResults);
//		}
//
//		return criteria.list();
//	}
//
//	public List<T> find(T instance) throws BaseException {
//		return find(instance, null, true);
//	}
//
//	public List<T> find(T instance, CriteriaVisitor visitor) throws BaseException {
//		return find(instance, null, true, visitor);
//	}	
//
//	public List<T> find(T instance, MatchMode m) throws BaseException {
//		return find(instance, null, true, m);
//	}
//
//	public List<T> find(T instance, MatchMode m, CriteriaVisitor visitor) throws BaseException {
//		return find(instance, null, true, m, visitor);
//	}
//
//	public List<T> find(T instance, String orderBy, boolean asc)
//			throws BaseException {
//		return find(instance, orderBy, asc, MatchMode.ANYWHERE, null);
//	}
//
//	public List<T> find(T instance, String orderBy, boolean asc, CriteriaVisitor visitor)
//			throws BaseException {
//		return find(instance, orderBy, asc, null, null, visitor);
//	}
//
//	public List<T> find(T instance, String orderBy, boolean asc,
//			MatchMode m) throws BaseException {
//		return find(instance, orderBy, asc, null, null, m);
//	}
//
//	public List<T> find(T instance, String orderBy, boolean asc,
//			MatchMode m, CriteriaVisitor visitor) throws BaseException {
//		return find(instance, orderBy, asc, null, null, m, visitor);
//	}	
//
//	public List<T> find(T instance, String orderBy, boolean asc,
//			Integer firstResult, Integer maxResults) throws BaseException {
//		return find(instance, orderBy, asc, firstResult,
//				maxResults, MatchMode.ANYWHERE);
//	}
//
//	public List<T> find(T instance, String orderBy, boolean asc,
//			Integer firstResult, Integer maxResults, CriteriaVisitor visitor) throws BaseException {
//		return find(instance, orderBy, asc, firstResult,
//				maxResults, MatchMode.ANYWHERE, visitor);
//	}
//
//	public List<T> find(T instance, String orderBy, boolean asc,
//			Integer firstResult, Integer maxResults, MatchMode m) throws BaseException {
//		return find(instance, orderBy, asc, firstResult,
//				maxResults, m, null);
//	}	
//
//	@SuppressWarnings("unchecked")
//	public List<T> find(T instance, String orderBy, boolean asc,
//			Integer firstResult, Integer maxResults, MatchMode m, CriteriaVisitor visitor)
//					throws BaseException {
//
//		Criteria criteria = createCriteria(instance, orderBy, asc, m, visitor);
//
//		if (firstResult != null) {
//			criteria.setFirstResult(firstResult);
//		}
//
//		if (maxResults != null) {
//			criteria.setMaxResults(maxResults);
//		}
//
//		if (visitor != null) {
//			visitor.visit(criteria);
//		}
//
//		return criteria.list();
//	}
//
//	public List<T> findByNamedQuery(String queryName,
//			Map<String, Object> parameters) throws BaseException {
//
//		return findByNamedQuery(queryName, parameters, null, null);
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<T> findByNamedQuery(String queryName,
//			Map<String, Object> parameters, Integer firstResult,
//			Integer maxResults) throws BaseException {
//
//		Query query = getEntityManager().createNamedQuery(queryName);
//
//		for (String name : parameters.keySet()) {
//			query.setParameter(name, parameters.get(name));
//		}
//
//		if (firstResult != null) {
//			query.setFirstResult(firstResult);
//		}
//		if (maxResults != null) {
//			query.setMaxResults(maxResults);
//		}
//		return (List<T>) query.getResultList();
//	}
//
//	public Object findSingleResultByNamedQuery(String queryName,
//			Map<String, Object> parameters) throws BaseException {
//
//		Query query = getEntityManager().createNamedQuery(queryName);
//
//		for (String name : parameters.keySet()) {
//			query.setParameter(name, parameters.get(name));
//		}
//		return query.getSingleResult();
//
//	}
//
//	/**
//	 * Método de obtenção de uma instancia de critiria para consulta por
//	 * parametros.
//	 * 
//	 * @param instance
//	 *            - Contendo parametros de pesquisa.
//	 * @param visitor 
//	 * @return Criteria
//	 */
//	private Criteria createCriteria(T instance, String orderBy, boolean asc,
//			MatchMode matchMode, CriteriaVisitor visitor) throws BaseException {
//
//		Criteria criteria = getSession().createCriteria(instance.getClass());
//
//		if (instance.getId() != null) {
//			criteria.add(Restrictions.idEq(instance.getId()));
//		} else {
//			Example example = createExample(instance, matchMode);
//			criteria.add(example);
//			List<String> order = new ArrayList<String>();
//			if (orderBy != null) {
//				order = Arrays.asList(orderBy.split("\\."));
//			}
//			createSubCriteria(criteria, instance, order, asc, matchMode, false);
//		}
//
//		return criteria;
//	}
//
//	/**
//	 * Composição do subcriterio para busca com restrições em entidades filhas.
//	 * 
//	 * @param criteria
//	 * @param instance
//	 * @param order
//	 * @param asc
//	 * @param matchMode
//	 * @throws BaseException
//	 */
//	private void createSubCriteria(Criteria criteria, BaseEntity instance,
//			List<String> order, boolean asc, MatchMode matchMode,
//			boolean leftJoin) throws BaseException {
//
//		boolean isLeftJoin = leftJoin;
//		/**
//		 * Acrescentando no criterio ordenação se existir
//		 */
//		if (order.size() == 1) {
//			Order o = asc ? Order.asc(order.get(0)) : Order.desc(order.get(0));
//			criteria.addOrder(o.ignoreCase());
//		}
//
//		/**
//		 * Acrescentando subcritério para subentidades relacionadas
//		 */
//		try {
//			PropertyDescriptor[] propertyDescriptors = PropertyUtils
//					.getPropertyDescriptors(instance);
//
//			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//				Method method = propertyDescriptor.getReadMethod();
//				if (method != null
//						&& (method.isAnnotationPresent(ManyToOne.class) || method
//								.isAnnotationPresent(OneToOne.class))) {
//
//					String propertyName = propertyDescriptor.getName();
//					Object sub = PropertyUtils.getProperty(instance,
//							propertyName);
//					/**
//					 * Se a subentidade existir e for do tipo BaseEntity adicionar
//					 * como subcriteria.
//					 */
//					if (sub != null && sub instanceof BaseEntity) {
//						Criteria subCriteria = null;
//						// adicionar inner join apenas quando entidade notnull
//						if (isLeftJoin
//								|| !method.isAnnotationPresent(NotNull.class)) {
//							subCriteria = criteria.createCriteria(propertyName,
//									Criteria.LEFT_JOIN);
//							isLeftJoin = true;
//						} else {
//							subCriteria = criteria.createCriteria(propertyName);
//						}
//
//						Long id = ((BaseEntity) sub).getId();
//						if (id != null) {
//							subCriteria.add(Restrictions.idEq(id));
//						} else {
//							Example ex = createExample(sub, matchMode);
//							subCriteria.add(ex);
//							List<String> subOrder = new ArrayList<String>();
//							if (order.size() > 1
//									&& order.get(0).equals(propertyName)) {
//								subOrder = order.subList(1, order.size());
//							}
//							createSubCriteria(subCriteria, (BaseEntity) sub,
//									subOrder, asc, matchMode, isLeftJoin);
//						}
//					}
//
//				}
//			}
//
//		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//			log.log(Level.SEVERE,
//					" Framework: "+ e.getClass() +" criando criteria para classe "+ instance.getClass() +" : ID ["+ instance.getId() +"]",
//					e);
//			throw new BaseException(e);
//		}
//
//	}
//
//	/**
//	 * Cria um example para a busca por criteria acrescentando nas condições da
//	 * busca:
//	 * 
//	 * - Excluir zeros - Ignorar se caixa alta ou baixa - Strings com LIKE -
//	 * Elimina String nulas ou vazias
//	 * 
//	 * @param instance
//	 *            Instancia usada para examplo da busca
//	 * @param matchMode
//	 *            Tipo de match usado para operações de LIKE
//	 * @return Example para a busca
//	 */
//	@SuppressWarnings("serial")
//	private Example createExample(Object instance, MatchMode matchMode) {
//
//		Example example = Example.create(instance).excludeZeroes()
//				.enableLike(matchMode).ignoreCase();
//
//		/**
//		 * Adicionando property selector para eliminar propriedades nulas e
//		 * string vazias no critério da busca.
//		 */
//		PropertySelector notNullOrEmptySelector = new PropertySelector() {
//			public boolean include(Object object, String propertyName,
//					org.hibernate.type.Type type) {
//				return object != null
//						&& (!(object instanceof String) || !((String) object)
//								.equals(""));
//			}
//		};
//		example.setPropertySelector(notNullOrEmptySelector);
//
//		return example;
//
//	}
//
//	@SuppressWarnings("unchecked")
//	public Criteria getSubCriteria(Criteria criteria, String path) {
//		try {
//			List<Subcriteria> subCriterias = (List<Subcriteria>)FieldUtils.readField(criteria, "subcriteriaList", true);
//			for (Subcriteria subCriteria : subCriterias) {
//				if (subCriteria.getPath().equals(path)) {
//					return subCriteria;
//				}
//			}
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//			throw new BaseException("Falha ao processar a operação. Contacte o Administrador do Sistema.");
//		}
//		return null;
//	}
//
//	/**
//	 * Metodo de obtenção de um session.
//	 * 
//	 * @return Session
//	 */
//	protected Session getSession() {
//		Session session = (Session) getEntityManager().getDelegate();
//		return session;
//	}
//
//	@SuppressWarnings("unchecked")
//	public Class<T> getEntityClass() {
//		if (entityClass == null) {
//			Type type = getClass().getGenericSuperclass();
//			if (type instanceof ParameterizedType) {
//				ParameterizedType paramType = (ParameterizedType) type;
//				if (paramType.getActualTypeArguments().length == 2) {
//					if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
//						throw new RuntimeException(
//								"Não foi possível descobrir a entidade por reflexão.");
//					} else {
//						entityClass = (Class<T>) paramType
//								.getActualTypeArguments()[1];
//					}
//				} else {
//					entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
//				}
//			} else {
//				throw new RuntimeException(
//						"Não foi possível descobrir a entidade por reflexão.");
//			}
//		}
//		return entityClass;
//	}
}