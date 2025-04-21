package com.ucapital24.advertisement.api;

import com.ucapital24.advertisement.model.Advertisement;
import com.ucapital24.advertisement.service.AdvertisementService;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/advertisements")
public class AdvertisementController {


    @Autowired
    private AdvertisementService advertisementService;

    private final int DEFAULT_PAGE = 0;
    private final int DEFAULT_PAGE_SIZE = 10;


    /**
     * Get an advertisement by its ID.
     *
     * @param adId the ID of the advertisement to retrieve
     * @return the Advertisement object if found
     */
    @GetMapping(value = "/{ad_id}", produces = "application/json")
    public Advertisement getAdd(@PathVariable(name = "ad_id", required = true) @Nonnull String adId) {
        return advertisementService.getAdvertisement(adId);
    }

    /**
     * Get a paginated list of advertisements.
     *
     * @param pageable the pagination and sorting information
     * @return a paginated list of Advertisement objects
     */
    @GetMapping(produces = "application/json")
    public Page<Advertisement> getAdds(@PageableDefault(page = DEFAULT_PAGE, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        return advertisementService.getAdvertisements(pageable);
    }

    /**
     * Create a new advertisement.
     *
     * @param advertisement the Advertisement data to create
     * @return the created Advertisement object in the response
     */
    @PostMapping(produces = "application/json")
    protected ResponseEntity<Advertisement> createAd(@RequestBody @Nonnull Advertisement advertisement) {
        var response = advertisementService.createAdvertisement(advertisement);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Delete an advertisement by ID.
     *
     * @param adId the ID of the advertisement to delete
     * @return confirmation message after deletion
     */
    @DeleteMapping("/{ad_id}")
    public ResponseEntity<String> deleteAd(@PathVariable(name = "ad_id", required = true) @Nonnull String adId) {
        advertisementService.deleteAdvertisement(adId);
        return ResponseEntity.ok().body("deleted!");
    }

    /**
     * Update an existing advertisement by ID.
     *
     * @param adId the ID of the advertisement to update
     * @param advertisement the new Advertisement data
     * @return the updated Advertisement object in the response
     */
    @PutMapping(value = "/{ad_id}", produces = "application/json")
    public ResponseEntity<Advertisement> updateAd(@PathVariable(name = "ad_id", required = true) @Nonnull String adId,
                                           @RequestBody @Nonnull Advertisement advertisement) {
        var response = advertisementService.updateAdvertisement(adId, advertisement);
        return ResponseEntity.ok().body(response);
    }
}
