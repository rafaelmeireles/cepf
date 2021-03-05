package br.com.rmc.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication, Long> {
}
