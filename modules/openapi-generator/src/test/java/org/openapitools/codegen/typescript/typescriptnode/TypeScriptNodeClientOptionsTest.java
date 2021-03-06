package org.openapitools.codegen.typescript.typescriptnode;

import org.openapitools.codegen.AbstractOptionsTest;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.languages.TypeScriptNodeClientCodegen;
import org.openapitools.codegen.options.TypeScriptNodeClientOptionsProvider;

import mockit.Expectations;
import mockit.Tested;

public class TypeScriptNodeClientOptionsTest extends AbstractOptionsTest {

    @Tested
    private TypeScriptNodeClientCodegen clientCodegen;

    public TypeScriptNodeClientOptionsTest() {
        super(new TypeScriptNodeClientOptionsProvider());
    }

    @Override
    protected CodegenConfig getCodegenConfig() {
        return clientCodegen;
    }

    @SuppressWarnings("unused")
    @Override
    protected void setExpectations() {
        new Expectations(clientCodegen) {{
            clientCodegen.setSortParamsByRequiredFlag(Boolean.valueOf(TypeScriptNodeClientOptionsProvider.SORT_PARAMS_VALUE));
            times = 1;
            clientCodegen.setModelPropertyNaming(TypeScriptNodeClientOptionsProvider.MODEL_PROPERTY_NAMING_VALUE);
            times = 1;
            clientCodegen.setSupportsES6(Boolean.valueOf(TypeScriptNodeClientOptionsProvider.SUPPORTS_ES6_VALUE));
            times = 1;
            clientCodegen.setPrependFormOrBodyParameters(Boolean.valueOf(TypeScriptNodeClientOptionsProvider.PREPEND_FORM_OR_BODY_PARAMETERS_VALUE));
            times = 1;
        }};
    }
}
