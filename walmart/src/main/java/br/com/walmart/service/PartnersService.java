package br.com.walmart.service;

import br.com.walmart.entity.PartnersEntity;

import java.util.List;

public interface PartnersService {
    List<PartnersEntity> findAllPartners();

    void savePartners(PartnersEntity user);

    PartnersEntity findByPartnersName(String partnersName);

    PartnersEntity findByProductName(String productName);

    PartnersEntity findById(long id);

    boolean isPartnersExist(PartnersEntity partners);

    boolean isPartnersExistProduct(PartnersEntity partners);

    void updatePartners(PartnersEntity partners);

    void delete(long id);
}
