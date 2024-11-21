package com.netone.random_draw.model;

import com.netone.random_draw.security.CurrentAuditor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@Audited
public abstract class BaseEntity implements Serializable {
    private static final CurrentAuditor usernameAuditorAware = new CurrentAuditor();

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Version
    private long version = 1L;

    @Column(name = "created_by_user", updatable = false)
    @CreatedBy
    private String createdByUser;

    @Column(name = "creation_time", updatable = false)
    private LocalDateTime creationTime;

    @Column(name = "modified_by_user")
    @LastModifiedBy
    private String modifiedByUser;

    @Column(name = "modification_time")
    private LocalDateTime modificationTime;

    protected boolean activeStatus;

    @PrePersist
    public void prePersist() {
        this.creationTime = LocalDateTime.now();
        this.activeStatus = true;
        this.modificationTime = LocalDateTime.now();
        this.createdByUser = usernameAuditorAware.getCurrentAuditor().map(User::getUsername).orElse("Anonymous User");
        this.modifiedByUser = usernameAuditorAware.getCurrentAuditor().map(User::getUsername).orElse("Anonymous User");
    }

    @PreUpdate
    public void preUpdate() {
        this.modificationTime = LocalDateTime.now();
        this.modifiedByUser = usernameAuditorAware.getCurrentAuditor().map(User::getUsername).orElse("Anonymous User");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
