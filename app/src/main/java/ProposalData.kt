import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ProposalData {
    fun getProposalsData(): QuerySnapshot? {
        val querySnapshot = Firebase.firestore.collection("proposals").get()
        while (!querySnapshot.isComplete) {}

        return if (querySnapshot.isSuccessful) querySnapshot.result else null
    }

    fun sendProposal(
        authorNumber: String,
        pointOfDeparture: String,
        pointOfArrival: String,
        departureDate: String,
        departureTime: String,
        userRole: String,
        passengerCount: Int,
        tripCost: Int
    ) {
        val proposalCollection = Firebase.firestore.collection("proposals")

        proposalCollection.add(hashMapOf(
            "role" to userRole,
            "authorNumber" to authorNumber,
            "pointOfDeparture" to pointOfDeparture,
            "pointOfArrival" to pointOfArrival,
            "departureDate" to departureDate,
            "departureTime" to departureTime,
            "passengerCount" to passengerCount,
            "tripCost" to tripCost
        ))
    }
}