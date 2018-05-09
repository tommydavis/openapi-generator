## Migration guide: from Swagger Codegen to OpenAPI Generator

OpenAPI Generator is a fork of `swagger-codegen` between version `2.3.1` and `2.4.0`.
This community-driven version called "OpenAPI Generator" provides similar functionalities and can be used as drop-in replacement.
This guide explains the major differences in order to ease with the migration.

### New maven coordinates

You can find our released artefact on maven central:

**Core:**

Old:

```
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-codegen</artifactId>
</dependency>
```

New:

```
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator</artifactId>
</dependency>
```

**Cli:**

```
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-codegen-cli</artifactId>
</dependency>
```

New:

```
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-cli</artifactId>
</dependency>
```

**Maven plugin:**

```
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-codegen-maven-plugin</artifactId>
</dependency>
```

New:

```
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
</dependency>
```


### New generator names

Some language name were changed, in order to be more consistent.

| name in `swagger-codegen` | name in `openapi-generator`  |
|--|--|
| `akka-scala` | `scala-akka` |
| `scala` | `scala-httpclient` |
| `jaxrs` | `jaxrs-jersey` |
| `qt5cpp` | `cpp-qt5` |
| `swift` | `swift2-deprecated` |


### New parameter name

Some parameters were renamed.
Often you need to replace "Swagger", with "OpenAPI"

| name in `swagger-codegen` | name in `openapi-generator`  |
|--|--|
| `debugSwagger` | `debugOpenAPI` |
| `GenerateSwaggerMetadata` | `GenerateOpenAPIMetadata` |
| `swagger.codegen.undertow.apipackage` | `openapi.codegen.undertow.apipackage` |
| `swagger.codegen.undertow.modelpackage` | `openapi.codegen.undertow.modelpackage` |


### Ignore file

`.swagger-codegen-ignore` is replaced by `.openapi-generator-ignore`.
The syntax inside the file stays the same.

You do not need to rename the file by yourself, OpenAPI Generator will do it when your run it.
(If only a `.swagger-codegen-ignore` file is present it will be considered).


### Metadata folder

The metatata folder (to store the `VERSION` file) is now called `.openapi-generator/` instead of `.swagger-codegen/`.



### New default values for the generated code

If you use a generator without specifying each value, you might see some differences in the generated code.
As example the default package name used in the generated code is new: `OpenAPITools` instead of `Swagger` and `org.openapitools` instead of `io.swagger`. Concretely if you did not specify anything when you are generating java code, a file `org/openapitools/api/PetApi.java` instead of `io/swagger/api/PetApi.java`.

If this is a problem for you, you need to explicitly the the parameter to match with the old value (`apiPackage` == `io.swagger` in the previous example).


### New fully qualified name for the classes 

If you have extended some generators in your project, and you are looking for a specific class, replace the  `io.swagger.codegen` package (old name) with `org.openapitools.codegen` package (new name).

Example: `org.openapitools.codegen.DefaultGenerator`


[Back to OpenAPI-Generator's README page](../README.md)
