package com.seata.example.seataclient1.service.impl;

import com.seata.example.seataclient1.dao.AccountInfoDao;
import com.seata.example.seataclient1.facade.Bank2Client;
import com.seata.example.seataclient1.service.AccountInfoService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    private Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    Bank2Client bank2Client;

    //张三转账
    @Override
    @GlobalTransactional(timeoutMills = 100000)
    @Transactional
    public void updateAccountBalance(String accountNo, Double amount) {
        logger.info("******** Bank1 Service Begin ... xid: {}" , RootContext.getXID());
        //张三扣减金额
        accountInfoDao.updateAccountBalance(accountNo,amount*-1);
        //向李四转账
        String remoteRst = bank2Client.transfer(amount);
        //远程调用失败
        if(remoteRst.equals("fallback")){
            throw new RuntimeException("bank1 下游服务异常");
        }
        //人为制造错误
        if(amount==3){
            throw new RuntimeException("bank1 make exception  3");
        }
    }
}
