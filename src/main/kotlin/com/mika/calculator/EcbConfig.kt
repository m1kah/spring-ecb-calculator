package com.mika.calculator

import EcbHistory
import feign.Feign
import feign.Logger
import feign.jaxb.JAXBContextFactory
import feign.jaxb.JAXBDecoder
import feign.jaxb.JAXBEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EcbConfig {
    @Bean
    fun ecbService(ecb: Ecb): EcbService {
        return EcbService(ecb = ecb)
    }

    @Bean
    fun ecb(): Ecb {
        val jaxbFactory = JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
                .build(listOf(EcbHistory::class.java))

        return Feign.builder()
                .client(OkHttpClient())
                .encoder(JAXBEncoder(jaxbFactory))
                .decoder(JAXBDecoder(jaxbFactory))
                .logger(Slf4jLogger(Ecb::class.java))
                .logLevel(Logger.Level.FULL)
                .target(Ecb::class.java, "https://www.ecb.europa.eu")
    }
}
