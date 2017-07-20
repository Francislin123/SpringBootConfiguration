package br.com.walmart.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.walmart.entity.Partners;
import br.com.walmart.service.PartnersService;
import br.com.walmart.util.CustomErrorType;

@Controller
@RequestMapping("/api")
public class HomeController {

	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	PartnersService partnersService;
	
	// ------------------- Method to list partners details ------------------------------------------------
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/list/", method = RequestMethod.GET)
	public ResponseEntity<List<Partners>> listAllPartners() {
		logger.info("List of partners ", partnersService.toString());
		List<Partners> partners = partnersService.findAllPartners();
		if (partners.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Partners>>(partners, HttpStatus.OK);
	}
	
	// ------------------- Method to find by partners id ------------------------------------------------
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPartners(@PathVariable("id") long id) {
		logger.info("Fetching Partners with id {}", id);
		Partners partners = partnersService.findById(id);
		if (partners == null) {
			logger.error("Partners with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Partners with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Partners>(partners, HttpStatus.OK);
	}

	// ------------------- Method to create partners ------------------------------------------------
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/create/", method = RequestMethod.POST)
	public ResponseEntity<?> createPartners(@RequestBody Partners partners, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Partners : {}", partners);

		if (partnersService.isPartnersExist(partners)) {
			logger.error("Unable to create. A Partners with name {} already exist", partners.getPartnersName());
			return new ResponseEntity(
		    new CustomErrorType("Unable to create. A Partners with name " + partners.getPartnersName() + " already exist."),HttpStatus.CONFLICT);
		}

		partnersService.savePartners(partners);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// ------------------- Method to update partners ------------------------------------------------
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/find/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePartners(@PathVariable("id") long id, @RequestBody Partners partners) {
		logger.info("Updating Partners with id {}", id);

		Partners currentPartners = partnersService.findById(id);

		if (currentPartners == null) {
			logger.error("Unable to partners. Partners with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Partners with id " + id + " not found."),HttpStatus.NOT_FOUND);
		}

		currentPartners.setPartnersName(partners.getPartnersName());
		currentPartners.setProductName(partners.getProductName());

		partnersService.updatePartners(currentPartners);
		return new ResponseEntity<Partners>(currentPartners, HttpStatus.OK);
	}
}
