@XmlSchema(
        namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref",
        xmlns = {
                @XmlNs(prefix = "", namespaceURI = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref"),
                @XmlNs(prefix = "gesmes", namespaceURI = "http://www.gesmes.org/xml/2002-08-01")
        },
        elementFormDefault = XmlNsForm.QUALIFIED,
        attributeFormDefault = XmlNsForm.UNQUALIFIED
)
package com.mika.calculator.xml;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
