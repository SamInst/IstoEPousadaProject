package com.example.PousadaIstoE.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = createInfo();
        info.setContact(createContact());
        info.setLicense(createLicense());
        return new OpenAPI()
                .info(info)
                .servers(createServer());
    }
    private List<Server> createServer(){
        var serverLocalHost = new Server();
        serverLocalHost.setUrl("http://localhost:8080/swagger-ui.html");
        serverLocalHost.setDescription("Local Host");
        return List.of(serverLocalHost);
    }

    private Info createInfo(){
        var info = new Info();
        info.setTitle("ISTO E POUSADA");
        info.setDescription("Microservice para realização de serviços de hotelaria");
        info.setVersion("v1");
        return info;
    }
    private Contact createContact(){
        var contato = new Contact();
        contato.setName("Sam Helson");
        contato.setEmail("sam04hel@gmail.com");
        contato.setUrl("https://mail.google.com/mail/?view=cm&to=sam04hel@gmail.com");
        return contato;
    }
    private License createLicense(){
        var licenca = new License();
        licenca.setName("Copyright (C) Isto E Pousada - Todos os direitos reservados ");
        licenca.setUrl("https://localhost:8080");
        return licenca;
    }
}
