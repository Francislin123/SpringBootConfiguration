package br.com.walmart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.walmart.entity.Partners;
import br.com.walmart.repository.PartnersRepository;;

@Service("partnersService")
@Transactional
public class PartnersImpl implements PartnersService {

	@Autowired
	private PartnersRepository partnersRepository;

	public List<Partners> findAllPartners() {
		return partnersRepository.findAll();
	}

	public Partners findByPartnersName(String partnersName) {
		return partnersRepository.findByPartnersName(partnersName);
	}

	public boolean isPartnersExist(Partners partners) {
		return findByPartnersName(partners.getPartnersName()) != null;
	}

	public Partners findById(Long id) {
		return partnersRepository.findOne(id);
	}
	
	public void savePartners(Partners partners) {
		partnersRepository.save(partners);
	}

	public void updatePartners(Partners partners) {
		savePartners(partners);
	}

}
