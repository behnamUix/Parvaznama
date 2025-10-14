package com.example.parvaznama.utils

import android.content.Context
import com.example.parvaznama.R
import com.opencsv.CSVReader
import java.io.InputStreamReader

data class Airport(
    val iata: String,
    val city: String,
    val country: String
)


class AirportRepository(context: Context) {

    private val airports: List<Airport>

    init {
        val inputStream = context.resources.openRawResource(R.raw.airports)
        val reader = CSVReader(InputStreamReader(inputStream))

        // خواندن سطرها
        val allRows = reader.readAll()

        // فرض می‌کنیم ردیف اول header است
        val header = allRows.firstOrNull()
        val iataIndex = header?.indexOf("iata_code") ?: 0
        val cityIndex = header?.indexOf("municipality") ?: 1
        val countryIndex = header?.indexOf("iso_country") ?: 2

        airports = allRows.drop(1) // حذف header
            .mapNotNull { row ->
                val iata = row.getOrNull(iataIndex)?.trim()
                val city = row.getOrNull(cityIndex)?.trim()
                val country = row.getOrNull(countryIndex)?.trim()
                if (!iata.isNullOrEmpty() && !city.isNullOrEmpty()) Airport(
                    iata,
                    city,
                    country ?: ""
                ) else null
            }
    }

    fun getCityByIATA(iataCode: String): String? {
        return airports.find { it.iata.equals(iataCode, ignoreCase = true) }?.city
    }

    fun getCountryByIATA(iataCode: String): String? {
        return airports.find { it.iata.equals(iataCode, ignoreCase = true) }?.country
    }

    fun getCountryName(code: String): String {
        val countries = mapOf(
            "AF" to "Afghanistan",
            "AL" to "Albania",
            "DZ" to "Algeria",
            "AS" to "American Samoa",
            "AD" to "Andorra",
            "AO" to "Angola",
            "AR" to "Argentina",
            "AM" to "Armenia",
            "AW" to "Aruba",
            "AU" to "Australia",
            "AT" to "Austria",
            "AZ" to "Azerbaijan",
            "BS" to "Bahamas",
            "BH" to "Bahrain",
            "BD" to "Bangladesh",
            "BB" to "Barbados",
            "BY" to "Belarus",
            "BE" to "Belgium",
            "BZ" to "Belize",
            "BJ" to "Benin",
            "BM" to "Bermuda",
            "BT" to "Bhutan",
            "BO" to "Bolivia",
            "BA" to "Bosnia and Herzegovina",
            "BW" to "Botswana",
            "BR" to "Brazil",
            "BN" to "Brunei",
            "BG" to "Bulgaria",
            "BF" to "Burkina Faso",
            "BI" to "Burundi",
            "KH" to "Cambodia",
            "CM" to "Cameroon",
            "CA" to "Canada",
            "CV" to "Cape Verde",
            "KY" to "Cayman Islands",
            "CF" to "Central African Republic",
            "TD" to "Chad",
            "CL" to "Chile",
            "CN" to "China",
            "CX" to "Christmas Island",
            "CC" to "Cocos Islands",
            "CO" to "Colombia",
            "KM" to "Comoros",
            "CD" to "Congo (Democratic Republic)",
            "CG" to "Congo (Republic)",
            "CK" to "Cook Islands",
            "CR" to "Costa Rica",
            "HR" to "Croatia",
            "CU" to "Cuba",
            "CW" to "Curaçao",
            "CY" to "Cyprus",
            "CZ" to "Czech Republic",
            "DK" to "Denmark",
            "DJ" to "Djibouti",
            "DM" to "Dominica",
            "DO" to "Dominican Republic",
            "EC" to "Ecuador",
            "EG" to "Egypt",
            "SV" to "El Salvador",
            "GQ" to "Equatorial Guinea",
            "ER" to "Eritrea",
            "EE" to "Estonia",
            "SZ" to "Eswatini",
            "ET" to "Ethiopia",
            "FJ" to "Fiji",
            "FI" to "Finland",
            "FR" to "France",
            "GA" to "Gabon",
            "GM" to "Gambia",
            "GE" to "Georgia",
            "DE" to "Germany",
            "GH" to "Ghana",
            "GR" to "Greece",
            "GD" to "Grenada",
            "GT" to "Guatemala",
            "GN" to "Guinea",
            "GW" to "Guinea-Bissau",
            "GY" to "Guyana",
            "HT" to "Haiti",
            "HN" to "Honduras",
            "HU" to "Hungary",
            "IS" to "Iceland",
            "IN" to "India",
            "ID" to "Indonesia",
            "IR" to "Iran",
            "IQ" to "Iraq",
            "IE" to "Ireland",
            "IL" to "Israel",
            "IT" to "Italy",
            "JM" to "Jamaica",
            "JP" to "Japan",
            "JO" to "Jordan",
            "KZ" to "Kazakhstan",
            "KE" to "Kenya",
            "KI" to "Kiribati",
            "KW" to "Kuwait",
            "KG" to "Kyrgyzstan",
            "LA" to "Laos",
            "LV" to "Latvia",
            "LB" to "Lebanon",
            "LS" to "Lesotho",
            "LR" to "Liberia",
            "LY" to "Libya",
            "LI" to "Liechtenstein",
            "LT" to "Lithuania",
            "LU" to "Luxembourg",
            "MG" to "Madagascar",
            "MW" to "Malawi",
            "MY" to "Malaysia",
            "MV" to "Maldives",
            "ML" to "Mali",
            "MT" to "Malta",
            "MH" to "Marshall Islands",
            "MR" to "Mauritania",
            "MU" to "Mauritius",
            "MX" to "Mexico",
            "FM" to "Micronesia",
            "MD" to "Moldova",
            "MC" to "Monaco",
            "MN" to "Mongolia",
            "ME" to "Montenegro",
            "MA" to "Morocco",
            "MZ" to "Mozambique",
            "MM" to "Myanmar",
            "NA" to "Namibia",
            "NR" to "Nauru",
            "NP" to "Nepal",
            "NL" to "Netherlands",
            "NZ" to "New Zealand",
            "NI" to "Nicaragua",
            "NE" to "Niger",
            "NG" to "Nigeria",
            "KP" to "North Korea",
            "MK" to "North Macedonia",
            "NO" to "Norway",
            "OM" to "Oman",
            "PK" to "Pakistan",
            "PW" to "Palau",
            "PA" to "Panama",
            "PG" to "Papua New Guinea",
            "PY" to "Paraguay",
            "PE" to "Peru",
            "PH" to "Philippines",
            "PL" to "Poland",
            "PT" to "Portugal",
            "QA" to "Qatar",
            "RO" to "Romania",
            "RU" to "Russia",
            "RW" to "Rwanda",
            "KN" to "Saint Kitts and Nevis",
            "LC" to "Saint Lucia",
            "VC" to "Saint Vincent and the Grenadines",
            "WS" to "Samoa",
            "SM" to "San Marino",
            "ST" to "Sao Tome and Principe",
            "SA" to "Saudi Arabia",
            "SN" to "Senegal",
            "RS" to "Serbia",
            "SC" to "Seychelles",
            "SL" to "Sierra Leone",
            "SG" to "Singapore",
            "SK" to "Slovakia",
            "SI" to "Slovenia",
            "SB" to "Solomon Islands",
            "SO" to "Somalia",
            "ZA" to "South Africa",
            "KR" to "South Korea",
            "SS" to "South Sudan",
            "ES" to "Spain",
            "LK" to "Sri Lanka",
            "SD" to "Sudan",
            "SR" to "Suriname",
            "SE" to "Sweden",
            "CH" to "Switzerland",
            "SY" to "Syria",
            "TJ" to "Tajikistan",
            "TZ" to "Tanzania",
            "TH" to "Thailand",
            "TL" to "Timor-Leste",
            "TG" to "Togo",
            "TO" to "Tonga",
            "TT" to "Trinidad and Tobago",
            "TN" to "Tunisia",
            "TR" to "Turkey",
            "TM" to "Turkmenistan",
            "TV" to "Tuvalu",
            "UG" to "Uganda",
            "UA" to "Ukraine",
            "AE" to "United Arab Emirates",
            "GB" to "United Kingdom",
            "US" to "United States",
            "UY" to "Uruguay",
            "UZ" to "Uzbekistan",
            "VU" to "Vanuatu",
            "VA" to "Vatican City",
            "VE" to "Venezuela",
            "VN" to "Vietnam",
            "YE" to "Yemen",
            "ZM" to "Zambia",
            "ZW" to "Zimbabwe"
        )

        return countries[code.uppercase()] ?: code
    }
}

