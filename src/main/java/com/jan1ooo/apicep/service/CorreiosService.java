package com.jan1ooo.apicep.service;

import com.jan1ooo.apicep.ApiCepApplication;
import com.jan1ooo.apicep.exception.NoContentException;
import com.jan1ooo.apicep.exception.NotReadyException;
import com.jan1ooo.apicep.model.Address;
import com.jan1ooo.apicep.model.AddressStatus;
import com.jan1ooo.apicep.model.Status;
import com.jan1ooo.apicep.repository.AddressRepository;
import com.jan1ooo.apicep.repository.AddressStatusRepository;
import com.jan1ooo.apicep.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class CorreiosService {

    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressStatusRepository addressStatusRepository;

    @Autowired
    private SetupRepository setupRepository;

    @Value("${setup.on.startup}")
    private boolean setupOnStartup;

    public Status getStatus(){
        return this.addressStatusRepository.findById(AddressStatus.DEFAULT_ID)
                .orElse(AddressStatus.builder().status(Status.NEED_SETUP).build()).getStatus();
    }

    public Address getAddressByZipCode(String zipcode){
        try {
            if (!this.getStatus().equals(Status.READY)) {
                throw new NotReadyException("Service in installation, please wait for 5 minutes");
            }
            return addressRepository.findById(zipcode).get();
        }catch (NoSuchElementException e){
            throw new NoContentException("No content with zip code: " + zipcode);
        }
    }

    private void saveStatus(Status status){
        this.addressStatusRepository.save(AddressStatus.builder()
                .id(AddressStatus.DEFAULT_ID)
                .status(status)
                .build());
    }

    @EventListener(ApplicationStartedEvent.class)
    protected void setupOnStartup(){
        if(!setupOnStartup) return;

        try{
            this.setup();
        }catch (Exception e){
            ApiCepApplication.close(999);
            logger.error(".setupOnStartup() - Exception ", e);
        }
    }

    public void setup() throws IOException {
        logger.info("-----------");
        logger.info("-----------");
        logger.info("----------- SETUP RUNNING");
        logger.info("-----------");
        logger.info("-----------");
        if(this.getStatus().equals(Status.NEED_SETUP)){
            try {
                this.saveStatus(Status.SETUP_RUNNING);
                this.addressRepository.saveAll(setupRepository.getFromOrigin());
            }
            catch (Exception e){
                this.saveStatus(Status.NEED_SETUP);
                throw e;
            }
            this.saveStatus(Status.READY);
        }

        logger.info("-----------");
        logger.info("-----------");
        logger.info("----------- SERVICE READY");
        logger.info("-----------");
        logger.info("-----------");
    }


}
