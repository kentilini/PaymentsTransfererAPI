package com.kentilini.server.utils;

import com.kentilini.server.exception.NoResultException;
import com.kentilini.server.exception.NonUniqueResultException;

import java.util.List;

public class VerifyResult {
        public static void isOneResult(List input){
        if(input.size() > 1){
            throw new NonUniqueResultException("[UNEXPECTED BEHAVIOUR] Expected only one result, but was " + input.size());
        }else if(input.isEmpty()){
            throw new NoResultException("[UNEXPECTED BEHAVIOUR] Result is empty.");
        }
    }

}
