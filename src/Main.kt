import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import java.io.StringReader
import java.lang.RuntimeException
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

val packageMain = """
    {"mac":"0x119F6736","tlevel":1,"rng":16160,"smac":"0x1984263A","dir":2,"nid":44234,"group":0,"level":1,
     "mrssi":-101,"rssi":-99,"devt":2,"devm":1,"rfch":6,"rfpwr":12,"scdtm":3600,"eblk":0,"cycles":"14_57",
     "runh":4632,"nvsc":161,"lpwm":10,"cpwm":100,"pow":61,"lux":0,"temp":6,"energy":55082,"pwm":100,"pwmct":0,
     "date":1570466712,"lat":"55.809517","lon":"37.833752","val":71,"rise":"03-05","set":"15-28","id":4,"d1":0,
     "p1":70,"d2":0,"p2":50,"rfps":0,"twil":3,"crc":"0xC188"}
""".trimIndent()

val packageAdditional = """
    {"mac":"0x119F6736","tlevel":1,"rng":39465,"smac":"0x1984263A","dir":2,"nid":44234,"group":0,"level":1,
     "mrssi":-101,"rssi":-100,"devt":2,"devm":1,"id":4,"pwm0":100,"time1":"20-00","pwm1":70,"time2":"21-30",
     "pwm2":50,"time3":"03-00","pwm3":70,"time4":"04-00","pwm4":100,"crc":"0xCE7D"}
""".trimIndent()

val packageList = """
    {"mac":"0x119F6736","tlevel":1,"rng":26690,"smac":"0x1984263A","dir":2,"nid":44234,"group":0,"level":1,
     "mrssi":-101,"rssi":-101,"devt":2,"devm":1,"crc":"0x3B2A"}
""".trimIndent()


class JsonParser {
    private var prevName: String = ""

    fun parsePackage(packageJson: String, modemPackage: ModemPackage) {
        prevName = ""
        val reader = JsonReader(StringReader(packageJson))
        reader.beginObject()
        while (reader.hasNext()) {
            val token = reader.peek();
            if (token == JsonToken.END_OBJECT) {
                reader.endObject()
                return
            } else {
                handleNonArrayToken(reader, token, modemPackage)
            }
        }
    }

    private fun handleNonArrayToken(reader: JsonReader, token: JsonToken, modemPackage: ModemPackage) {

        when (token) {
            JsonToken.NAME -> prevName = reader.nextName()
            JsonToken.STRING -> {
                val value = reader.nextString()
                try {
                    var property = ModemPackage::class.memberProperties.single { it.name == prevName } as KMutableProperty1<ModemPackage, *>
                    when (property.returnType) {
                        String::class.createType() -> property.setter.call(modemPackage, value)
                        Int::class.createType() -> property.setter.call(modemPackage, value.toInt())
                        Double::class.createType() -> property.setter.call(modemPackage, value.toDouble())
                        else -> {}
                    }
                } catch (e: RuntimeException) {
                }
            }
            JsonToken.NUMBER -> {
                try {
                    var property =
                        ModemPackage::class.memberProperties.single { it.name == prevName } as KMutableProperty1<ModemPackage, *>
                    when (property.returnType) {
                        Int::class.createType() -> property.setter.call(modemPackage, reader.nextInt())
                        Double::class.createType() -> property.setter.call(modemPackage, reader.nextDouble())
                        else -> reader.skipValue()
                    }
                } catch (e: RuntimeException) {
                    reader.skipValue()
                }
            }
            else -> reader.skipValue()
        }
    }
}


var prevName: String = ""

fun main(args: Array<String>) {
    val gson = Gson()
    var modemMainPackage = gson.fromJson(packageMain, ModemPackage::class.java)
    var modemAdditionalPackage = gson.fromJson(packageAdditional, ModemPackage::class.java)
    var modemListPackage = gson.fromJson(packageList, ModemPackage::class.java)
    println("mainPackage : $modemMainPackage")
    println("additionalPackage : $modemAdditionalPackage")
    println("listPackage : $modemListPackage")

    if (modemListPackage.rise == null) {
        println("Null rise")
    }

    val modemPackage = ModemPackage()

    val parser = JsonParser()

    parser.parsePackage(packageList, modemPackage)
    parser.parsePackage(packageMain, modemPackage)
    parser.parsePackage(packageAdditional, modemPackage)

    println("modemPackage: $modemPackage")
}

