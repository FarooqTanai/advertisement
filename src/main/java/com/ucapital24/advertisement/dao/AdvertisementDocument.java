package com.ucapital24.advertisement.dao;

import com.ucapital24.advertisement.model.Advertisement;
import com.ucapital24.advertisement.model.AdvertisementMediaType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "advertisement")
@Data
@AllArgsConstructor
public class AdvertisementDocument {

    @Id
    private String id;

    @NotNull
    @Field("title")
    private String title;

    @NotNull
    @Field("content")
    private String content;

    @NotNull
    @Field("media_url")
    private String mediaUrl;

    @NotNull
    @Field("media_type") // "image" or "video"
    private AdvertisementMediaType mediaType;

    @NotNull
    @Field("start_date")
    private Instant startDate;

    @NotNull
    @Field("end_date")
    private Instant endDate;

    @NotNull
    public Advertisement toAdvertisement() {
        return new Advertisement(id, title, content, mediaUrl, mediaType, startDate, endDate);
    }
}
