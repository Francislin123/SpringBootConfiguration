package br.com.walmart.repository;

import br.com.walmart.entity.PartnersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnersRepository extends JpaRepository<PartnersEntity, Long>
{

    PartnersEntity findByPartnersName(String partnersName);

    PartnersEntity findByProductName(String productName);

}