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
import java.io.FileInputStream;
import javax.validation.constraints.*;

public class FakeApiControllerImp implements FakeApiControllerImpInterface {
    @Override
    public Boolean fakeOuterBooleanSerialize( UNKNOWN_PARAM_NAME) throws Exception {
        //Do your magic!!!
        return new Boolean(true);
    }

    @Override
    public OuterComposite fakeOuterCompositeSerialize(OuterComposite outerComposite) throws Exception {
        //Do your magic!!!
        return new OuterComposite();
    }

    @Override
    public BigDecimal fakeOuterNumberSerialize( UNKNOWN_PARAM_NAME) throws Exception {
        //Do your magic!!!
        return new BigDecimal(1.0);
    }

    @Override
    public String fakeOuterStringSerialize( UNKNOWN_PARAM_NAME) throws Exception {
        //Do your magic!!!
        return new String();
    }

    @Override
    public void testBodyWithQueryParams( @NotNull String query, User user) throws Exception {
        //Do your magic!!!
    }

    @Override
    public Client testClientModel(Client client) throws Exception {
        //Do your magic!!!
        return new Client();
    }

    @Override
    public void testEndpointParameters(ERRORUNKNOWN ERRORUNKNOWN) throws Exception {
        //Do your magic!!!
    }

    @Override
    public void testEnumParameters(List<String> enumHeaderStringArray, String enumHeaderString, List<String> enumQueryStringArray, String enumQueryString, Integer enumQueryInteger, Double enumQueryDouble, ERRORUNKNOWN ERRORUNKNOWN) throws Exception {
        //Do your magic!!!
    }

    @Override
    public void testInlineAdditionalProperties( UNKNOWN_PARAM_NAME) throws Exception {
        //Do your magic!!!
    }

    @Override
    public void testJsonFormData(ERRORUNKNOWN ERRORUNKNOWN) throws Exception {
        //Do your magic!!!
    }

}
