import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserData {
    enum class RegistrationRequestCode {
        SUCCESSFUL, ALREADY_LOGIN, USER_WITH_THIS_NUMBER_ALREADY_EXISTS, NETWORK_ISSUE
    }
    enum class LoginRequestCode {
        SUCCESSFUL, ALREADY_LOGIN, USER_WITH_THIS_NUMBER_DOES_NOT_EXISTS_OR_WRONG_PASSWORD, NETWORK_ISSUE
    }

    fun getLocalUserData(context: Context): SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun getUsersData(): QuerySnapshot? {
        val querySnapshot = Firebase.firestore.collection("users").get()
        while (!querySnapshot.isComplete) {}

        return if (querySnapshot.isSuccessful) querySnapshot.result else null
    }

    fun logOut(context: Context) {
        getLocalUserData(context).edit().putString("phoneNumber", null).apply()
    }

    fun getUserDataByPhoneNumber(phoneNumber: String?): QueryDocumentSnapshot? {
        if (phoneNumber == null) return null

        getUsersData()?.forEach { queryDocumentSnapshot ->
            if (queryDocumentSnapshot.id == phoneNumber)
                return queryDocumentSnapshot
        }

        return null
    }

    fun getUserData(context: Context): QueryDocumentSnapshot? = getUserDataByPhoneNumber(getPhoneNumber(context))

    fun getPhoneNumber(context: Context): String? = getLocalUserData(context).getString("phoneNumber", null)

    fun getUserFirstName(context: Context): String? = getUserData(context)?.getString("firstName")

    fun hasUserWithPhoneNumber(phoneNumber: String): Boolean = getUserDataByPhoneNumber(phoneNumber) != null

    fun isPasswordCorrect(phoneNumber: String, password: String): Boolean = getUserDataByPhoneNumber(phoneNumber)!!.get("password") == password

    fun isUserAlreadyLogin(context: Context): Boolean = getPhoneNumber(context) != null

    fun registrateUser(
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

        userCollection.document(phoneNumber).set(hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "dateOfBirth" to dateOfBirth,
            "password" to password
        )).addOnFailureListener {
            registrationRequestCode = RegistrationRequestCode.NETWORK_ISSUE
        }

        if (registrationRequestCode != RegistrationRequestCode.SUCCESSFUL) return registrationRequestCode

        getLocalUserData(context).edit().apply {
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

        getLocalUserData(context).edit().apply {
            putString("phoneNumber", phoneNumber)
            apply()
        }

        return LoginRequestCode.SUCCESSFUL
    }
}