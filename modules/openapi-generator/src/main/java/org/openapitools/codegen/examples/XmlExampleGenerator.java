package org.openapitools.codegen.examples;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.parser.util.SchemaTypeUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class XmlExampleGenerator {
    protected final Logger LOGGER = LoggerFactory.getLogger(XmlExampleGenerator.class);
    public static String NEWLINE = "\n";
    public static String TAG_START = "<";
    public static String CLOSE_TAG = ">";
    public static String TAG_END = "</";
    private static String EMPTY = "";
    protected Map<String, Schema> examples;

    public XmlExampleGenerator(Map<String, Schema> examples) {
        this.examples = examples;
        if (examples == null) {
            this.examples = new HashMap<String, Schema>(); 
        }
    }

    public String toXml(Schema schema) {
        return toXml(null, schema, 0, Collections.<String>emptySet());
    }

    protected String toXml(Schema schema, int indent, Collection<String> path) {
        if (StringUtils.isNotEmpty(schema.get$ref())) {
            Schema actualSchema = examples.get(schema.get$ref());
            if (actualSchema != null) {
                return modelImplToXml(actualSchema, indent, path);
            }
        }
        return modelImplToXml(schema, indent, path);
    }

    protected String modelImplToXml(Schema schema, int indent, Collection<String> path) {
        final String modelName = schema.getName();
        if (path.contains(modelName)) {
            return EMPTY;
        }
        final Set<String> selfPath = new HashSet<String>(path);
        selfPath.add(modelName);

        StringBuilder sb = new StringBuilder();
        // attributes
        Map<String, Schema> attributes = new LinkedHashMap<String, Schema>();
        Map<String, Schema> elements = new LinkedHashMap<String, Schema>();

        String name = modelName;
        XML xml = schema.getXml();
        if (xml != null) {
            if (xml.getName() != null) {
                name = xml.getName();
            }
        }
        // TODO: map objects will not enter this block
        Map<String, Schema> properties = schema.getProperties();
        if(properties != null && !properties.isEmpty()) {
            for (String pName : properties.keySet()) {
                Schema property = properties.get(pName);
                if (property != null && property.getXml() != null && property.getXml().getAttribute() != null && property.getXml().getAttribute()) {
                    attributes.put(pName, property);
                } else {
                    elements.put(pName, property);
                }
            }
        }

        sb.append(indent(indent)).append(TAG_START);
        sb.append(name);
        for (String pName : attributes.keySet()) {
            Schema s = attributes.get(pName);
            sb.append(" ").append(pName).append("=").append(quote(toXml(null, s, 0, selfPath)));
        }
        sb.append(CLOSE_TAG);
        sb.append(NEWLINE);
        for (String pName : elements.keySet()) {
            Schema s = elements.get(pName);
            final String asXml = toXml(pName, s, indent + 1, selfPath);
            if (StringUtils.isEmpty(asXml)) {
                continue;
            }
            sb.append(asXml);
            sb.append(NEWLINE);
        }
        sb.append(indent(indent)).append(TAG_END).append(name).append(CLOSE_TAG);

        return sb.toString();
    }

    @SuppressWarnings("static-method")
    protected String quote(String string) {
        return "\"" + string + "\"";
    }

    protected String toXml(String name, Schema schema, int indent, Collection<String> path) {
        if (schema == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        if (schema instanceof ArraySchema) {
            ArraySchema as = (ArraySchema) schema;
            Schema inner = as.getItems();
            boolean wrapped = false;
            if (schema.getXml() != null && schema.getXml().getWrapped() != null && schema.getXml().getWrapped()) {
                wrapped = true;
            }
            if (wrapped) {
                String prefix = EMPTY;
                if (name != null) {
                    sb.append(indent(indent));
                    sb.append(openTag(name));
                    prefix = NEWLINE;
                }
                final String asXml = toXml(name, inner, indent + 1, path);
                if (StringUtils.isNotEmpty(asXml)) {
                    sb.append(prefix).append(asXml);
                }
                if (name != null) {
                    sb.append(NEWLINE);
                    sb.append(indent(indent));
                    sb.append(closeTag(name));
                }
            } else {
                sb.append(toXml(name, inner, indent, path));
            }
        } else if (StringUtils.isNotEmpty(schema.get$ref())) {
            Schema actualSchema = examples.get(schema.get$ref());
            sb.append(toXml(actualSchema, indent, path));
        } else {
            if (name != null) {
                sb.append(indent(indent));
                sb.append(openTag(name));
            }
            sb.append(getExample(schema));
            if (name != null) {
                sb.append(closeTag(name));
            }
        }
        return sb.toString();
    }

    /**
    * Get the example string value for the given schema.
    *
    * If an example value was not provided in the specification, a default will be generated.
    *
    * @param schema Schema to get example string for
    *
    * @return Example String
    */
    protected String getExample(Schema schema) {
        if (schema.getExample() != null) {
            return schema.getExample().toString();
        } else if (schema instanceof DateTimeSchema) {
            return "2000-01-23T04:56:07.000Z";
        } else if (schema instanceof DateSchema) {
            return "2000-01-23";
        } else if (schema instanceof BooleanSchema) {
            return "true";
        } else if (schema instanceof NumberSchema) {
            if (SchemaTypeUtil.FLOAT_FORMAT.equals(schema.getFormat())) { // float
                return "1.3579";
            } else { // double
                return "3.149";
            }
        } else if (schema instanceof PasswordSchema) {
            return "********";
        } else if (schema instanceof UUIDSchema) {
            return "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
        // do these last in case the specific types above are derived from these classes
        } else if (schema instanceof StringSchema) {
            return "aeiou";
        } else if (schema instanceof IntegerSchema) {
            if (SchemaTypeUtil.INTEGER32_FORMAT.equals(schema.getFormat())) { // integer
                return "123";
            } else { //long
                return "123456789";
            }
        }
        LOGGER.warn("default example value not implemented for " + schema);
        return "";
    }

    @SuppressWarnings("static-method")
    protected String openTag(String name) {
        return "<" + name + ">";
    }

    @SuppressWarnings("static-method")
    protected String closeTag(String name) {
        return "</" + name + ">";
    }

    @SuppressWarnings("static-method")
    protected String indent(int indent) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
}