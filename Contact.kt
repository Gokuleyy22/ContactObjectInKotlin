package contactObjectInKotlin

data class Contact(private val contactBuilder: ContactBuilder) {
    var userName = contactBuilder.userName
    private var email = contactBuilder.email
    private var phoneNumber = contactBuilder.phoneNumber
    private var primaryAddress: String? = null
    private var secondaryAddress: String? = null
    private var contactType: TypeOfContact? = contactBuilder.contactType ?: TypeOfContact.OTHER
    private var dob: String? = null
    private var job: String? = null

    override fun toString(): String {
        return "Contact(username = $userName, email = $email)"
    }


    fun setPrimaryAddress(address: Address) = apply { this.primaryAddress = address.getAddress() }

    fun setSecondaryAddress(address: Address) = apply { this.secondaryAddress = address.getAddress() }

    fun setDOB(dob: DOB) = apply { this.dob = dob.getDOB() }

    fun setJob(job: JobDescription) = apply { this.job = job.getJobDescription() }

    fun getJob(): String =  this.job ?: "Invalid Job"

    fun getPhoneNumber(): String  = this.phoneNumber ?: "Invalid Phone Number"

    fun getPrimaryAddress(): String = this.primaryAddress ?: "Invalid Primary Address"

    fun getSecondaryAddress(): String = this.secondaryAddress ?: "Invalid Secondary Address"

    fun getEmail(): String = this.email ?: "Invalid Email"

    fun getDOB(): String = this.dob ?: "Invalid DOB"

    fun getContactType(): TypeOfContact? = this.contactType
}

data class Address(
    private val doorNo: String,
    private val streetLane1: String,
    private val streetLane2: String = "",
    private val district: String,
    private val state: String,
    private val country: String,
    private val pincode: Int
) {
    fun getAddress(): String = "$doorNo ,$streetLane1 ,$streetLane2 ,$district ,$state ,$country ,$pincode"
}

data class DOB(
    private val date: Int,
    private val month: Int,
    private val year: Int
) {
    fun getDOB(): String = "$date/$month/$year"
}

data class Qualification (
    private val qualification: String,
    private val completedYear: Int
) {
    fun getQualification(): String = "$qualification-$completedYear"
}

class UserName(private val firstName: String, private val lastName: String?) {
    fun getUserName(): String {
        return firstName + (lastName ?: "")
    }
}

data class PhoneNumber(private val countryCode: String, private val phoneNumber: String) {
    fun getPhoneNumber(): String =  countryCode + phoneNumber
}

object ContactBuilder {
    var userName: String = ""
    var email: String? = ""
    var phoneNumber: String? = ""
    var contactType: TypeOfContact? = null
    private val emailRegex = Regex("^[a-zA-Z0-9]+[a-zA-Z0-9_.-]*@[a-zA-Z]+\\.[a-z]{2,3}$")
    private val phoneRegex = Regex("^(?:\\+?[0-9]{1,3})?\\s*[0-9]{3}\\s*[0-9]{3}\\s*[0-9]{4}$")


    fun setUserName(userName: UserName) = apply {  this.userName = userName.getUserName() }
    fun setEmail(email: String) = apply { this.email = if (CheckRegexObject.checkRegexPattern(emailRegex, email)) email else null }
    fun setPhoneNumber(phoneNumber: PhoneNumber) = apply { this.phoneNumber = if(CheckRegexObject.checkRegexPattern(
            phoneRegex, phoneNumber.getPhoneNumber())) phoneNumber.getPhoneNumber() else null }
    fun setContactType(type: String) = apply { this.contactType = TypeOfContact.getFromString(type) }

    fun build(): Contact?  = if (this.userName != "") Contact(this) else null
}

data class JobDescription(
    private val jobName: String,
    private val experience: String,
    private val jobLocation: Address
) {
    fun getJobDescription(): String = "$jobName, $experience, ${jobLocation.getAddress()}"
}

object SortContacts {
    private var contactList: List<Contact> = listOf()

    fun sortContacts(with: String = "username", order: String = "ascending", listOfContact: List<Contact>): List<Contact> {
        when (order.lowercase()) {
            "ascending" -> contactList = sortByAscending(with, listOfContact)
            "descending" -> contactList = sortByDescending(with, listOfContact)
        }
        return contactList
    }

    private fun sortByAscending(with: String, listOfContact: List<Contact>): List<Contact> {
        return when (with.lowercase()) {
            "username" -> listOfContact.sortedBy { it.userName }
            "email" -> listOfContact.sortedBy { it.getEmail() }
            else -> listOfContact
        }
    }

    private fun sortByDescending(with: String, listOfContact: List<Contact>): List<Contact> {
        return when (with.lowercase()) {
            "username" -> listOfContact.sortedByDescending { it.userName }
            "email" -> listOfContact.sortedByDescending { it.getEmail() }
            else -> listOfContact
        }
    }
}

enum class TypeOfContact {
    FAMILY,
    FRIENDS,
    BUSINESS,
    OTHER;

    companion object {
        fun getFromString(type: String): TypeOfContact {
            return when (type.lowercase()) {
                "family" -> FAMILY
                "friends" -> FRIENDS
                "business" -> BUSINESS
                else -> OTHER
            }
        }
    }
}
enum class OrderBy(by: String) {
    USERNAME("username"),
    EMAIL("email")
}

enum class Order(order: String) {
    ASCENDING("ascending"),
    DESCENDING("descending")
}

object SearchContacts {
    private var listOfContacts =  mutableListOf<Contact>()
    private var currentText = ""
    fun searchContact(listOfContact: List<Contact>) {
        print("Start typing to search contact(type 'exit' to finish) ->")
        while (true) {
            val userInput = readlnOrNull() ?: continue
            this.listOfContacts = arrayListOf()
            if (userInput.equals("exit", ignoreCase = true)) break
            currentText += userInput

            println(currentText)
            println(sortAndFilterContacts(currentText, listOfContact))
        }
    }

    private fun sortAndFilterContacts(input: String, listOfContact: List<Contact>): List<Contact> {
        listOfContact.forEach { if (it.userName.contains(input, ignoreCase = true)) this.listOfContacts.add(it) }
//        println("Given list $listOfContact")
//        println("Returning list $listOfContacts")
        return listOfContacts.sortedByDescending { it.userName }
    }
}

//How to change from string to enumeration
//Enum name should be upper case
//Priority setting in Kotlin

object CheckRegexObject {
    fun checkRegexPattern(pattern: Regex, stringToCheck: String): Boolean = stringToCheck.matches(pattern)
}

fun main() {
    val userName1 = UserName("Gokuleyy1", "Sri")
    val userName2 = UserName("Sruthi2", "Sri")
    val userName3 = UserName("Jashwin3", "Sri")
    val userName4 = UserName("Jashwin3", "Sri")
    val addressPrimary1 = Address("10/20", "Street 1", district = "District", state = "State", country = "Country", pincode = 123456)
    val number = PhoneNumber("+91", "12324567890")
    val contact1: Contact? = ContactBuilder.setUserName(userName1).setEmail("Aoogle123@gmail.com").setPhoneNumber(number).setContactType("family").build()
    val contact2: Contact? = ContactBuilder.setUserName(userName2).setEmail("Boogle123@gmail.com").setPhoneNumber(number).build()
    val contact3: Contact? = ContactBuilder.setUserName(userName3).setEmail("Coogle123@gmail.com").setPhoneNumber(number).build()
    val contact4: Contact? = ContactBuilder.setUserName(userName4).setEmail("Doogle123@gmail.com").setPhoneNumber(number).build()

    val listOfContact: List<Contact> = listOf(contact1!!,contact2!!,contact3!!)

    println(contact1.getContactType())

    val sortedContactsByUserNameAscending = SortContacts.sortContacts(OrderBy.USERNAME.name,Order.ASCENDING.name, listOfContact)
    val sortedContactsByEMAILAscending = SortContacts.sortContacts(OrderBy.EMAIL.name,Order.ASCENDING.name, listOfContact)
    val sortedContactsByUserNameDescending = SortContacts.sortContacts(OrderBy.USERNAME.name,Order.DESCENDING.name, listOfContact)
    val sortedContactsByEMAILDescending = SortContacts.sortContacts(OrderBy.EMAIL.name,Order.DESCENDING.name, listOfContact)

    SearchContacts.searchContact(listOfContact)
//    println("Sorted by username Ascending: ")
//    println(sortedContactsByUserNameAscending)
//    println("Sorted by email Ascending: ")
//    println(sortedContactsByEMAILAscending)
//    println("Sorted by username Descending: ")
//    println(sortedContactsByUserNameDescending)
//    println("Sorted by email Descending: ")
//    println(sortedContactsByEMAILDescending)

}
