package br.com.walmart.controllers;

import br.com.walmart.entity.Partners;
import br.com.walmart.service.PartnersService;
import br.com.walmart.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.NonUniqueResultException;
import java.util.List;

@RestController
@RequestMapping(HomeController.V1_PARTNERS)
public class HomeController {

    public static final String V1_PARTNERS = "/v1/partners";
    public static final String V1_PARTNERS_DELETE = "/v1/partners/delete/";

    public static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    PartnersService partnersService;

    // ------------------- Method to list partners details -----------------------------------------------------------//

    @RequestMapping(value = "/list/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Partners>> listAllPartners() {
        logger.info("List of partners", partnersService.toString());
        List<Partners> partners = partnersService.findAllPartners();
        if (partners == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<Partners>>(partners, HttpStatus.OK);
        }
    }

    // ------------------- Method to find by partners id -------------------------------------------------------------//

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getPartners(@PathVariable("id") long id) {
        logger.info("Fetching Partners with id {}", id);
        Partners partners = partnersService.findById(id);
        if (partners == null) {
            logger.error("Partners with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Partners with id " + id + " not found"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Partners>(partners, HttpStatus.OK);
    }

    // ------------------- Method to create partners  ----------------------------------------------------------------//

    @RequestMapping(value = "/create/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPartners(@RequestBody Partners partners, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Partners : {}", partners);

        if (partnersService.isPartnersExist(partners)) {
            logger.error("Unable to create. A Partners with name {} already exist", partners.getPartnersName());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Partners with name " +
                    partners.getPartnersName() + " already exist."), HttpStatus.CONFLICT);
        }

        partnersService.savePartners(partners);

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    // ------------------- Method to update partners ----------------------------------------------------------------//

    @RequestMapping(value = "/find/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updatePartners(@PathVariable("id") long id, @RequestBody Partners partners) {

        try {

            Partners currentPartners = partnersService.findById(id);

            if (currentPartners == null) {
                return new ResponseEntity(new CustomErrorType
                        ("Unable to upate. Partners with id " + id + " not found."), HttpStatus.NOT_FOUND);
            }
            if (partnersService.isPartnersExist(partners) && partnersService.isPartnersExistProduct(partners)) {
                return new ResponseEntity(new CustomErrorType
                        ("Unable to upate. Partners with partners " + partners + " exist "), HttpStatus.CONFLICT);
            }

            currentPartners.setPartnersName(partners.getPartnersName());
            currentPartners.setProductName(partners.getProductName());

            partnersService.updatePartners(currentPartners);
            return new ResponseEntity(currentPartners, HttpStatus.OK);

        } catch (NonUniqueResultException e) {
            return new ResponseEntity(new CustomErrorType("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> deletePartnesById(@PathVariable("id") long id) throws Exception {
        logger.info("Delete Partners with id {}", id);
        try {
            partnersService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new CustomErrorType("Partner with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
    }
}
