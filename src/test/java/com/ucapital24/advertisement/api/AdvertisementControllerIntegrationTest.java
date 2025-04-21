package com.ucapital24.advertisement.api;

import com.ucapital24.advertisement.dao.AdvertisementDocument;
import com.ucapital24.advertisement.dao.AdvertisementRepository;
import com.ucapital24.advertisement.model.AdvertisementMediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdvertisementControllerIntegrationTest {

    private final String API_PATH = "/api/v1/advertisements";
    private final String docId1 = "doc1";
    private final String docId2 = "doc2";
    private Instant startDate;
    private Instant endDate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @BeforeEach
    public void setUp() {
        startDate = Instant.now();
        endDate = Instant.now().plus(1, ChronoUnit.DAYS);

        AdvertisementDocument ad = new AdvertisementDocument(
                docId1,
                "first add title",
                "clicking on first add will bring you to the add page",
                "https://example.com/image.png",
                AdvertisementMediaType.IMAGE,
                startDate,
                endDate
        );
        AdvertisementDocument ad2 = new AdvertisementDocument(
                docId2,
                "second add title",
                "clicking on second add will bring you to the add page",
                "https://example.com/image.png",
                AdvertisementMediaType.VIDEO,
                startDate,
                endDate
        );
        advertisementRepository.save(ad);
        advertisementRepository.save(ad2);
    }

    @AfterEach
    public void cleanUp() {
        advertisementRepository.deleteAll();
    }

    @Test
    public void createAdvertisement_OK() throws Exception {

        String requestBody = """
        {
          "title": "My Ad title",
          "content": "Come and visit us by clicking the link!",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-04-30T00:00:00Z"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My Ad title"))
                .andExpect(jsonPath("$.content").value("Come and visit us by clicking the link!"));
    }

    @Test
    public void createAdvertisement_emptyTitle_KO() throws Exception {

        String requestBody = """
        {
          "title" : "",
          "content": "Come and visit us by clicking the link!",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-04-30T00:00:00Z"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.error_message").value("Title length too short"));
    }

    @Test
    public void createAdvertisement_emptyContent_KO() throws Exception {

        String requestBody = """
        {
          "title" : "the title",
          "content": "",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-04-30T00:00:00Z"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.error_message").value("Content length too short"));
    }

    @Test
    public void createAdvertisement_incorrectUrl_KO() throws Exception {

        String requestBody = """
        {
          "title" : "the title",
          "content": "Come and visit us by clicking the link!",
          "media_url": "ht://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-04-30T00:00:00Z"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.error_message").value("URL is not valid: unknown protocol: ht"));
    }

    @Test
    public void createAdvertisement_endDateBeforeStartDate_KO() throws Exception {

        String requestBody = """
        {
          "title" : "the title",
          "content": "Come and visit us by clicking the link!",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-02-27T00:00:00Z"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.error_message").value(containsString("start_date should be before end_date")));
    }

    @Test
    public void createAdvertisement_endDateInThePast_KO() throws Exception {

        String requestBody = """
        {
          "title" : "the title",
          "content": "Come and visit us by clicking the link!",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2025-02-21T00:00:00Z",
          "end_date": "2025-02-27T00:00:00Z"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.error_message").value(containsString("end_date cannot be in the past")));
    }

    @Test
    public void getAllAdvertisements() throws Exception {

        String expectedStartDate = startDate.truncatedTo(ChronoUnit.MILLIS).toString();
        String expectedEndDate = endDate.truncatedTo(ChronoUnit.MILLIS).toString();

        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content.[0].title").value("first add title"))
                .andExpect(jsonPath("$.content.[0].content").value("clicking on first add will bring you to the add page"))
                .andExpect(jsonPath("$.content.[0].media_url").value("https://example.com/image.png"))
                .andExpect(jsonPath("$.content.[0].media_type").value("IMAGE"))
                .andExpect(jsonPath("$.content.[0].start_date").value(expectedStartDate))
                .andExpect(jsonPath("$.content.[0].end_date").value(expectedEndDate))
                .andExpect(jsonPath("$.content.[1].title").value("second add title"))
                .andExpect(jsonPath("$.content.[1].content").value("clicking on second add will bring you to the add page"))
                .andExpect(jsonPath("$.content.[1].media_url").value("https://example.com/image.png"))
                .andExpect(jsonPath("$.content.[1].media_type").value("VIDEO"))
                .andExpect(jsonPath("$.content.[1].start_date").value(expectedStartDate))
                .andExpect(jsonPath("$.content.[1].end_date").value(expectedEndDate))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10));

    }

    @Test
    public void getAdvertisement_byId_OK() throws Exception {

        mockMvc.perform(get(API_PATH + "/" + docId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("first add title"))
                .andExpect(jsonPath("$.content").value("clicking on first add will bring you to the add page"))
                .andExpect(jsonPath("$.media_url").value("https://example.com/image.png"));
    }

    @Test
    public void getAdvertisement_byId_notFound_KO() throws Exception {

        mockMvc.perform(get(API_PATH + "/anyId" ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("ADVERTISEMENT_NOT_FOUND"))
                .andExpect(jsonPath("$.error_message").value("Advertisement with id: anyId not found"));

    }

    @Test
    public void deleteAdvertisement_byId_OK() throws Exception {

        mockMvc.perform(delete(API_PATH + "/" + docId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("deleted!"));
    }

    @Test
    public void deleteAdvertisement_byId_notFound_KO() throws Exception {

        mockMvc.perform(delete(API_PATH + "/anyId" ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("ADVERTISEMENT_NOT_FOUND"))
                .andExpect(jsonPath("$.error_message").value("Advertisement with id: anyId not found"));

    }

    @Test
    public void updateAdvertisement_OK() throws Exception {

        String requestBody = """
        {
          "title": "updated Ad title",
          "content": "update content: Come and visit us by clicking the link!",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-04-30T00:00:00Z"
        }
        """;

        mockMvc.perform(put(API_PATH + "/" + docId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated Ad title"))
                .andExpect(jsonPath("$.content").value("update content: Come and visit us by clicking the link!"));
    }

    @Test
    public void updateAdvertisement_NotFound_KO() throws Exception {

        String requestBody = """
        {
          "title": "updated Ad title",
          "content": "update content: Come and visit us by clicking the link!",
          "media_url": "http://example.com/image.jpg",
          "media_type": "IMAGE",
          "start_date": "2029-04-21T00:00:00Z",
          "end_date": "2029-04-30T00:00:00Z"
        }
        """;
        mockMvc.perform(put(API_PATH + "/anyId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("ADVERTISEMENT_NOT_FOUND"))
                .andExpect(jsonPath("$.error_message").value("Advertisement with id: anyId not found"));
    }
}
