package com.example.server.service;


import com.example.server.context.CustomContext;
import com.example.server.model.ContextParamDefault;
import com.example.server.model.User;
import com.example.server.model.UsersResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final RestTemplate restTemplate;
    private final ContextRestorationService contextRestorationService;
    private final String BASE_URL = "https://dummyjson.com";

    @Autowired
    public UserService(RestTemplateBuilder restTemplateBuilder, ContextRestorationService contextRestorationService) {
        this.restTemplate = restTemplateBuilder.build();
        this.contextRestorationService = contextRestorationService;
    }

    /**
     * Get all users with pagination
     *
     * @param limit Maximum number of users to return
     * @param skip  Number of users to skip for pagination
     * @return List of users wrapped in a response object
     */
    @Tool(name = "getAllUsers", description = "Get all users")
    public UsersResponse getAllUsers(int limit, int skip) {

        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.get(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users?limit=" + limit + "&skip=" + skip;
        return restTemplate.getForObject(url, UsersResponse.class);
    }

    /**
     * Get all users (default pagination)
     *
     * @return List of users wrapped in a response object
     */
    @Tool(name = "getAllUsersDefault", description = "Get all users with default pagination")
    public UsersResponse getAllUsers() {
        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.get(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users";
        return restTemplate.getForObject(url, UsersResponse.class);
    }

    /**
     * Get a single user by ID
     *
     * @param id The user ID
     * @return User object
     */
    @Tool(name = "getUserById", description = "Get a single user by ID")
    public User getUserById(int id) {
        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.getObject(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users/" + id;
        return restTemplate.getForObject(url, User.class);
    }

    /**
     * Search for users by query
     *
     * @param query The search query
     * @return List of users that match the query
     */
    @Tool(name = "searchUsers", description = "Search for users by query")
    public UsersResponse searchUsers(String query) {
        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.get(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users/search?q=" + query;
        return restTemplate.getForObject(url, UsersResponse.class);
    }

    /**
     * Add a new user
     *
     * @param user The user to add
     * @return The added user with ID
     */
    @Tool(name = "addUser", description = "Add a new user")
    public User addUser(User user) {

        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.get(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users/add";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        return restTemplate.postForObject(url, request, User.class);
    }

    /**
     * Update a user
     *
     * @param id      The ID of the user to update
     * @param updates A map of fields to update
     * @return The updated user
     */
    @Tool(name = "updateUser", description = "Update a user")
    public User updateUser(int id, Map<String, Object> updates) {

        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.get(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(updates, headers);

        return restTemplate.exchange(url, HttpMethod.PUT, request, User.class).getBody();
    }

    /**
     * Delete a user
     *
     * @param id The ID of the user to delete
     * @return The deleted user with isDeleted flag
     */
    @Tool(name = "deleteUser", description = "Delete a user")
    public User deleteUser(int id) {

        // Single line to restore all contexts and get headers
        HttpHeaders httpHeaders = contextRestorationService.restoreAllContextsAndGetHeaders();
        System.out.println("Headers in Tool: " + httpHeaders);
        System.out.println("CustomContext in Tool: "
                + CustomContext.get(ContextParamDefault.X_TRAFFIC_TYPE));

        String url = BASE_URL + "/users/" + id;

        return restTemplate.exchange(url, HttpMethod.DELETE, null, User.class).getBody();
    }


    @Tool(description = "This method provide date and time as per user timezone")
    String getCurrentDateAndTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

}