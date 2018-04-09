package controllers;

import java.math.BigDecimal;
import apimodels.Client;
import apimodels.ERRORUNKNOWN;
import apimodels.OuterComposite;
import apimodels.User;

import play.mvc.Http;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.validation.constraints.*;

@SuppressWarnings("RedundantThrows")
public interface FakeApiControllerImpInterface {
    Boolean fakeOuterBooleanSerialize( UNKNOWN_PARAM_NAME) throws Exception;

    OuterComposite fakeOuterCompositeSerialize(OuterComposite outerComposite) throws Exception;

    BigDecimal fakeOuterNumberSerialize( UNKNOWN_PARAM_NAME) throws Exception;

    String fakeOuterStringSerialize( UNKNOWN_PARAM_NAME) throws Exception;

    void testBodyWithQueryParams( @NotNull String query, User user) throws Exception;

    Client testClientModel(Client client) throws Exception;

    void testEndpointParameters(ERRORUNKNOWN ERRORUNKNOWN) throws Exception;

    void testEnumParameters(List<String> enumHeaderStringArray, String enumHeaderString, List<String> enumQueryStringArray, String enumQueryString, Integer enumQueryInteger, Double enumQueryDouble, ERRORUNKNOWN ERRORUNKNOWN) throws Exception;

    void testInlineAdditionalProperties( UNKNOWN_PARAM_NAME) throws Exception;

    void testJsonFormData(ERRORUNKNOWN ERRORUNKNOWN) throws Exception;

}
