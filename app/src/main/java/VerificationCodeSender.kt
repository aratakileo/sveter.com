import android.os.StrictMode
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import kotlin.random.Random
import kotlin.random.nextInt

class VerificationCodeSender(private val token: String, private val message: String, val codeLength: Int) {
    enum class SendVerificationMessageRequestCode {
        SUCCESSFUL, NETWORK_ISSUE
    }

    init {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
    }

    private var code: String? = null
    private val client = OkHttpClient()

    fun generateCode() {
        code = ""

        repeat(codeLength) {
            code += "1234567890"[Random.nextInt(0..9)]
        }
    }

    fun sendVerificationCode(phoneNumber: String): SendVerificationMessageRequestCode {
        if (phoneNumber == "70000000000") {
            code = ""

            repeat(codeLength) {
                code += "0"
            }

            println("Using debug code: $code")

            return SendVerificationMessageRequestCode.SUCCESSFUL
        }

        if (code == null) generateCode()

        var responseIsSuccessful: Boolean

        client.newCall(
            Request.Builder().url("https://sms.ru/sms/send?api_id=$token&to=$phoneNumber&msg=${
                URLEncoder.encode(
                    message.replace(
                        "%key%",
                        code!!
                    ), "UTF-8"
                )
            }&json=1").build()
        ).execute().use { response ->
            responseIsSuccessful = response.isSuccessful
            println(response.body?.string())
        }

        return if (responseIsSuccessful) SendVerificationMessageRequestCode.SUCCESSFUL else SendVerificationMessageRequestCode.NETWORK_ISSUE
    }

    fun isValidCode(code: String): Boolean {
        if (code == this.code) {
            this.code = null
            return true
        }

        return false
    }
}