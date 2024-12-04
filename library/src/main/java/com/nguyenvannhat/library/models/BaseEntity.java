package com.nguyenvannhat.library.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseEntity {
    @Column(name = "expiration")
    private Date expiration;
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }
}
