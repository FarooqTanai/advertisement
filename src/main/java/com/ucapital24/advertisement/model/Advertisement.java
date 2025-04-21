package com.ucapital24.advertisement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ucapital24.advertisement.Exception.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

public record Advertisement(@JsonProperty(value = "_id", required = false)
                            String id,

                            @JsonProperty(value = "title", required = true)
                            @NotNull(message = "title is required")
                            String title,

                            @JsonProperty(value = "content", required = true)
                            @NotNull(message = "content is required")
                            String content,

                            @JsonProperty(value = "media_url", required = true)
                            @NotNull(message = "media_url is required")
                            String mediaUrl,

                            @JsonProperty(value = "media_type", required = true)
                            @NotNull(message = "media_type is required")
                            AdvertisementMediaType mediaType,

                            @JsonProperty(value = "start_date", required = true)
                            @NotNull(message = "start_date is required")
                            Instant startDate,

                            @JsonProperty(value = "end_date", required = true)
                            @NotNull(message = "end_date is required")
                            Instant endDate) {
    public Advertisement {
        validate(title, content, mediaUrl, startDate, endDate);
    }

    private void validate(@NotNull String title,
                          @NotNull String content,
                          @NotNull String mediaUrl,
                          @NotNull Instant startDate,
                          @NotNull Instant endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("start_date should be before end_date");
        }
        if (endDate.isBefore(Instant.now())) {
            throw new ValidationException("end_date cannot be in the past");
        }
        if (title.isEmpty() || title.length() < 2) {
            throw new ValidationException("Title length too short");
        }
        if (content.isEmpty() || content.length() < 2) {
            throw new ValidationException("Content length too short");
        }
        try {
            URL url = new URL(mediaUrl);
            String protocol = url.getProtocol();
            if (!(protocol.equals("http") || protocol.equals("https"))) {
                throw new ValidationException("URL must use http or https");
            }
        } catch (MalformedURLException e) {
            String errorMessage = String.format("URL is not valid: %s", e.getMessage());
            throw new ValidationException(errorMessage);
        }
    }
}
