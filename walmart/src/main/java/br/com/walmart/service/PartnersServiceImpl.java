package br.com.walmart.service;

import br.com.walmart.entity.PartnersEntity;
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

    public List<PartnersEntity> findAllPartners() {return partnersRepository.findAll();}

    public PartnersEntity findByPartnersName(String partnersName) {return partnersRepository.findByPartnersName(partnersName);}

    public PartnersEntity findByProductName(String productName) {return partnersRepository.findByProductName(productName);}

    public boolean isPartnersExist(PartnersEntity partners) {return findByPartnersName(partners.getPartnersName()) != null;}

    public boolean isPartnersExistProduct(PartnersEntity partners) {return findByProductName(partners.getProductName()) != null;}

    public PartnersEntity findById(long id) {return partnersRepository.findOne(id);}

    public void savePartners(PartnersEntity partners) {
        partnersRepository.save(partners);
    }

    public void updatePartners(PartnersEntity partners) {
        savePartners(partners);
    }

    public void delete(long id) { PartnersEntity partnersRemover = findById(id);partnersRepository.delete(partnersRemover);}
}
