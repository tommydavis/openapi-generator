package org.openapitools.codegen.kotlin;

import org.openapitools.codegen.AbstractOptionsTest;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.languages.KotlinClientCodegen;
import org.openapitools.codegen.options.KotlinClientOptionsProvider;

import mockit.Expectations;
import mockit.Tested;

public class KotlinClientCodegenOptionsTest extends AbstractOptionsTest {

    @Tested
    private KotlinClientCodegen codegen;

    public KotlinClientCodegenOptionsTest() {
        super(new KotlinClientOptionsProvider());
    }

    @Override
    protected CodegenConfig getCodegenConfig() {
        return codegen;
    }

    @SuppressWarnings("unused")
    @Override
    protected void setExpectations() {
        new Expectations(codegen) {{
            codegen.setPackageName(KotlinClientOptionsProvider.PACKAGE_NAME_VALUE);
            times = 1;
            codegen.setArtifactVersion(KotlinClientOptionsProvider.ARTIFACT_VERSION_VALUE);
            times = 1;
            codegen.setArtifactId(KotlinClientOptionsProvider.ARTIFACT_ID);
            times = 1;
            codegen.setGroupId(KotlinClientOptionsProvider.GROUP_ID);
            times = 1;
            codegen.setSourceFolder(KotlinClientOptionsProvider.SOURCE_FOLDER);
            times = 1;
            codegen.setEnumPropertyNaming(KotlinClientOptionsProvider.ENUM_PROPERTY_NAMING);
            times = 1;
            codegen.setDateLibrary(KotlinClientOptionsProvider.DATE_LIBRARY);
        }};
    }
}

