package com.playko.parkingservice.configuration;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SWAGGER_TITLE_MESSAGE = "User API";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "User microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";
    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String USER_IS_NOT_PARTNER_MESSAGE = "El usuario no es socio";
    public static final String NO_DATA_FOUND_MESSAGE = "Datos no encontrados";
    public static final String INVALID_ASSIGNED_PARTNER_MESSAGE = "El usuario no es socio del estacionamiento";
    public static final String PARKING_CREATED_MESSAGE = "Estacionamiento creado satisfactoriamente";
    public static final String PARKING_UPDATE_MESSAGE = "Estacionamiento actualizado satisfactoriamente";
    public static final String PARKING_DELETE_MESSAGE = "Estacionamiento eliminado satisfactoriamente";
    public static final String NAME_IS_REQUIRED_MESSAGE = "El campo 'name' no puede estar vacío";
    public static final String MAXIMUMCAPACITY_IS_REQUIRED_MESSAGE = "El campo 'maximumCapacity' no puede estar vacío";
    public static final String COSTPERHOUR_IS_REQUIRED_MESSAGE = "El campo 'costPerHour' no puede estar vacío";
}
