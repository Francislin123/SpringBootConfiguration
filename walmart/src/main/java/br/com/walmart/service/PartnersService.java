package br.com.walmart.service;

import java.util.List;

import br.com.walmart.entity.Partners;

public interface PartnersService {

	List<Partners> findAllPartners();
	
	void savePartners(Partners user);

	Partners findByPartnersName(String partnersName);
	
	Partners findById(Long id);

	boolean isPartnersExist(Partners partners);
	
	void updatePartners(Partners partners);

}
