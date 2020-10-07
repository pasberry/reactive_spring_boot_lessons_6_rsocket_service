package com.example.rsocketservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.util.stream.Stream

@SpringBootApplication
class RsocketServiceApplication

fun main(args: Array<String>) {
    runApplication<RsocketServiceApplication>(*args)
}

@Controller
class GreetingsController(val greetingService: GreetingService) {

    @MessageMapping("greetings.{timeInSeconds}")
    fun greet(@DestinationVariable("timeInSeconds") timeInSeconds:Int,
              request: GreetingRequest) : Flux<GreetingResponse> = this
            .greetingService.greet(request,timeInSeconds)


}

data class GreetingRequest(val name:String)
data class GreetingResponse(val message:String)

@Service
class GreetingService {

    fun greet(request:GreetingRequest, delay:Int) = Flux
            .fromStream( Stream.generate
                 {GreetingResponse("Hello ${request.name} @ ${Instant.now()} !")})
            .delayElements(Duration.ofSeconds(delay.toLong()))

}

