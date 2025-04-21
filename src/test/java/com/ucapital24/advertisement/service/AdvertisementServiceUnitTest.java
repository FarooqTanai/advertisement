package com.ucapital24.advertisement.service;

import com.ucapital24.advertisement.Exception.AdvertisementNotFoundException;
import com.ucapital24.advertisement.Exception.GenericException;
import com.ucapital24.advertisement.dao.AdvertisementDocument;
import com.ucapital24.advertisement.dao.AdvertisementRepository;
import com.ucapital24.advertisement.model.Advertisement;
import com.ucapital24.advertisement.model.AdvertisementMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdvertisementServiceUnitTest {

    private Advertisement advertisement;
    private AdvertisementDocument document;
    private String docId;

    @Mock
    private AdvertisementRepository advertisementRepository;

    @InjectMocks
    private AdvertisementService advertisementService;

    @BeforeEach
    public void setUp() {
        advertisement = new Advertisement(
                docId,
                "Title of the Advertisement",
                "The content goes here",
                "https://image.com/img.jpg",
                AdvertisementMediaType.IMAGE,
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        document = new AdvertisementDocument(
                docId,
                advertisement.title(),
                advertisement.content(),
                advertisement.mediaUrl(),
                advertisement.mediaType(),
                advertisement.startDate(),
                advertisement.endDate()
        );
    }

    @Test
    void getAdvertisements_OK() {
        Pageable pageable = PageRequest.of(0, 10);
        when(advertisementRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(document)));

        Page<Advertisement> result = advertisementService.getAdvertisements(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(advertisement.title(), result.getContent().get(0).title());
    }

    @Test
    void getAdvertisement_OK() {
        when(advertisementRepository.findById(docId)).thenReturn(Optional.of(document));

        Advertisement result = advertisementService.getAdvertisement(docId);

        assertEquals(advertisement.title(), result.title());
    }

    @Test
    void getAdvertisement_notFound_KO() {
        when(advertisementRepository.findById("id-404")).thenReturn(Optional.empty());

        assertThrows(AdvertisementNotFoundException.class, () ->
                advertisementService.getAdvertisement("id-404"));
    }

    @Test
    void createAdvertisement_OK() {
        when(advertisementRepository.save(any())).thenReturn(document);

        Advertisement result = advertisementService.createAdvertisement(advertisement);

        assertEquals(advertisement.title(), result.title());
        verify(advertisementRepository).save(any());
    }

    @Test
    void createAdvertisement_genericException_KO() {
        when(advertisementRepository.save(any())).thenThrow(new RuntimeException("MongoDb generic error"));

        assertThrows(GenericException.class, () ->
                advertisementService.createAdvertisement(advertisement));
    }

    @Test
    void deleteAdvertisement_OK() {
        when(advertisementRepository.findById(docId)).thenReturn(Optional.of(document));

        advertisementService.deleteAdvertisement(docId);

        verify(advertisementRepository).delete(document);
    }

    @Test
    void deleteAdvertisement_notFound_KO() {
        when(advertisementRepository.findById("anyId")).thenReturn(Optional.empty());

        assertThrows(AdvertisementNotFoundException.class, () ->
                advertisementService.deleteAdvertisement("anyId"));
    }
    @Test
    void updateAdvertisement_OK() {
        when(advertisementRepository.findById(docId)).thenReturn(Optional.of(document));
        when(advertisementRepository.save(any())).thenReturn(document);

        Advertisement updated = advertisementService.updateAdvertisement(docId, advertisement);

        assertEquals(advertisement.title(), updated.title());
        verify(advertisementRepository).save(any());
    }

    @Test
    void updateAdvertisement_notFound_KO() {
        when(advertisementRepository.findById("anyId")).thenReturn(Optional.empty());

        assertThrows(AdvertisementNotFoundException.class, () ->
                advertisementService.updateAdvertisement("anyId", advertisement));
    }
}
