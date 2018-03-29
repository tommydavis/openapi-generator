package org.openapitools.codegen.languages;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.DefaultCodegen;
import org.openapitools.codegen.SupportingFile;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.util.SchemaTypeUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class TizenClientCodegen extends DefaultCodegen implements CodegenConfig {
    protected static String PREFIX = "ArtikCloud";
    protected String sourceFolder = "src";
    protected String documentationFolder = "doc";

    public TizenClientCodegen() {
        super();
        outputFolder = "";
        modelTemplateFiles.put("model-header.mustache", ".h");
        modelTemplateFiles.put("model-body.mustache", ".cpp");
        apiTemplateFiles.put("api-header.mustache", ".h");
        apiTemplateFiles.put("api-body.mustache", ".cpp");
        embeddedTemplateDir = templateDir = "tizen";
        modelPackage = "";

        defaultIncludes = new HashSet<String>(
                Arrays.asList(
                        "bool",
                        "int",
                        "long long",
                        "double",
                        "float")
        );
        languageSpecificPrimitives = new HashSet<String>(
                Arrays.asList(
                        "bool",
                        "int",
                        "long long",
                        "double",
                        "float",
                        "std::string")
        );

        additionalProperties().put("prefix", PREFIX);

        setReservedWordsLowerCase(
                Arrays.asList(
                        "alignas", "alignof", "and", "and_eq", "asm", "atomic_cancel", "atomic_commit", "atomic_noexcept",
                        "auto", "bitand", "bitor", "bool", "break", "case", "catch", "char", "char16_t", "char32_t",
                        "class", "compl", "concept", "const", "constexpr", "const_cast", "continue", "decltype", "default",
                        "delete", "do", "double", "dynamic_cast", "else", "enum", "explicit", "export", "extern", "false",
                        "float", "for", "friend", "goto", "if", "inline", "int", "import", "long", "module", "mutable",
                        "namespace", "new", "noexcept", "not", "not_eq", "nullptr", "operator", "or", "or_eq", "private",
                        "protected", "public", "register", "reinterpret_cast", "requires", "return", "short", "signed",
                        "sizeof", "static", "static_assert", "static_cast", "struct", "switch", "synchronized", "template",
                        "this", "thread_local", "throw", "true", "try", "typedef", "typeid", "typename", "union",
                        "unsigned", "using", "virtual", "void", "volatile", "wchar_t", "while", "xor", "xor_eq"
                ));

        super.typeMapping = new HashMap<String, String>();

        //typeMapping.put("Date", "DateTime");
        //typeMapping.put("DateTime", "DateTime");
        typeMapping.put("string", "std::string");
        typeMapping.put("integer", "int");
        typeMapping.put("float", "float");
        typeMapping.put("long", "long long");
        typeMapping.put("boolean", "bool");
        typeMapping.put("double", "double");
        typeMapping.put("array", "std::list");
        typeMapping.put("map", "std::map");
        typeMapping.put("number", "long long");
        typeMapping.put("object", "std::string");
        typeMapping.put("binary", "std::string");
        typeMapping.put("password", "std::string");
        //TODO:Maybe use better formats for dateTime?
        typeMapping.put("file", "std::string");
        typeMapping.put("DateTime", "std::string");
        typeMapping.put("Date", "std::string");
        typeMapping.put("UUID", "std::string");

        importMapping = new HashMap<String, String>();

        supportingFiles.clear();
        supportingFiles.add(new SupportingFile("helpers-header.mustache", sourceFolder, "Helpers.h"));
        supportingFiles.add(new SupportingFile("helpers-body.mustache", sourceFolder, "Helpers.cpp"));
        supportingFiles.add(new SupportingFile("netclient-header.mustache", sourceFolder, "NetClient.h"));
        supportingFiles.add(new SupportingFile("netclient-body.mustache", sourceFolder, "NetClient.cpp"));
        supportingFiles.add(new SupportingFile("object.mustache", sourceFolder, "Object.h"));
        supportingFiles.add(new SupportingFile("requestinfo.mustache", sourceFolder, "RequestInfo.h"));
        supportingFiles.add(new SupportingFile("error-header.mustache", sourceFolder, "Error.h"));
        supportingFiles.add(new SupportingFile("error-body.mustache", sourceFolder, "Error.cpp"));
        supportingFiles.add(new SupportingFile("Doxyfile.mustache", documentationFolder, "Doxyfile"));
        supportingFiles.add(new SupportingFile("generateDocumentation.mustache", documentationFolder, "generateDocumentation.sh"));
        supportingFiles.add(new SupportingFile("doc-readme.mustache", documentationFolder, "README.md"));
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    @Override
    public String getName() {
        return "tizen";
    }

    @Override
    public String getHelp() {
        return "Generates a Samsung Tizen C++ client library.";
    }

    @Override
    public String toInstantiationType(Schema p) {
        if (p instanceof MapSchema) {
            return instantiationTypes.get("map");
        } else if (p instanceof ArraySchema) {
            return instantiationTypes.get("array");
        } else {
            return null;
        }
    }

    @Override
    public String getTypeDeclaration(String name) {
        if (languageSpecificPrimitives.contains(name)) {
            return name;
        } else {
            return name + "";
        }
    }

    @Override
    public String getSchemaType(Schema p) {
        String openAPIType = super.getSchemaType(p);
        String type = null;
        if (typeMapping.containsKey(openAPIType)) {
            type = typeMapping.get(openAPIType);
            if (languageSpecificPrimitives.contains(type)) {
                return toModelName(type);
            }
        } else {
            type = openAPIType;
        }
        return toModelName(type);
    }

    @Override
    public String getTypeDeclaration(Schema p) {
        String openAPIType = getSchemaType(p);
        if (languageSpecificPrimitives.contains(openAPIType)) {
            return toModelName(openAPIType);
        } else {
            return openAPIType + "";
        }
    }

    @Override
    public String toModelName(String type) {
        if (typeMapping.keySet().contains(type) ||
                typeMapping.values().contains(type) ||
                importMapping.values().contains(type) ||
                defaultIncludes.contains(type) ||
                languageSpecificPrimitives.contains(type)) {
            return type;
        } else {
            return Character.toUpperCase(type.charAt(0)) + type.substring(1);
        }
    }

    @Override
    public String toModelImport(String name) {
        if (name.equals("std::string")) {
            return "#include <string>";
        } else if (name.equals("std::map")) {
            return "#include <map>";
        } else if (name.equals("std::list")) {
            return "#include <list>";
        }
        return "#include \"" + name + ".h\"";
    }

    //Might not be needed
    @Override
    public String toDefaultValue(Schema p) {
        if (p instanceof StringSchema) {
            return "std::string()";
        } else if (p instanceof BooleanSchema) {
            return "bool(false)";
        } else if (p instanceof NumberSchema) {
            if (SchemaTypeUtil.FLOAT_FORMAT.equals(p.getFormat())) {
                return "float(0)";
            }
            return "double(0)";

        } else if (p instanceof IntegerSchema) {
            if (SchemaTypeUtil.INTEGER64_FORMAT.equals(p.getFormat())) {
                return "long(0)";
            }
            return "int(0)";
        } else if (p instanceof MapSchema) {
            return "new std::map()";
        } else if (p instanceof ArraySchema) {
            return "new std::list()";
        } else if (!StringUtils.isEmpty(p.get$ref())) {
            return "new " + toModelName(p.get$ref()) + "()";
        }
        return "null";
    }


    @Override
    public String apiFileFolder() {
        return outputFolder + File.separator + sourceFolder;
    }

    @Override
    public String modelFileFolder() {
        return outputFolder + File.separator + sourceFolder;
    }

    @Override
    public String toModelFilename(String name) {
        return initialCaps(name);
    }

    @Override
    public String toApiName(String name) {
        return initialCaps(name) + "Manager";
    }

    @Override
    public String toApiFilename(String name) {
        return initialCaps(name) + "Manager";
    }

    @Override
    public String toVarName(String name) {
        String paramName = name.replaceAll("[^a-zA-Z0-9_]", "");
        paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
        if (isReservedWord(paramName)) {
            return escapeReservedWord(paramName);
        }
        return "" + paramName;
    }

    @Override
    public String escapeReservedWord(String name) {
        if (this.reservedWordsMappings().containsKey(name)) {
            return this.reservedWordsMappings().get(name);
        }
        return "_" + name;
    }

    @Override
    public String toOperationId(String operationId) {
        // throw exception if method name is empty
        if (operationId == "") {
            throw new RuntimeException("Empty method name (operationId) not allowed");
        }

        // method name cannot use reserved keyword, e.g. return$
        if (isReservedWord(operationId)) {
            operationId = escapeReservedWord(operationId);
        }

        // add_pet_by_id => addPetById
        return camelize(operationId, true);
    }

}