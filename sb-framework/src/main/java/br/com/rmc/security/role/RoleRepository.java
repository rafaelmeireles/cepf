package br.com.rmc.security.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
}
