package com.controller;

import java.util.List;

import com.entity.Thoroughbred;
import com.payload.MessageResponse;
import com.service.ThoroughbredService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/thoroughbred", produces = "application/json")
public class ThoroughbredController {
    
    @Autowired
    private ThoroughbredService thoroughbredService;

    @PostMapping("/save")
    public ResponseEntity<MessageResponse> save (@RequestBody Thoroughbred thoroughbred) {
        Thoroughbred result = thoroughbredService.save(thoroughbred);

        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<Thoroughbred>> findAll() {
        List<Thoroughbred> thoroughbreds = thoroughbredService.findAll();

        return ResponseEntity.ok(thoroughbreds);
    }

    @GetMapping("/find-id/{id}")
    public ResponseEntity<Thoroughbred> findById(@PathVariable Long id) {
        Thoroughbred thoroughbred = thoroughbredService.findById(id);

        return ResponseEntity.ok(thoroughbred);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> update(@RequestBody Thoroughbred thoroughbred) {
        Thoroughbred result = thoroughbredService.update(thoroughbred);

        if(result != null) {
            return ResponseEntity.ok(new MessageResponse("success"));
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        try{
            thoroughbredService.delete(id);

            return ResponseEntity.ok(new MessageResponse("success"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new MessageResponse("failed"));
    }
}
