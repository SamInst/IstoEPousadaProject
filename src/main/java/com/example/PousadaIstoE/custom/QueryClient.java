package com.example.PousadaIstoE.custom;

import org.springframework.stereotype.Component;

@Component
public class QueryClient {

    public String autoCompleteName(String name){
        return
            """
            select ip01.ip01_name as name
            from ip01_clients ip01
            where ip01.ip01_name ilike '%""".concat(name).concat("%'");
    }
}
