import java.util.*

data class ModemPackage(
    var dir: Int = 0,
    var level: Int = 0,
    var nid: Int = 0,
    var group: Int = 0,
    var mac: String = "",
    var smac: String = "",
    var rssi: Int = 0,
    var mrssi: Int = 0,
    var rfch: Int = 0,
    var rfpwr: Int = 0,
    var pwm: Int = 0,
    var pwmct: Int = 0,
    var pow: Int = 0,
    var lux: Int = 0,
    var temp: Int = 0,
    var energy: Int = 0,
    var rng: Int = 0,
    var tlevel: Int = 0,
    var date: Int = 0,
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var vtd: Int = 0,
    var vll: Int = 0,
    var rise: String = "",
    var set: String = "",
    var id: Int = 0,
    var d1: Int = 0,
    var p1: Int = 0,
    var d2: Int = 0,
    var p2: Int = 0,
    var scdtm: Int = 0,
    var pwm0: Int = 0,
    var time1: String = "",
    var pwm1: Int = 0,
    var time2: String = "",
    var pwm2: Int = 0,
    var time3: String = "",
    var pwm3: Int = 0,
    var time4: String = "",
    var pwm4: Int = 0,
    var received: Date? = null
)