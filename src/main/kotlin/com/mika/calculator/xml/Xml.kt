import com.sun.xml.bind.marshaller.NamespacePrefixMapper
import javax.xml.bind.annotation.*

const val NS_GESMES = "http://www.gesmes.org/xml/2002-08-01"
const val NS_ECB = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref"

@XmlRootElement(name = "Envelope", namespace = NS_GESMES)
@XmlAccessorType(XmlAccessType.FIELD)
data class EcbHistory(
        @field:XmlElement(name = "Cube", namespace = NS_ECB, required = false)
        var Cube: Cube? = null
)

@XmlAccessorType(XmlAccessType.FIELD)
data class Cube (
        @field:XmlAttribute(name = "time", required = false)
        var time: String? = null,
        @field:XmlAttribute(name = "currency", required = false)
        var currency: String? = null,
        @field:XmlAttribute(name = "rate", required = false)
        var rate: String? = null,
        // Important:
        // Annotations need to target field
        // Namespace here and in root element must match
        @field:XmlElement(name = "Cube", namespace = NS_ECB, required = false)
        var Cube: List<Cube>? = null
)

class NamespaceMapper: NamespacePrefixMapper() {
    override fun getPreferredPrefix(namespaceUri: String?, suggestion: String?, requirePrefixed: Boolean): String {
        if (namespaceUri == NS_GESMES) {
            return "gesmes"
        }
        return suggestion ?: ""
    }

    override fun getPreDeclaredNamespaceUris(): Array<String> {
        return arrayOf(NS_GESMES)
    }
}
