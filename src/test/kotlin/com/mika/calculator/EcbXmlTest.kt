package com.mika.calculator

import Cube
import EcbHistory
import NamespaceMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.slf4j.LoggerFactory
import java.io.StringWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

class EcbXmlTest {
    @Test
    fun parseXml() {
        val xml = EcbXmlTest::class.java.getResource("/sample.xml").readText()
        val jaxbContext = JAXBContext.newInstance(EcbHistory::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        val history = unmarshaller.unmarshal(xml.reader()) as? EcbHistory ?: fail(message = "Failed to parse XML")
        log.info("$history")
        Assertions.assertNotNull(history.Cube)
    }

    @Test
    fun marshal() {
        val history = EcbHistory(
                Cube = Cube(Cube = listOf(
                        Cube(time = "2020-03-22", Cube = listOf(
                                Cube(currency = "DKK", rate = "7.4691"),
                                Cube(currency = "SEK", rate = "11.0598")
                )))))
        val jaxbContext = JAXBContext.newInstance(EcbHistory::class.java)
        val marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8")
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", NamespaceMapper())
        val writer = StringWriter()
        marshaller.marshal(history, writer)
        log.info(writer.toString())

        val unmarshaller = jaxbContext.createUnmarshaller()
        val read = unmarshaller.unmarshal(writer.toString().reader()) as? EcbHistory ?: fail(message = "Failed to parse XML")
        log.info("$read")
        Assertions.assertTrue(history == read)

    }

    companion object {
        val log = LoggerFactory.getLogger(EcbXmlTest::class.java)
    }
}