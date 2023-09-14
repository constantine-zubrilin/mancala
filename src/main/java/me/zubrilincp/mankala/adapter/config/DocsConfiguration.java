package me.zubrilincp.mankala.adapter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class DocsConfiguration {
  @Bean
  public OpenAPI customOpenAPI() {
    Schema errorResponseSchema =
        new Schema<Map<String, Object>>()
            .addProperty("timestamp", new DateTimeSchema().example("2020-12-26T19:39:11.426+00:00"))
            .addProperty("status", new IntegerSchema().example(404))
            .addProperty("error", new StringSchema().example("Not Found"))
            .addProperty("message", new StringSchema().example("Party not found"))
            .addProperty(
                "path",
                new StringSchema().example("/api/v1/party/a158f9cc-fcd7-4364-ac7e-075862ba9841"));

    return new OpenAPI()
        .components(new Components().addSchemas("ErrorResponseSchema", errorResponseSchema));
  }
}
