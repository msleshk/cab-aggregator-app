package cab.app.rideservice.controller;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.exception.ValidationException;
import cab.app.rideservice.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
public class RideController {
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<Void> createRide(@RequestBody @Valid RideRequest rideRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        rideService.createRide(rideRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRide(@PathVariable("id") Long rideId, @RequestBody @Valid RideRequest rideRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        rideService.updateRide(rideId, rideRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<Void> updateRideStatus(@PathVariable("id") Long rideId, @PathVariable("status") String newRideStatus){
        rideService.updateRideStatus(rideId, newRideStatus);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable("id") Long rideId){
        rideService.deleteRide(rideId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable("id") Long rideId){
        return ResponseEntity.ok().body(rideService.getRide(rideId));
    }

    @GetMapping
    public ResponseEntity<List<RideResponse>> getAllRides(){
        return ResponseEntity.ok().body(rideService.getAllRides());
    }

    @GetMapping("/rides-by-passenger/{id}")
    public ResponseEntity<List<RideResponse>> getAllRidesByPassengerId(@PathVariable("id") Long passengerId){
        return ResponseEntity.ok().body(rideService.getAllRidesByPassenger(passengerId));
    }

    @GetMapping("/rides-by-driver/{id}")
    public ResponseEntity<List<RideResponse>> getAllRidesByDriverId(@PathVariable("id") Long driverId){
        return ResponseEntity.ok().body(rideService.getAllRidesByDriver(driverId));
    }

    @GetMapping("/rides-by-status/{status}")
    public ResponseEntity<List<RideResponse>> getRidesByStatus(@PathVariable("status") String status){
        return ResponseEntity.ok().body(rideService.getRidesByStatus(status));
    }
}
