/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.util.SchemaTypeUtil;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModelUtils {
    static Logger LOGGER = LoggerFactory.getLogger(ModelUtils.class);

    /**
     * Searches for the model by name in the map of models and returns it
     *
     * @param name   Name of the model
     * @param models Map of models
     * @return model
     */
    public static CodegenModel getModelByName(final String name, final Map<String, Object> models) {
        final Object data = models.get(name);
        if (data instanceof Map) {
            final Map<?, ?> dataMap = (Map<?, ?>) data;
            final Object dataModels = dataMap.get("models");
            if (dataModels instanceof List) {
                final List<?> dataModelsList = (List<?>) dataModels;
                for (final Object entry : dataModelsList) {
                    if (entry instanceof Map) {
                        final Map<?, ?> entryMap = (Map<?, ?>) entry;
                        final Object model = entryMap.get("model");
                        if (model instanceof CodegenModel) {
                            return (CodegenModel) model;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static List<String> getUnusedSchemas(OpenAPI openAPI) {
        List<String> unusedSchemas = new ArrayList<String>();

        // no model defined
        if (openAPI.getComponents().getSchemas() == null) {
            openAPI.getComponents().setSchemas(new HashMap<String, Schema>());
        }

        // operations
        Map<String, PathItem> paths = openAPI.getPaths();
        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();

        if (paths != null) {
            for (String pathname : paths.keySet()) {
                PathItem path = paths.get(pathname);
                Map<PathItem.HttpMethod, Operation> operationMap = path.readOperationsMap();
                if (operationMap != null) {
                    for (PathItem.HttpMethod method : operationMap.keySet()) {
                        Operation operation = operationMap.get(method);
                        RequestBody requestBody = operation.getRequestBody();

                        if (requestBody == null) {
                            continue;
                        }

                        //LOGGER.info("debugging resolver: " + requestBody.toString());
                        if (requestBody.getContent() == null) {
                            continue;
                        }

                        // go through "content"
                        for (String mimeType : requestBody.getContent().keySet()) {
                            if ("application/x-www-form-urlencoded".equalsIgnoreCase(mimeType) ||
                                    "multipart/form-data".equalsIgnoreCase(mimeType)) {
                                // remove the schema that's automatically created by the parser
                                MediaType mediaType = requestBody.getContent().get(mimeType);
                                if (mediaType.getSchema().get$ref() != null) {
                                    LOGGER.debug("mark schema (form parameters) as unused: " + getSimpleRef(mediaType.getSchema().get$ref()));
                                    unusedSchemas.add(getSimpleRef(mediaType.getSchema().get$ref()));
                                }
                            }
                        }
                    }
                }
            }
        }

        return unusedSchemas;
    }

    public static String getSimpleRef(String ref) {
        if (ref.startsWith("#/components/")) {
            ref = ref.substring(ref.lastIndexOf("/") + 1);
        } else if (ref.startsWith("#/definitions/")) {
            ref = ref.substring(ref.lastIndexOf("/") + 1);
        } else  {
            LOGGER.warn("Failed to get the schema name: {}", ref);
            return null;
        }

        return ref;
    }

    public static boolean isObjectSchema(Schema schema) {
        if (schema instanceof ObjectSchema) {
            return true;
        }
        if (SchemaTypeUtil.OBJECT_TYPE.equals(schema.getType()) && !(schema instanceof MapSchema)) {
            return true;
        }
        if (schema.getType() == null && schema.getProperties() != null && !schema.getProperties().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isComposedSchema(Schema schema) {
        if (schema instanceof ComposedSchema) {
            return true;
        }
        return false;
    }

    public static boolean isMapSchema(Schema schema) {
        if (schema instanceof MapSchema) {
            return true;
        }
        if (schema.getAdditionalProperties() != null) {
            return true;
        }
        return false;
    }

    public static boolean isArraySchema(Schema schema) {
        if (schema instanceof ArraySchema) {
            return true;
        }
        // assume it's an array if maxItems, minItems is set
        if (schema.getMaxItems() != null || schema.getMinItems() != null) {
            return true;
        }
        return false;
    }

    public static boolean isStringSchema(Schema schema) {
        if (schema instanceof StringSchema || SchemaTypeUtil.STRING_TYPE.equals(schema.getType())) {
            return true;
        }
        return false;
    }

    public static boolean isIntegerSchema(Schema schema) {
        if (schema instanceof IntegerSchema) {
            return true;
        }
        if (SchemaTypeUtil.INTEGER_TYPE.equals(schema.getType())) {
            return true;
        }
        return false;
    }

    public static boolean isShortchema(Schema schema) {
        if (SchemaTypeUtil.INTEGER_TYPE.equals(schema.getType()) // type: integer
                && SchemaTypeUtil.INTEGER32_FORMAT.equals(schema.getFormat())) { // format: short (int32)
            return true;
        }
        return false;
    }

    public static boolean isLongSchema(Schema schema) {
        if (SchemaTypeUtil.INTEGER_TYPE.equals(schema.getType()) // type: integer
                && SchemaTypeUtil.INTEGER64_FORMAT.equals(schema.getFormat())) { // format: long (int64)
            return true;
        }
        return false;
    }

    public static boolean isBooleanSchema(Schema schema) {
        if (schema instanceof BooleanSchema) {
            return true;
        }
        if (SchemaTypeUtil.BOOLEAN_TYPE.equals(schema.getType())) {
            return true;
        }
        return false;
    }

    public static boolean isNumberSchema(Schema schema) {
        if (schema instanceof NumberSchema) {
            return true;
        }
        if (SchemaTypeUtil.NUMBER_TYPE.equals(schema.getType())) {
            return true;
        }
        return false;
    }

    public static boolean isFloatSchema(Schema schema) {
        if (SchemaTypeUtil.NUMBER_TYPE.equals(schema.getType())
                && SchemaTypeUtil.FLOAT_FORMAT.equals(schema.getFormat())) { // format: float
            return true;
        }
        return false;
    }

    public static boolean isDoubleSchema(Schema schema) {
        if (schema instanceof NumberSchema) {
            return true;
        }
        if (SchemaTypeUtil.NUMBER_TYPE.equals(schema.getType())
                && SchemaTypeUtil.DOUBLE_FORMAT.equals(schema.getFormat())) { // format: double
            return true;
        }
        return false;
    }

    public static boolean isDateSchema(Schema schema) {
        if (schema instanceof DateSchema) {
            return true;
        }

        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.DATE_FORMAT.equals(schema.getFormat())) { // format: date
            return true;
        }
        return false;
    }

    public static boolean isDateTimeSchema(Schema schema) {
        if (schema instanceof DateTimeSchema) {
            return true;
        }
        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.DATE_TIME_FORMAT.equals(schema.getFormat())) { // format: date-time
            return true;
        }
        return false;
    }

    public static boolean isPasswordSchema(Schema schema) {
        if (schema instanceof PasswordSchema) {
            return true;
        }
        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.PASSWORD_FORMAT.equals(schema.getFormat())) { // double
            return true;
        }
        return false;
    }

    public static boolean isByteArraySchema(Schema schema) {
        if (schema instanceof ByteArraySchema) {
            return true;
        }
        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.BYTE_FORMAT.equals(schema.getFormat())) { // format: byte
            return true;
        }
        return false;
    }

    public static boolean isBinarySchema(Schema schema) {
        if (schema instanceof BinarySchema) {
            return true;
        }
        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.BINARY_FORMAT.equals(schema.getFormat())) { // format: binary
            return true;
        }
        return false;
    }

    public static boolean isFileSchema(Schema schema) {
        if (schema instanceof FileSchema) {
            return true;
        }
        // file type in oas2 mapped to binary in oas3
        return isBinarySchema(schema);
    }

    public static boolean isUUIDSchema(Schema schema) {
        if (schema instanceof UUIDSchema) {
            return true;
        }
        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.UUID_FORMAT.equals(schema.getFormat())) { // format: uuid
            return true;
        }
        return false;
    }

    public static boolean isEmailSchema(Schema schema) {
        if (schema instanceof EmailSchema) {
            return true;
        }
        if (SchemaTypeUtil.STRING_TYPE.equals(schema.getType())
                && SchemaTypeUtil.EMAIL_FORMAT.equals(schema.getFormat())) { // format: email
            return true;
        }
        return false;
    }

    /**
     * If a Schema contains a reference to an other Schema with '$ref', returns the referenced Schema or the actual Schema in the other cases.
     * @param openAPI
     * @param schema potentially containing a '$ref'
     * @return schema without '$ref'
     */
    public static Schema getReferencedSchema(OpenAPI openAPI, Schema schema) {
        if (schema != null && StringUtils.isNotEmpty(schema.get$ref())) {
            String name = getSimpleRef(schema.get$ref());
            return getSchema(openAPI, name);
        }
        return schema;
    }
    
    public static Schema getSchema(OpenAPI openAPI, String name) {
        if (name == null) {
            return null;
        }

        return getSchemas(openAPI).get(name);
    }

    public static Map<String, Schema> getSchemas(OpenAPI openAPI) {
        if (openAPI != null && openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            return openAPI.getComponents().getSchemas();
        }
        return Collections.emptyMap();
    }

    /**
     * If a RequestBody contains a reference to an other RequestBody with '$ref', returns the referenced RequestBody or the actual RequestBody in the other cases.
     * @param openAPI
     * @param requestBody potentially containing a '$ref'
     * @return requestBody without '$ref'
     */
    public static RequestBody getReferencedRequestBody(OpenAPI openAPI, RequestBody requestBody) {
        if (requestBody != null && StringUtils.isNotEmpty(requestBody.get$ref())) {
            String name = getSimpleRef(requestBody.get$ref());
            return getRequestBody(openAPI, name);
        }
        return requestBody;
    }

    public static RequestBody getRequestBody(OpenAPI openAPI, String name) {
        if (name == null) {
            return null;
        }

        if (openAPI != null && openAPI.getComponents() != null && openAPI.getComponents().getRequestBodies() != null) {
            return openAPI.getComponents().getRequestBodies().get(name);
        }
        return null;
    }

    /**
     * If a ApiResponse contains a reference to an other ApiResponse with '$ref', returns the referenced ApiResponse or the actual ApiResponse in the other cases.
     * @param openAPI
     * @param apiResponse potentially containing a '$ref'
     * @return apiResponse without '$ref'
     */
    public static ApiResponse getReferencedApiResponse(OpenAPI openAPI, ApiResponse apiResponse) {
        if (apiResponse != null && StringUtils.isNotEmpty(apiResponse.get$ref())) {
            String name = getSimpleRef(apiResponse.get$ref());
            return getApiResponse(openAPI, name);
        }
        return apiResponse;
    }

    public static ApiResponse getApiResponse(OpenAPI openAPI, String name) {
        if (name == null) {
            return null;
        }

        if (openAPI != null && openAPI.getComponents() != null && openAPI.getComponents().getRequestBodies() != null) {
            return openAPI.getComponents().getResponses().get(name);
        }
        return null;
    }
    

    public static Schema getSchemaFromRequestBody(RequestBody requestBody) {
        return getSchemaFromContent(requestBody.getContent());
    }

    public static Schema getSchemaFromResponse(ApiResponse response) {
        return getSchemaFromContent(response.getContent());
    }

    private static Schema getSchemaFromContent(Content content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        MediaType mediaType = content.values().iterator().next();
        return mediaType.getSchema();
    }
}
