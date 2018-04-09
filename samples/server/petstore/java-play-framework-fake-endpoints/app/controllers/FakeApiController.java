package controllers;

import java.math.BigDecimal;
import apimodels.Client;
import apimodels.ERRORUNKNOWN;
import apimodels.OuterComposite;
import apimodels.User;

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


public class FakeApiController extends Controller {

    private final FakeApiControllerImpInterface imp;
    private final ObjectMapper mapper;
    private final Configuration configuration;

    @Inject
    private FakeApiController(Configuration configuration, FakeApiControllerImpInterface imp) {
        this.imp = imp;
        mapper = new ObjectMapper();
        this.configuration = configuration;
    }


    @ApiAction
    public Result fakeOuterBooleanSerialize() throws Exception {
        JsonNode nodeUNKNOWN_PARAM_NAME = request().body().asJson();
         UNKNOWN_PARAM_NAME;
        if (nodeUNKNOWN_PARAM_NAME != null) {
            UNKNOWN_PARAM_NAME = mapper.readValue(nodeUNKNOWN_PARAM_NAME.toString(), .class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(UNKNOWN_PARAM_NAME);
            }
        } else {
            UNKNOWN_PARAM_NAME = null;
        }
        Boolean obj = imp.fakeOuterBooleanSerialize(UNKNOWN_PARAM_NAME);
        JsonNode result = mapper.valueToTree(obj);
        return ok(result);
    }

    @ApiAction
    public Result fakeOuterCompositeSerialize() throws Exception {
        JsonNode nodeouterComposite = request().body().asJson();
        OuterComposite outerComposite;
        if (nodeouterComposite != null) {
            outerComposite = mapper.readValue(nodeouterComposite.toString(), OuterComposite.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(outerComposite);
            }
        } else {
            outerComposite = null;
        }
        OuterComposite obj = imp.fakeOuterCompositeSerialize(outerComposite);
        if (configuration.getBoolean("useOutputBeanValidation")) {
            SwaggerUtils.validate(obj);
        }
        JsonNode result = mapper.valueToTree(obj);
        return ok(result);
    }

    @ApiAction
    public Result fakeOuterNumberSerialize() throws Exception {
        JsonNode nodeUNKNOWN_PARAM_NAME = request().body().asJson();
         UNKNOWN_PARAM_NAME;
        if (nodeUNKNOWN_PARAM_NAME != null) {
            UNKNOWN_PARAM_NAME = mapper.readValue(nodeUNKNOWN_PARAM_NAME.toString(), .class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(UNKNOWN_PARAM_NAME);
            }
        } else {
            UNKNOWN_PARAM_NAME = null;
        }
        BigDecimal obj = imp.fakeOuterNumberSerialize(UNKNOWN_PARAM_NAME);
        if (configuration.getBoolean("useOutputBeanValidation")) {
            SwaggerUtils.validate(obj);
        }
        JsonNode result = mapper.valueToTree(obj);
        return ok(result);
    }

    @ApiAction
    public Result fakeOuterStringSerialize() throws Exception {
        JsonNode nodeUNKNOWN_PARAM_NAME = request().body().asJson();
         UNKNOWN_PARAM_NAME;
        if (nodeUNKNOWN_PARAM_NAME != null) {
            UNKNOWN_PARAM_NAME = mapper.readValue(nodeUNKNOWN_PARAM_NAME.toString(), .class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(UNKNOWN_PARAM_NAME);
            }
        } else {
            UNKNOWN_PARAM_NAME = null;
        }
        String obj = imp.fakeOuterStringSerialize(UNKNOWN_PARAM_NAME);
        JsonNode result = mapper.valueToTree(obj);
        return ok(result);
    }

    @ApiAction
    public Result testBodyWithQueryParams() throws Exception {
        JsonNode nodeuser = request().body().asJson();
        User user;
        if (nodeuser != null) {
            user = mapper.readValue(nodeuser.toString(), User.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(user);
            }
        } else {
            throw new IllegalArgumentException("'User' parameter is required");
        }
        String valuequery = request().getQueryString("query");
        String query;
        if (valuequery != null) {
            query = valuequery;
        } else {
            throw new IllegalArgumentException("'query' parameter is required");
        }
        imp.testBodyWithQueryParams(query, user);
        return ok();
    }

    @ApiAction
    public Result testClientModel() throws Exception {
        JsonNode nodeclient = request().body().asJson();
        Client client;
        if (nodeclient != null) {
            client = mapper.readValue(nodeclient.toString(), Client.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(client);
            }
        } else {
            throw new IllegalArgumentException("'Client' parameter is required");
        }
        Client obj = imp.testClientModel(client);
        if (configuration.getBoolean("useOutputBeanValidation")) {
            SwaggerUtils.validate(obj);
        }
        JsonNode result = mapper.valueToTree(obj);
        return ok(result);
    }

    @ApiAction
    public Result testEndpointParameters() throws Exception {
        JsonNode nodeERRORUNKNOWN = request().body().asJson();
        ERRORUNKNOWN ERRORUNKNOWN;
        if (nodeERRORUNKNOWN != null) {
            ERRORUNKNOWN = mapper.readValue(nodeERRORUNKNOWN.toString(), ERRORUNKNOWN.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(ERRORUNKNOWN);
            }
        } else {
            throw new IllegalArgumentException("'ERRORUNKNOWN' parameter is required");
        }
        imp.testEndpointParameters(ERRORUNKNOWN);
        return ok();
    }

    @ApiAction
    public Result testEnumParameters() throws Exception {
        JsonNode nodeERRORUNKNOWN = request().body().asJson();
        ERRORUNKNOWN ERRORUNKNOWN;
        if (nodeERRORUNKNOWN != null) {
            ERRORUNKNOWN = mapper.readValue(nodeERRORUNKNOWN.toString(), ERRORUNKNOWN.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(ERRORUNKNOWN);
            }
        } else {
            ERRORUNKNOWN = null;
        }
        String valueenumQueryStringArray = request().getQueryString("enum_query_string_array");
        List<String> enumQueryStringArray;
        if (valueenumQueryStringArray != null) {
            enumQueryStringArray = valueenumQueryStringArray;
        } else {
            enumQueryStringArray = null;
        }
        String valueenumQueryString = request().getQueryString("enum_query_string");
        String enumQueryString;
        if (valueenumQueryString != null) {
            enumQueryString = valueenumQueryString;
        } else {
            enumQueryString = null;
        }
        String valueenumQueryInteger = request().getQueryString("enum_query_integer");
        Integer enumQueryInteger;
        if (valueenumQueryInteger != null) {
            enumQueryInteger = Integer.parseInt(valueenumQueryInteger);
        } else {
            enumQueryInteger = null;
        }
        String valueenumQueryDouble = request().getQueryString("enum_query_double");
        Double enumQueryDouble;
        if (valueenumQueryDouble != null) {
            enumQueryDouble = Double.parseDouble(valueenumQueryDouble);
        } else {
            enumQueryDouble = null;
        }
        String valueenumHeaderStringArray = request().getHeader("enum_header_string_array");
        List<String> enumHeaderStringArray;
        if (valueenumHeaderStringArray != null) {
            enumHeaderStringArray = valueenumHeaderStringArray;
        } else {
            enumHeaderStringArray = null;
        }
        String valueenumHeaderString = request().getHeader("enum_header_string");
        String enumHeaderString;
        if (valueenumHeaderString != null) {
            enumHeaderString = valueenumHeaderString;
        } else {
            enumHeaderString = null;
        }
        imp.testEnumParameters(enumHeaderStringArray, enumHeaderString, enumQueryStringArray, enumQueryString, enumQueryInteger, enumQueryDouble, ERRORUNKNOWN);
        return ok();
    }

    @ApiAction
    public Result testInlineAdditionalProperties() throws Exception {
        JsonNode nodeUNKNOWN_PARAM_NAME = request().body().asJson();
         UNKNOWN_PARAM_NAME;
        if (nodeUNKNOWN_PARAM_NAME != null) {
            UNKNOWN_PARAM_NAME = mapper.readValue(nodeUNKNOWN_PARAM_NAME.toString(), .class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(UNKNOWN_PARAM_NAME);
            }
        } else {
            throw new IllegalArgumentException("'UNKNOWN_BASE_NAME' parameter is required");
        }
        imp.testInlineAdditionalProperties(UNKNOWN_PARAM_NAME);
        return ok();
    }

    @ApiAction
    public Result testJsonFormData() throws Exception {
        JsonNode nodeERRORUNKNOWN = request().body().asJson();
        ERRORUNKNOWN ERRORUNKNOWN;
        if (nodeERRORUNKNOWN != null) {
            ERRORUNKNOWN = mapper.readValue(nodeERRORUNKNOWN.toString(), ERRORUNKNOWN.class);
            if (configuration.getBoolean("useInputBeanValidation")) {
                SwaggerUtils.validate(ERRORUNKNOWN);
            }
        } else {
            throw new IllegalArgumentException("'ERRORUNKNOWN' parameter is required");
        }
        imp.testJsonFormData(ERRORUNKNOWN);
        return ok();
    }
}
