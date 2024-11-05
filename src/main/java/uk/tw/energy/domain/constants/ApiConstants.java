package uk.tw.energy.domain.constants;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
public class ApiConstants {

    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String BASE_API_URI = "/api/joe";
    public static final String API_VERSION_V2 = BASE_API_URI + "/v2";
    public static final String METERS_API_V2 = API_VERSION_V2 + "/meters";
    public static final String METER_WITH_ID_API_V2 = METERS_API_V2 + "/{smartMeterId}";
    public static final String READINGS_API_V2 = METER_WITH_ID_API_V2 + "/readings";

}
