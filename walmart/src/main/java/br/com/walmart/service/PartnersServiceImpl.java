package br.com.walmart.service;

import br.com.walmart.entity.Partners;
import br.com.walmart.repository.PartnersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("partnersService")
@Transactional
public class PartnersServiceImpl implements PartnersService {

    @Autowired
    private PartnersRepository partnersRepository;

    public List<Partners> findAllPartners() {return partnersRepository.findAll();}

    public Partners findByPartnersName(String partnersName) {return partnersRepository.findByPartnersName(partnersName);}

    public Partners findByProductName(String productName) {return partnersRepository.findByProductName(productName);}

    public boolean isPartnersExist(Partners partners) {return findByPartnersName(partners.getPartnersName()) != null;}

    public boolean isPartnersExistProduct(Partners partners) {return findByProductName(partners.getProductName()) != null;}

    public Partners findById(long id) {return partnersRepository.findOne(id);}

    public void savePartners(Partners partners) {
        partnersRepository.save(partners);
    }

    public void updatePartners(Partners partners) {
        savePartners(partners);
    }

    public void delete(long id) { Partners partnersRemover = findById(id);partnersRepository.delete(partnersRemover);}
}
