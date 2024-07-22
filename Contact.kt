package ContactObjectInKotlin

data class Contact(private val contactBuilder: ContactBuilder) {
    var userName = contactBuilder.userName
    private var email = contactBuilder.email
    private var phoneNumber = contactBuilder.phoneNumber
    private var primaryAddress: String? = null
    private var secondaryAddress: String? = null
    private var dob: String? = null
    private var job: String? = null

    override fun toString(): String {
        return "Contact($userName)"
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
    var userName: String? = ""
    var email: String? = ""
    var phoneNumber: String? = ""
    private val emailRegex = Regex("^[a-zA-Z0-9]+[a-zA-Z0-9_.-]*@[a-zA-Z]+\\.[a-z]{2,3}$")
    private val phoneRegex = Regex("^(?:\\+?[0-9]{1,3})?\\s*[0-9]{3}\\s*[0-9]{3}\\s*[0-9]{4}$")

    fun setUserName(userName: UserName) = apply {  this.userName = userName.getUserName() }
    fun setEmail(email: String) = apply { this.email = if (CheckRegexObject.checkRegexPattern(emailRegex, email)) email else null }
    fun setPhoneNumber(phoneNumber: PhoneNumber) = apply { this.phoneNumber = if(CheckRegexObject.checkRegexPattern(
            phoneRegex, phoneNumber.getPhoneNumber())) phoneNumber.getPhoneNumber() else null }

    fun build(): Contact  = Contact(this)
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
    fun sortContacts(by: String = "username", order: String = "ascending", listOfContact: List<Contact>): List<Contact> {
        when (order) {
            "Ascending" -> this.contactList = sortByAscending(by,listOfContact)
            "Descending" -> this.contactList = sortByDescending(by, listOfContact)
        }
        return this.contactList
    }

    private fun sortByAscending(field: String, listOfContact: List<Contact>): List<Contact> {
        when (field) {
            "Username" -> listOfContact.sortedBy { it.userName }
            "Email" -> listOfContact.sortedBy { it.getEmail() }
        }

        return listOfContact
    }

    private fun sortByDescending(field: String, listOfContact: List<Contact>): List<Contact> {
        when (field) {
            "Username" -> listOfContact.sortedBy { it.userName }
            "Email" -> listOfContact.sortedBy { it.getEmail() }
        }

        return listOfContact.reversed()
    }

    private fun sortByUserNameAscending() {

    }
}

enum class OrderBy(by: String) {
    Username("username"),
    Email("email")
}

enum class Order(order: String) {
    Ascending("ascending"),
    Descending("descending")
}

object CheckRegexObject {
    fun checkRegexPattern(pattern: Regex, stringToCheck: String): Boolean = stringToCheck.matches(pattern)
}

fun main() {
    val userName = UserName("Gokuleyy1", "Sri")
    val userName1 = UserName("Gokuleyy2", "Sri")
    val userName2 = UserName("Gokuleyy3", "Sri")
    val addressPrimary1 = Address("10/20", "Street 1", district = "District", state = "State", country = "Country", pincode = 123456)
    val number = PhoneNumber("+91", "12324567890")
    val contact1: Contact = ContactBuilder.setUserName(userName).setEmail("Aoogle123@gmail.com").setPhoneNumber(number).build()
    val contact2: Contact = ContactBuilder.setUserName(userName1).setEmail("Boogle1231@gmail.com").setPhoneNumber(number).build()
    val contact3: Contact = ContactBuilder.setUserName(userName2).setEmail("Coogle1232@gmail.com").setPhoneNumber(number).build()

    val listOfContact: List<Contact> = listOf(contact1,contact2,contact3)
    println("${OrderBy.Email.name} ${Order.Descending.name}")
    println(SortContacts.sortContacts(OrderBy.Email.name,Order.Descending.name, listOfContact))
}
