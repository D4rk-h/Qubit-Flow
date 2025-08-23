// Copyright 2025 D4rk-h
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI quantumComputingOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development server");
        Contact contact = new Contact();
        contact.setUrl("https://github.com/D4rk-h");
        contact.setName("Qubit Flow API Team");
        License apacheLicense = new License()
                .name("Apache License 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");
        Info info = new Info()
                .title("Qubit Flow REST API")
                .version("1.0.0")
                .contact(contact)
                .description("REST API for quantum circuit simulation and manipulation. " +
                        "This API allows you to create quantum circuits, add quantum gates, " +
                        "perform measurements, and simulate quantum computations.")
                .license(apacheLicense);
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}