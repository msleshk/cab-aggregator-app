package cab.app.ratingservice.client.decoder;

import cab.app.ratingservice.exception.BadRequestException;
import cab.app.ratingservice.exception.EntityNotFoundException;
import cab.app.ratingservice.exception.ExternalServerErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class CustomFeignClientDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        String errorMessage = extractErrorMessage(response);
        return switch (response.status()) {
            case 400 -> new BadRequestException(errorMessage);
            case 404 -> new EntityNotFoundException(errorMessage);
            case 500 -> new ExternalServerErrorException(errorMessage);
            default -> defaultDecoder.decode(s, response);
        };
    }

    private String extractErrorMessage(Response response) {
        if (response.body() == null) {
            return "No error details";
        }
        try (InputStream inputStream = response.body().asInputStream()) {
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            return jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error";
        } catch (IOException e) {
            return "Error during reading response body";
        }
    }
}
