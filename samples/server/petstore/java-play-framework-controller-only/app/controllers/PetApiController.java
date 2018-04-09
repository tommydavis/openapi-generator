package controllers;

import java.io.InputStream;
import apimodels.ModelApiResponse;
import apimodels.Pet;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import java.io.File;
import swagger.SwaggerUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.validation.constraints.*;
import play.Configuration;

import swagger.SwaggerUtils.ApiAction;


public class PetApiController extends Controller {

    private final ObjectMapper mapper;
    private final Configuration configuration;

    @Inject
    private PetApiController(Configuration configuration) {
        mapper = new ObjectMapper();
        this.configuration = configuration;
    }


    @ApiAction
    public Result addPet() throws Exception {
        JsonNode nodepet = request().body().asJson();
        Pet pet;
        if (nodepet != null) {
            pet = mapper.readValue(nodepet.toString(), Pet.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(pet);
            }
        } else {
            throw new IllegalArgumentException("'Pet' parameter is required");
        }
        return ok();
    }

    @ApiAction
    public Result deletePet(Long petId) throws Exception {
        String valueapiKey = request().getHeader("api_key");
        String apiKey;
        if (valueapiKey != null) {
            apiKey = valueapiKey;
        } else {
            apiKey = null;
        }
        return ok();
    }

    @ApiAction
    public Result findPetsByStatus() throws Exception {
        String valuestatus = request().getQueryString("status");
        List<String> status;
        if (valuestatus != null) {
            status = valuestatus;
        } else {
            throw new IllegalArgumentException("'status' parameter is required");
        }
        return ok();
    }

    @ApiAction
    public Result findPetsByTags() throws Exception {
        String valuetags = request().getQueryString("tags");
        List<String> tags;
        if (valuetags != null) {
            tags = valuetags;
        } else {
            throw new IllegalArgumentException("'tags' parameter is required");
        }
        return ok();
    }

    @ApiAction
    public Result getPetById(Long petId) throws Exception {
        return ok();
    }

    @ApiAction
    public Result updatePet() throws Exception {
        JsonNode nodepet = request().body().asJson();
        Pet pet;
        if (nodepet != null) {
            pet = mapper.readValue(nodepet.toString(), Pet.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(pet);
            }
        } else {
            throw new IllegalArgumentException("'Pet' parameter is required");
        }
        return ok();
    }

    @ApiAction
    public Result updatePetWithForm(Long petId) throws Exception {
        String name = request().body().asMultipartFormData().getFile("name");
        String status = request().body().asMultipartFormData().getFile("status");
        return ok();
    }

    @ApiAction
    public Result uploadFile(Long petId) throws Exception {
        String additionalMetadata = request().body().asMultipartFormData().getFile("additionalMetadata");
        Http.MultipartFormData.FilePart file = request().body().asMultipartFormData().getFile("file");
        return ok();
    }
}
