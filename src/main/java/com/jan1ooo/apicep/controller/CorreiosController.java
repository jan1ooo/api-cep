package com.jan1ooo.apicep.controller;

import com.jan1ooo.apicep.model.Address;
import com.jan1ooo.apicep.service.CorreiosService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorreiosController {

    @Autowired
    private CorreiosService service;

    @GetMapping("/status")
    public String getStatus(){
        return "Service status: " + service.getStatus();
    }

    @GetMapping("/zipcode/{zipcode}")
    public Address getAddressByZipCode(@PathVariable String zipcode){
        return this.service.getAddressByZipCode(zipcode);
    }
}
