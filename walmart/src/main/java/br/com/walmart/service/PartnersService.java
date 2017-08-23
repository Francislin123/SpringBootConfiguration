package br.com.walmart.service;

import br.com.walmart.entity.Partners;

import java.util.List;

public interface PartnersService {
    List<Partners> findAllPartners();

    void savePartners(Partners user);

    Partners findByPartnersName(String partnersName);

    Partners findByProductName(String productName);

    Partners findById(long id);

    boolean isPartnersExist(Partners partners);

    boolean isPartnersExistProduct(Partners partners);

    void updatePartners(Partners partners);

    void delete(Partners partners) throws Exception;
}
