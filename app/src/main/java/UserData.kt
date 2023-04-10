import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserData {
    enum class RegistrationRequestCode {
        SUCCESSFUL, ALREADY_LOGIN, USER_WITH_THIS_NUMBER_ALREADY_EXISTS, NETWORK_ISSUE
    }
    enum class LoginRequestCode {
        SUCCESSFUL, ALREADY_LOGIN, USER_WITH_THIS_NUMBER_DOES_NOT_EXISTS_OR_WRONG_PASSWORD, NETWORK_ISSUE
    }

    fun getData(context: Context): SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun getPhoneNumber(context: Context): String? = getData(context).getString("phoneNumber", null)

    fun isUserAlreadyLogin(context: Context): Boolean = getPhoneNumber(context) != null

    fun registrationUser(
        context: Context,
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        phoneNumber: String,
        password: String
    ): RegistrationRequestCode {
        if (isUserAlreadyLogin(context)) return RegistrationRequestCode.ALREADY_LOGIN

        val userCollection = Firebase.firestore.collection("users")
        var registrationRequestCode: RegistrationRequestCode = RegistrationRequestCode.SUCCESSFUL

        userCollection.get()
            .addOnSuccessListener {
                result -> result.forEach {
                    document -> if (document.id == phoneNumber) {
                        registrationRequestCode = RegistrationRequestCode.USER_WITH_THIS_NUMBER_ALREADY_EXISTS
                        return@forEach
                    }
                }
            }
            .addOnFailureListener {
                registrationRequestCode = RegistrationRequestCode.NETWORK_ISSUE
            }

        if (registrationRequestCode != RegistrationRequestCode.SUCCESSFUL) return registrationRequestCode

        userCollection.add(hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "dateOfBirth" to dateOfBirth,
            "password" to password
        )).addOnFailureListener {
            registrationRequestCode = RegistrationRequestCode.NETWORK_ISSUE
        }

        if (registrationRequestCode != RegistrationRequestCode.SUCCESSFUL) return registrationRequestCode

        getData(context).edit().apply {
            putString("phoneNumber", phoneNumber)
            apply()
        }

        return registrationRequestCode
    }

    fun loginUser(context: Context, phoneNumber: String, password: String): LoginRequestCode {
        if (isUserAlreadyLogin(context)) return LoginRequestCode.ALREADY_LOGIN

        val userCollection = Firebase.firestore.collection("users")
        var loginRequestCode: LoginRequestCode = LoginRequestCode.SUCCESSFUL

        userCollection.get()
            .addOnSuccessListener {
                result -> result.forEach {
                    document -> if (document.id == phoneNumber) {
                        if (document!!.get("password")!! != password)
                            loginRequestCode = LoginRequestCode.USER_WITH_THIS_NUMBER_DOES_NOT_EXISTS_OR_WRONG_PASSWORD

                        return@forEach
                    }
                }

                loginRequestCode = LoginRequestCode.USER_WITH_THIS_NUMBER_DOES_NOT_EXISTS_OR_WRONG_PASSWORD
            }
            .addOnFailureListener {
                loginRequestCode = LoginRequestCode.NETWORK_ISSUE
            }

        if (loginRequestCode != LoginRequestCode.SUCCESSFUL) return loginRequestCode

        getData(context).edit().apply {
            putString("phoneNumber", phoneNumber)
            apply()
        }

        return LoginRequestCode.SUCCESSFUL
    }
}