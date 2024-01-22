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
    public static final String PLATE_ALREADY_EXISTS_MESSAGE = "No se puede Registrar Ingreso, ya existe la placa en este u otro parqueadero";
    public static final String PARKING_FULL_MESSAGE = "El estacionamiento esta en su capacidad maxima";
    public static final String PLATE_NOT_REGISTERED_MESSAGE = "No se puede Registrar Salida, no existe la placa en el parqueadero";
    public static final String REGISTERED_OUTPUT_MESSAGE = "Salida registrada";
    public static final String PARKING_NOT_FOUND = "No se encontró el parqueadero con el ID especificado.";
    public static final String PLATE_IN_PARKING_MESSAGE = "Se registro la entrada de un nuevo vehiculo.";
}
