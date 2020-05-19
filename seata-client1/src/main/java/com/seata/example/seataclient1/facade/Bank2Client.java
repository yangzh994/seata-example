package com.seata.example.seataclient1.facade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-client-bank2",fallback = Bank2ClientFallback.class)
public interface Bank2Client {

    @GetMapping("/bank2/transfer")
    String transfer(@RequestParam("amount") Double amount);
}

@Component
class Bank2ClientFallback implements Bank2Client{
    @Override
    public String transfer(Double amount) {

        return "fallback";
    }
}