package com.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Thoroughbred {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int purebredLevel;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdate;
    
    @ManyToOne
    @JoinColumn( name = "user_system_id")
    private UserSystem userSystem;

    @OneToMany(mappedBy = "thoroughbred")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPurebredLevel() {
        return purebredLevel;
    }

    public void setPurebredLevel(int purebredLevel) {
        this.purebredLevel = purebredLevel;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public UserSystem getUserSystem() {
        return userSystem;
    }

    public void setUserSystem(UserSystem userSystem) {
        this.userSystem = userSystem;
    }

    
}
