package com.richcode.job.domain;

import com.richcode.job.dto.AsyncJobStatus;
import com.richcode.job.dto.AsyncJobType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AsyncJobSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contextId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AsyncJobType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AsyncJobStatus status;

    @Column
    private String username;

    @Column
    private String parameters;

    @Column
    private String result;

    @CreatedDate
    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false, updatable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false)
    private OffsetDateTime updated;

    public Duration getDurationTime() {
        return status.isFinal() ? Duration.between(created, updated) : Duration.ZERO;
    }

}
