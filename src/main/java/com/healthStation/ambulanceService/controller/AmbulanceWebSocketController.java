package com.healthStation.ambulanceService.controller;

import com.healthStation.ambulanceService.MessageDTOS.AmbulanceNotificationMessage;
import com.healthStation.ambulanceService.MessageDTOS.AmbulanceResponseMessage;
import com.healthStation.ambulanceService.MessageDTOS.RequestUpdateMessage;
import com.healthStation.ambulanceService.model.*;
import com.healthStation.ambulanceService.repository.AmbulanceNotificationRepository;
import com.healthStation.ambulanceService.repository.AmbulanceRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AmbulanceWebSocketController {
    ///I dont feel confident in this, so might change it as a bit of it is gpt generated and one must never trust AI (Cool Coding>> vibe Coding)
    /// Use the below link as reference and look up
    //https://www.geeksforgeeks.org/advance-java/using-websocket-to-build-an-interactive-web-application-in-spring-boot/
    private final SimpMessagingTemplate messageTemplate;

    private final AmbulanceRequestRepository ambulanceRequestRepository;

    private final AmbulanceNotificationRepository ambulanceNotificationRepository;

    public void notifyNearbyAmbulances(AmbulanceRequest request, List<Ambulance> nearbyAmbulances){
        for(Ambulance a:nearbyAmbulances){
            Long id=a.getAmbulanceId();
            AmbulanceNotificationMessage msg=new AmbulanceNotificationMessage();
            msg.setRequestId(request.getRequestId());
            msg.setLocation(request.getPickupLocation());
            //Fuck it, i will just hard code it here,
            msg.setServiceType("CRITICAL");

            messageTemplate.convertAndSend("/topic/ambulance"+id,msg);
        }
    }

    @MessageMapping("/ambulance/response")
    @Transactional
    public void handleAmbulanceResponse(AmbulanceResponseMessage dto){
        ///Ahh this is such a conceptually deep method
        ///So the flow ism first from the ambulance response message (Made up of requestId,ambulanceId and response string), from that i get the request and check if it is valid or not
        ///If the request is valid, i check if it has already been assigned using AcceptedAt() timestamp, if null not assigned
        ////If not assigned, then from the ambulance response message i get the response string, if it is accept, then i accept it
            ///Once accepted, i set the status of the request to accepted and the accepted time is set to Instant.now()
            /// Also we must update in the ambulance notification repository, and make its status as accepted
            ///Finally we broadcast to other ambulances that this has been accepted
        ////If the response string is something else then we reject it
        Optional<AmbulanceRequest> holder=ambulanceRequestRepository.findByIdForUpdate(dto.getRequestId());
        if(holder.isEmpty()) return;
        AmbulanceRequest ambulanceRequest=holder.get();

        if(ambulanceRequest.getAcceptedAt() !=null ){
            messageTemplate.convertAndSend("/topic/ambulance"+dto.getAmbulanceId(),
                    new RequestUpdateMessage("FAILED", dto.getRequestId(),null));
            ////We value a lot about privacy (Obvious sarcasm) so we dont let out the assignedAmbulanceId
            //// More like i messed up and Patchwork :)
            return;
        }

        if("ACCEPT".equalsIgnoreCase(dto.getResponse())){
            //For the request part
            ambulanceRequest.setAcceptedAt(Instant.now());
            ambulanceRequest.setStatus(AmbulanceRequestStatus.ACCEPTED);
            ambulanceRequestRepository.save(ambulanceRequest);

            //For the Notification part
            ambulanceNotificationRepository.updateStatus(dto.getAmbulanceId(),ambulanceRequest.getRequestId(), AmbulanceNotificationStatusType.ACCEPTED);

            //notify all or broadcast
            messageTemplate.convertAndSend("/topic/request"+dto.getRequestId(),
                    new RequestUpdateMessage("ASSIGNED", dto.getRequestId(),dto.getAmbulanceId()));
        }else{
            ambulanceNotificationRepository.updateStatus(dto.getAmbulanceId() ,ambulanceRequest.getRequestId(),AmbulanceNotificationStatusType.REJECTED);
        }
    }
}

