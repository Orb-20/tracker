package com.courier;

import com.courier.dto.CourierRequest;
import com.courier.dto.StatusUpdateRequest;
import com.courier.model.CourierStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void createCourier_shouldReturn201() throws Exception {
        CourierRequest request = new CourierRequest("Alice", "Bob", "Mumbai", "Delhi");
        mockMvc.perform(post("/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trackingNumber").exists())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void createCourier_shouldReturn400_whenFieldsBlank() throws Exception {
        CourierRequest request = new CourierRequest("", "Bob", "Mumbai", "");
        mockMvc.perform(post("/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.senderName").exists());
    }

    @Test
    void getCourier_shouldReturn200() throws Exception {
        CourierRequest request = new CourierRequest("Carol", "Dave", "Pune", "Bangalore");
        MvcResult result = mockMvc.perform(post("/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andReturn();
        String trackingNumber = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("trackingNumber").asText();
        mockMvc.perform(get("/courier/" + trackingNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").value(trackingNumber));
    }

    @Test
    void getCourier_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/courier/TRK-UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStatus_shouldReturn200() throws Exception {
        CourierRequest request = new CourierRequest("Eve", "Frank", "Chennai", "Hyderabad");
        MvcResult result = mockMvc.perform(post("/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andReturn();
        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        StatusUpdateRequest update = new StatusUpdateRequest(CourierStatus.IN_TRANSIT);
        mockMvc.perform(put("/courier/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_TRANSIT"));
    }

    @Test
    void updateStatus_shouldReturn404_whenNotFound() throws Exception {
        StatusUpdateRequest update = new StatusUpdateRequest(CourierStatus.DELIVERED);
        mockMvc.perform(put("/courier/9999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(update)))
                .andExpect(status().isNotFound());
    }
}
