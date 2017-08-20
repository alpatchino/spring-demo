package com.patrick.demo.bootstrap;


import com.patrick.demo.domain.PredictionModel;
import com.patrick.demo.domain.TrainingData;
import com.patrick.demo.domain.User;
import com.patrick.demo.repositories.ModelRepository;
import com.patrick.demo.repositories.TrainingDataRepository;
import com.patrick.demo.repositories.UserRepository;
import com.patrick.demo.utils.Constants;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class UserLoader implements ApplicationListener<ContextRefreshedEvent>  {

	//TODO: finish model repo

	@Autowired
    private UserRepository userRepository;
    @Autowired
	private ModelRepository modelRepository;
    @Autowired
	private TrainingDataRepository trainingDataRepository;

    private Logger log = Logger.getLogger(UserLoader.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

		/**
		 * 	Initialise test users
		 */
		User admin = new User();
    	admin.setAccountType(Constants.ACCOUNT_TYPE_ADMIN);
    	admin.setApiKey("0000-1111-abcd-edfg");
    	admin.setCreatedOn(new Date());
    	admin.setLastLogin(new Date());
    	admin.setFailedLoginAttempts(0);
    	admin.setFirstName("Admin");
    	admin.setLastName("Admin");
    	admin.setEmail("admin@admin.com");
    	admin.setUsername("admin");
    	admin.setPassword("password");
    	userRepository.save(admin);
    	log.info("Saved user - " + admin.getId());
    	
    	User premiumUser = new User();
    	premiumUser.setAccountType(Constants.ACCOUNT_TYPE_PREMIUM);
    	premiumUser.setApiKey("1234-5678-abcd-edfg");
    	premiumUser.setCreatedOn(new Date());
    	premiumUser.setLastLogin(new Date());
    	premiumUser.setFailedLoginAttempts(0);
    	premiumUser.setFirstName("Premium");
    	premiumUser.setLastName("User");
    	premiumUser.setEmail("premium@user.com");
    	premiumUser.setUsername("premiumuser");
    	premiumUser.setPassword("password");
    	userRepository.save(premiumUser);
    	log.info("Saved user - " + premiumUser.getId());

		User freeUser = new User();
		freeUser.setAccountType(Constants.ACCOUNT_TYPE_FREE);
		freeUser.setApiKey("abcd-efgh-1234-5678");
		freeUser.setCreatedOn(new Date());
		freeUser.setLastLogin(new Date());
		freeUser.setFailedLoginAttempts(0);
		freeUser.setFirstName("Free");
		freeUser.setLastName("User");
		freeUser.setEmail("free@user.com");
		freeUser.setUsername("freeuser");
		freeUser.setPassword("password");
		userRepository.save(freeUser);
		log.info("Saved user - " + freeUser.getId());


		/**
		 *  Initialise training data
		 */
		TrainingData ANDData = new TrainingData();
		ANDData.setCols(2);
		ANDData.setRows(4);
		ANDData.setFiletype(Constants.DATA_TYPE_CSV);
		ANDData.setLocation(Constants.DATA_LOCATION_LOCAL);
		ANDData.setDescription("Training data for Logical AND");
		trainingDataRepository.save(ANDData);

		TrainingData ORData = new TrainingData();
		ORData.setCols(2);
		ORData.setRows(4);
		ORData.setFiletype(Constants.DATA_TYPE_CSV);
		ORData.setLocation(Constants.DATA_LOCATION_LOCAL);
		ORData.setDescription("Training data for Logical OR");
		trainingDataRepository.save(ORData);



		/**
		 *  Initialise test models
		 */
		PredictionModel ANDModel = new PredictionModel();
    	ANDModel.setCreatedBy(admin);
    	ANDModel.setDescription("Logical AND of two inputs");
    	ANDModel.setEndpointUri("/and");
    	ANDModel.setName("Logical AND");
    	ANDModel.setStatus(Constants.MODEL_STATUS_OFFLINE);
    	ANDModel.setModelLocation("/models/1.ser");
    	ANDModel.setData(ANDData);
    	modelRepository.save(ANDModel);
    	
    	PredictionModel ORModel = new PredictionModel();
    	ORModel.setCreatedBy(admin);
    	ORModel.setDescription("Logical OR of two inputs");
    	ORModel.setEndpointUri("/or");
    	ORModel.setName("Logical OR");
    	ORModel.setStatus(Constants.MODEL_STATUS_ONLINE);
		ORModel.setModelLocation("/models/2.ser");
		ORModel.setData(ORData);
    	modelRepository.save(ORModel);



	}
}

