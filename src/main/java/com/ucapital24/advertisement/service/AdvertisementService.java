package com.ucapital24.advertisement.service;

import com.ucapital24.advertisement.Exception.AdvertisementNotFoundException;
import com.ucapital24.advertisement.Exception.GenericException;
import com.ucapital24.advertisement.dao.AdvertisementDocument;
import com.ucapital24.advertisement.dao.AdvertisementRepository;
import com.ucapital24.advertisement.model.Advertisement;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;


    /**
     * Retrieves a paginated list of advertisements.
     *
     * @param pageable pagination parameters (page number, size, sorting)
     * @return a page of {@link Advertisement} objects
     */
    @NotNull
    public Page<Advertisement> getAdvertisements(Pageable pageable) {
        return advertisementRepository.findAll(pageable).map(AdvertisementDocument::toAdvertisement);
    }

    /**
     * Retrieves an advertisement by its ID.
     *
     * @param adId the ID of the advertisement
     * @return the {@link Advertisement} object
     * @throws AdvertisementNotFoundException if the advertisement is not found
     */
    @NotNull
    public Advertisement getAdvertisement(@NotNull String adId) {
        return findAdvertisementById(adId).toAdvertisement();
    }

    /**
     * Creates a new advertisement.
     *
     * @param advertisement the advertisement to create
     * @return the created {@link Advertisement}
     * @throws GenericException if the save operation fails
     */
    public Advertisement createAdvertisement(@NotNull Advertisement advertisement) {
        try {
            String id = UUID.randomUUID().toString();
            AdvertisementDocument advertisementDocument = new AdvertisementDocument(
                    id,
                    advertisement.title(),
                    advertisement.content(),
                    advertisement.mediaUrl(),
                    advertisement.mediaType(),
                    advertisement.startDate(),
                    advertisement.endDate()
            );
            return advertisementRepository.save(advertisementDocument).toAdvertisement();
        } catch (Exception e) {
            log.error("Error saving advertisement: {}" , e);
            throw new GenericException(e.getMessage());
        }
    }

    /**
     * Deletes an advertisement by ID.
     *
     * @param adId the ID of the advertisement to delete
     * @throws AdvertisementNotFoundException if the advertisement does not exist
     * @throws GenericException for any other errors during deletion
     */
    public void deleteAdvertisement(@NotNull String adId) {

        try {
            AdvertisementDocument advertisementDocument = findAdvertisementById(adId);
            advertisementRepository.delete(advertisementDocument);
        } catch (AdvertisementNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting advertisement with Id: {}, error: {}" , adId, e);
            throw new GenericException(e.getMessage());
        }
    }

    @NotNull
    private AdvertisementDocument findAdvertisementById(@NotNull String adId) {
        return advertisementRepository.findById(adId).orElseThrow(() ->
                new AdvertisementNotFoundException(String.format("Advertisement with id: %s not found", adId)));
    }

    /**
     * Updates an existing advertisement.
     *
     * @param adId          the ID of the advertisement to update
     * @param advertisement the updated advertisement data
     * @return the updated {@link Advertisement}
     * @throws AdvertisementNotFoundException if the advertisement is not found
     * @throws GenericException if an error occurs while updating
     */
    @NotNull
    public Advertisement updateAdvertisement(@NotNull String adId, @NotNull Advertisement advertisement) {

        try {
            AdvertisementDocument advertisementDocument = findAdvertisementById(adId);
            advertisementDocument.setTitle(advertisement.title());
            advertisementDocument.setContent(advertisement.content());
            advertisementDocument.setMediaUrl(advertisement.mediaUrl());
            advertisementDocument.setMediaType(advertisement.mediaType());
            advertisementDocument.setStartDate(advertisement.startDate());
            advertisementDocument.setEndDate(advertisement.endDate());
            return advertisementRepository.save(advertisementDocument).toAdvertisement();
        } catch (AdvertisementNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating advertisement: {}" , e);
            throw new GenericException(e.getMessage());
        }
    }
}
