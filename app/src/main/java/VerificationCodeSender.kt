import android.os.StrictMode
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import kotlin.random.Random

class VerificationCodeSender(private val message: String, val codeLength: Int) {
    companion object {
        private const val ALPHABET = "1234567890"
        private const val TOKEN = "C834B5D4-73EA-EF4B-391C-580B66450271&to=79604873962"
    }

    enum class SendVerificationMessageRequestCode {
        SUCCESSFUL, NETWORK_ISSUE
    }

    init {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
    }

    private var key: String? = null
    private val client = OkHttpClient()

    fun generateKey() {
        key = ""
        repeat(codeLength) {
            key += ALPHABET[Random.nextInt(0, ALPHABET.length - 1)]
        }
        println(key)
    }

    fun sendVerificationCode(phoneNumber: String): SendVerificationMessageRequestCode {
        if (key == null) generateKey()

        var responseIsSuccessful: Boolean

        client.newCall(
            Request.Builder().url("https://sms.ru/sms/send?api_id=$TOKEN&to=$phoneNumber&msg=${
                URLEncoder.encode(
                    message.replace(
                        "%key%",
                        key!!
                    ), "UTF-8"
                )
            }&json=1").build()
        ).execute().use { response ->
            responseIsSuccessful = response.isSuccessful
            println(response.body?.string())
        }

        return if (responseIsSuccessful) SendVerificationMessageRequestCode.SUCCESSFUL else SendVerificationMessageRequestCode.NETWORK_ISSUE

//        try {
//            client.newCall(request).execute().use { response ->
//                if (!response.isSuccessful) {
//                    throw IOException()
//                }
//                // пример получения конкретного заголовка ответа
//                println("Server: ${response.header("Server")}")
//                // вывод тела ответа
//                println(response.body!!.string())
//            }
//        } catch (e: IOException) {
//            println("Ошибка подключения: $e");
//        }
    }

    fun isValidKey(key: String): Boolean {
        if (key == this.key) {
            this.key = null
            return true
        }

        return false
    }
}