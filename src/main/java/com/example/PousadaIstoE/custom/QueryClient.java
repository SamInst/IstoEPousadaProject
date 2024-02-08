package com.example.PousadaIstoE.custom;

import org.springframework.stereotype.Component;

@Component
public class QueryClient {

    public String autoCompleteName(String name){
        return
            """
            select 
            ip01.ip01_cpf as cpf,
            ip01.ip01_name as name
            from ip01_clients ip01
            where 1=1
            and ip01.ip01_name ilike '%""".concat(name).concat("%'");
    }
}
