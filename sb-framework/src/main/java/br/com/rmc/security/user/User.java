package br.com.rmc.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.rmc.BaseEntity;
import br.com.rmc.security.role.Role;

@Entity
@Table(schema = "security")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = -5017824520579753974L;
		
	@NotNull
	@Column(nullable=false, unique = true)	
	private String username;
	
	@NotNull
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable=false)
	private String password;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String newPassword;
	
	@NotNull
	@JsonIgnore
	@Column(nullable=false)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean active;
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String cpf;
	
	@NotNull
	@Column(nullable = false, unique = true)
	@Email(message = "Falha na validação do campo email. Informe um valor válido.")
	private String email;
	
	@Transient
	private String token;
	
	@NotNull
	@JoinTable(
		schema="security",
		name = "user_has_role",
		joinColumns = @JoinColumn(nullable = false),
		inverseJoinColumns = @JoinColumn(nullable = false)
	)
//	@JsonIgnore
	@OrderColumn(name = "user_has_role_order")
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private List<Role> roles;

	public User() {
	}
	
	public User(String username) {
		this.username = username;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@PrePersist
	public void prePresist() {
		setActive(true);		
	}

	@Override
	@Transient
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(role -> {
					SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getCode());
					return simpleGrantedAuthority;
				}).collect(Collectors.toList());
	}

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}