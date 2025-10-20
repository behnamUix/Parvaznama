import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//flightDate	تاریخ پرواز (مثلاً "2025-10-08")
//flightStatus	وضعیت پرواز: "scheduled", "active", "landed", "cancelled" و...
//departure	جزئیات مبدأ (فرودگاه و ساعت‌ها)
//arrival	جزئیات مقصد (فرودگاه و ساعت‌ها)
//aircraft	نوع و شماره‌ی هواپیما (اگه داده باشه)
//live	وضعیت زنده (درحال پرواز، سرعت، موقعیت مکانی و...)

// ✅ پاسخ کلی API پروازها
@Serializable
data class FlightResponse(
    val pagination: Pagination,
    val data: List<FlightData>
)

// ✅ صفحه‌بندی (برای درخواست‌های محدودشده)
@Serializable
data class Pagination(
    val limit: Int,
    val offset: Int,
    val count: Int,
    val total: Int
)

// ✅ اطلاعات هر پرواز
@Serializable
data class FlightData(
    @SerialName("flight_date") val flightDate: String? = null,
    @SerialName("flight_status") val flightStatus: String? = null,
    val departure: FlightDeparture? = null,
    val arrival: FlightArrival? = null,
    val airline: Airline? = null,
    val flight: FlightDetail? = null,
    val aircraft: Aircraft? = null,
    val live: Live? = null
)

@Serializable
data class Airline(
    val name: String? = null,
    val iata: String? = null,
    val icao: String? = null
)

@Serializable
data class FlightDetail(
    val number: String? = null,
    val iata: String? = null,
    val icao: String? = null,
    val codeshared: Codeshared? = null
)

@Serializable
data class Codeshared(
    @SerialName("airline_name") val airlineName: String? = null,
    @SerialName("airline_iata") val airlineIata: String? = null,
    @SerialName("airline_icao") val airlineIcao: String? = null,
    @SerialName("flight_number") val flightNumber: String? = null,
    @SerialName("flight_iata") val flightIata: String? = null,
    @SerialName("flight_icao") val flightIcao: String? = null
)

// ✅ جزئیات فرود مبدأ
@Serializable
data class FlightDeparture(
    val airport: String? = null,
    val timezone: String? = null,
    val iata: String? = null,
    val icao: String? = null,
    val terminal: String? = null,
    val gate: String? = null,
    val delay: Int? = null,
    val scheduled: String? = null,
    val estimated: String? = null,
    val actual: String? = null,
    @SerialName("estimated_runway") val estimatedRunway: String? = null,
    @SerialName("actual_runway") val actualRunway: String? = null
)

// ✅ جزئیات فرود مقصد
@Serializable
data class FlightArrival(
    val airport: String? = null,
    val timezone: String? = null,
    val iata: String? = null,
    val icao: String? = null,
    val terminal: String? = null,
    val gate: String? = null,
    val baggage: String? = null,
    val delay: Int? = null,
    val scheduled: String? = null,
    val estimated: String? = null,
    val actual: String? = null,
    @SerialName("estimated_runway") val estimatedRunway: String? = null,
    @SerialName("actual_runway") val actualRunway: String? = null
)

// ✅ نوع و شماره‌ی هواپیما
@Serializable
data class Aircraft(
    val registration: String? = null,
    val iata: String? = null,
    val icao: String? = null,
    val icao24: String? = null
)

// ✅ وضعیت زنده پرواز
@Serializable
data class Live(
    val updated: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val altitude: Double? = null,
    val direction: Double? = null,
    @SerialName("speed_horizontal") val speedHorizontal: Double? = null,
    @SerialName("speed_vertical") val speedVertical: Double? = null,
    @SerialName("is_ground") val isGround: Boolean? = null
)

// ✅ مدل فرودگاه (برای فایل airports.json)
@Serializable
data class AirportDto(
    val icao: String? = null,
    val iata: String? = null,
    val name: String? = null,
    val city: String? = null,
    val country: String? = null,
    val elevation: Int? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    @SerialName("tz") val timezone: String? = null
) {
    // مختصات تبدیل‌شده به Double
    val latDouble: Double? get() = lat
    val lonDouble: Double? get() = lon
}
