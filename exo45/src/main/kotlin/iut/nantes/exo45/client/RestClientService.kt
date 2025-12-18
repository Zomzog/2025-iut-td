package iut.nantes.exo45.client

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
@Profile("restclient")
class RestClientService(val restClient: RestClient) {
    fun hello(): HelloDto? {
        return restClient.get().uri {
            it.path("/api/v1/hello")
                .queryParam("name", "world")
                .build()
        }
            .retrieve()
            .body(HelloDto::class.java)
    }

    fun noHello(): HelloDto? {
        return restClient.post().uri {
            it.path("/api/v1/noHello")
                .build()
        }.body(HelloDto("No hello"))
            .retrieve()
            .body(HelloDto::class.java)
    }

    fun error() {
        restClient.get().uri {
            it.path("/api/v1/error").build() }
            .retrieve()
            .onStatus({ it.is5xxServerError }, {req, res -> throw MyInternalError()})
            .body(HelloDto::class.java)
    }
}