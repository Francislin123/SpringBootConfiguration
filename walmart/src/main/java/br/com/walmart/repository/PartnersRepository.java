package br.com.walmart.repository;

import br.com.walmart.entity.Partners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnersRepository extends JpaRepository<Partners, Long>
{

    Partners findByPartnersName(String partnersName);

    Partners findByProductName(String productName);

}