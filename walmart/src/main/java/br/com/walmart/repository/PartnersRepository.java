package br.com.walmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.walmart.entity.Partners;

@Repository
public interface PartnersRepository extends JpaRepository<Partners, Long> {

	Partners findByPartnersName(String name);

}