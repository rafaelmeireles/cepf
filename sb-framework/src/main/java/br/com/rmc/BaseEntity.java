package br.com.rmc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 4788813917978594591L;
	
	/*
	 * Identificador da entidade.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;

	/*
	 * Version da entidade.
	 */
	@Version
	@Column(nullable = false, columnDefinition = "bigint DEFAULT 0")
	private Long version;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void validate() {
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + "[id=" + id + "]";
	}
	
//	public boolean isEmpty() {
//		for (Field field : this.getClass().getDeclaredFields()) {
//			try {
//				field.setAccessible(true);
//					System.out.println("field.get(this):" + field.get(this));
//					
//					if (field.get(this) != null) {
//						field.getName().equals("serialVersionUID")
//						System.out.println("field.get(this) != null:" + field.get(this) != null);
//						if (field.get(this) instanceof BaseEntity) {
//							System.out.println("field.get(this) instanceof BaseEntity:" + true);
//							BaseEntity entity = (BaseEntity) field.get(this);
//							entity.isEmpty();
//						}
//					}
//				}
//			} catch (Exception e) {
//			}
//		}
//		return true;
//	}
}